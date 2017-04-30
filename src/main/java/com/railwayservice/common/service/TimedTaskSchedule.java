package com.railwayservice.common.service;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.merchantmanage.service.MerchantmenService;
import com.railwayservice.messages.service.NoticeService;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.service.MainOrderService;
import com.railwayservice.order.service.OrderStatic;
import com.railwayservice.order.vo.OrderQuartzVo;
import com.railwayserviceWX.config.ConfigParser;
import com.railwayserviceWX.controller.WechatControllerTest;
import com.railwayserviceWX.util.WechatPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务以及初始化任务
 *
 * @author lid
 * @date 2017.3.31
 */
@Service
public class TimedTaskSchedule implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(TimedTaskSchedule.class);

    /**
     * 将所有未处理的订单放在map中
     */
    public static Map<String, Date> waitAcceptOrders;

    private MainOrderService mainOrderService;

    private NoticeService noticeService;

    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Autowired
    public void setMainOrderService(MainOrderService mainOrderService) {
        this.mainOrderService = mainOrderService;
    }

    @Transactional
    //@Scheduled(cron = "0/10 * *  * * ? ")   //每10秒执行一次
    public void waitAcceptOrders() {
        /*logger.info("定时任务执行：处理待接单超时订单：waitAcceptOrders");*/
        //从配置文件读取超时时间
        final String orderOverTime = ConfigParser.getOrderOverTime();

        if (0 == waitAcceptOrders.size()) {
            /*logger.info("0 == waitAcceptOrders.size()！");*/
            return;
        }

        Calendar nowTime = Calendar.getInstance();

        nowTime.add(Calendar.MINUTE, 0 - Integer.valueOf(orderOverTime));

        Date overTime = nowTime.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat(AppConfig.DATE_TIME_FORMAT_PAY);

        //处理所有未处理的订单
        for (String key : waitAcceptOrders.keySet()) {
            //只处理X分钟仍未接单的订单
            if (waitAcceptOrders.get(key).before(overTime)) {
                //退款，并设置订单状态为已超时
                logger.info("取消订单并退款！key：" + key + ",value:" + sdf.format(waitAcceptOrders.get(key)));
                MainOrder order = mainOrderService.findMainOrderByOrderId(key);

                logger.info("order.getOrderStatus()：" + order.getOrderStatus());
                logger.info("order.getPayStatus()：" + order.getPayStatus());

                //处理等待商家接单并且支付状态为未退款的订单
                if ((OrderStatic.MAINORDER_STATUS_WAIT_ACCEPT == order.getOrderStatus())
                        && (OrderStatic.PAY_STATUS_REFUNDED != order.getPayStatus())) {
                    logger.info("超时退款！退款单号：" + key);

                    int totalMoney = order.getOrderTotalPrice().multiply(new BigDecimal("100")).intValue();
                    logger.info("totalMoney：" + totalMoney);

                    Map<String, Object> mapResult = new HashMap<String, Object>();

                    try {
                        mapResult = WechatPayUtil.refund(order.getTransactionId(),
                                order.getOrderNo(), order.getOrderNo(), totalMoney, totalMoney);
                    } catch (Exception e) {
                        logger.error("订单：" + order.getOrderNo() + " 退款调用微信支付接口异常：", e);
                    }

                    logger.info("mapResult:" + mapResult.toString());

                    if ("SUCCESS".equals(mapResult.get("returnCode"))) {
                        //设置订单状态为已退款，并设置退款单号和退款时间
                        order.setPayStatus(OrderStatic.PAY_STATUS_REFUNDED);

                        if (StringUtils.hasText(String.valueOf(mapResult.get("refund_id")))) {
                            order.setRefundId(String.valueOf(mapResult.get("refund_id")));
                        }

                        order.setOutRefundNo(order.getOrderNo());
                        order.setRefundDate(new Date());
                        order.setUpdateDate(new Date());
                        order.setOrderStatus(OrderStatic.MAINORDER_STATUS_OVERTIME);
                        mainOrderService.updateMainOrder(order);

                        //发消息通知用户
                        waitAcceptOrders.remove(key);
                        noticeService.noticeToUserNoException(order);
                        logger.info(order.getOrderNo() + "订单状态已设置为超时并退款！");
                    } else {
                        throw new AppException("订单：" + order.getOrderNo() + " 退款失败：" + mapResult.get("return_msg"));
                    }
                } else {
                    logger.info(order.getOrderNo() + "移除已处理的订单！");
                    waitAcceptOrders.remove(key);
                }
                logger.info("无需要退款的订单");
            }
        }
    }

    /**
     * 当bean装载完之后调用一次初始化任务
     * 获取所有等待商家接单状态的订单
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        waitAcceptOrders = new ConcurrentHashMap<>();

        //获取所有等待商家接单的订单
        List<OrderQuartzVo> waitAcceptOrderVos = mainOrderService.queryMainOrderByStatus(OrderStatic.MAINORDER_STATUS_WAIT_ACCEPT);
        logger.info("waitAcceptOrderVos.size():" + waitAcceptOrderVos.size());

        for (OrderQuartzVo vo : waitAcceptOrderVos) {
            waitAcceptOrders.put(vo.getOrderId(), vo.getCreateDate());
        }

        logger.info("定时任务初始化完成！waitAcceptOrders.size():" + waitAcceptOrders.size());
        
        //判断是否为测试环境
        final String isTest = ConfigParser.getIsTest();
        logger.info("读取配置文件成功，1为测试环境，0为生产环境，当前环境为:"+isTest);
        
        if("1".equals(isTest)){
        	WechatControllerTest.isTest = null;
        	WechatControllerTest.isTest = new Boolean(true);
        	logger.info("设置环境成功");
        }
    }
}
