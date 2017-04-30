package com.railwayserviceWX.controller;

import com.railwayservice.application.util.RandomString;
import com.railwayservice.application.util.SendMSG;
import com.railwayservice.merchantmanage.dao.MerchantDao;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.messages.service.ChannelInfoService;
import com.railwayservice.order.dao.MainOrderDao;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.entity.PayRecord;
import com.railwayservice.order.entity.SubOrder;
import com.railwayservice.order.service.*;
import com.railwayservice.productmanage.dao.ProductDao;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Date;
import java.util.List;

/**
 * 微信业务入口控制器。
 *
 * @author lid
 * @date 2017.2.26
 */
@Controller
@RequestMapping(value = "/payNotifyFromWechat", produces = {"application/json;charset=UTF-8"})
@Api(value = "微信业务入口控制器", description = "微信业务入口控制器")
public class PayNotifyFromWechat {

    private final Logger logger = LoggerFactory.getLogger(PayNotifyFromWechat.class);

    //通知微信服务器成功获取支付通知的字符串
    private static final String weixinNotifyResult = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    //通知商户的标题
    private static final String title = "新的订单";

    //通知商户的内容
    private static final String description = "您的商户有一条新的订单，请及时处理！";
    private static final String descriptionWithOrderNo = "您的商户有一条新的订单[1]，请及时处理！";

    private MainOrderDao mainOrderDao;

    private PayRecordService payRecordService;

    private UserService userService;

    private ShoppingCartService shoppingCartService;

    private ChannelInfoService channelInfoService;

    private SubOrderService subOrderService;

    private ProductDao productDao;

    private MerchantDao merchantDao;

    private OrderStatusRecordService orderStatusRecordService;

    @Autowired
    public void setOrderStatusRecordService(OrderStatusRecordService orderStatusRecordService) {
        this.orderStatusRecordService = orderStatusRecordService;
    }

    @Autowired
    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setSubOrderService(SubOrderService subOrderService) {
        this.subOrderService = subOrderService;
    }

    @Autowired
    public void setChannelInfoService(ChannelInfoService channelInfoService) {
        this.channelInfoService = channelInfoService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPayRecordService(PayRecordService payRecordService) {
        this.payRecordService = payRecordService;
    }

    @Autowired
    public void setMainOrderDao(MainOrderDao mainOrderDao) {
        this.mainOrderDao = mainOrderDao;
    }

    /**
     * 订单支付通知
     * 1,修改订单状态
     * 2,保存支付记录
     * 3,清空购物车
     * 4,通知商户
     * 5，添加商品销量
     * 6，添加商户销量
     * 7,将订单放入定时任务的map中
     * 8,添加订单状态记录
     *
     * @param req
     * @param res
     * @throws Exception
     * @author lid
     * @date 2017.3.14
     */
    @ResponseBody
    @RequestMapping("payNotifyFromOrder")
    public void payNotifyFromOrder(HttpServletRequest req, HttpServletResponse res) throws Exception {
        logger.info("payNotifyFromOrder");
        StringBuffer xmlStr = new StringBuffer();

        try {
            BufferedReader reader = req.getReader();
            String line = null;
            while ((line = reader.readLine()) != null) {
                xmlStr.append(line);
            }

            //解析字符串 
            String notifyResult = xmlStr.toString();
            logger.info("notifyResult:" + notifyResult);

            if ((notifyResult.contains("<result_code><![CDATA[SUCCESS]]></result_code>"))
                    && (notifyResult.contains("<return_code><![CDATA[SUCCESS]]></return_code>"))) {
                String orderId = this.getParameter(notifyResult, "attach");
                String openid = this.getParameter(notifyResult, "openid");
                if ((!StringUtils.hasText(orderId))
                        || (!StringUtils.hasText(openid))) {
                    return;
                }

                logger.info("orderId:" + orderId + " openid:" + openid);

                User user = userService.getUserByOpenid(openid);
                MainOrder mainOrder = mainOrderDao.findOne(orderId);
                //如果订单支付状态为已支付，则直接返回
                if (OrderStatic.PAY_STATUS_PAYED == mainOrder.getPayStatus()) {
                    res.getWriter().write(weixinNotifyResult);
                    return;
                }

                //1,修改订单状态
                //将订单状态改为已支付     订单状态设置为待接单
                //因为支付回调为异步，所以将订单状态设为等待商家接单时需先判断当前订单状态是否为已取消
                if (mainOrder.getOrderStatus() == OrderStatic.MAINORDER_STATUS_WAIT_PAY) {
                    mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_ACCEPT);
                    mainOrder.setPayStatus(OrderStatic.PAY_STATUS_PAYED);
                }

                //设置微信支付订单号
                String transactionId = this.getParameter(notifyResult, "transaction_id");
                //获取订单总金额
                Integer totalFee = Integer.valueOf(getTotalFee(notifyResult));
                logger.info("totalFee:" + totalFee);
                mainOrder.setTransactionId(transactionId);
                // 生成收货码
                mainOrder.setReceiveCode(RandomString.randomNumberString(4));
                //设置支付时间
                mainOrder.setPayDate(new Date());
                mainOrderDao.save(mainOrder);
                logger.info("修改订单状态成功");

                //2,保存支付记录
                PayRecord payRecord = new PayRecord();
                payRecord.setMainOrderId(orderId);
                payRecord.setPayDate(new Date());
                //微信支付账号不保存
                payRecord.setPayId("");
                payRecord.setPayStatus(OrderStatic.PAY_STATUS_PAYED);
                payRecord.setPayType(OrderStatic.PAY_TYPE_WEIXIN);
                payRecord.setTransactionId(transactionId);
                payRecord.setUserId(user.getUserId());
                payRecord.setTotalFee(totalFee);
                payRecordService.addPayRecord(payRecord);
                logger.info("保存支付记录成功");

                //3,清空购物车
                shoppingCartService.cleanShoppingCarts(user.getUserId(), mainOrder.getMerchantId());
                logger.info("清空购物车成功");

                //4, 通知商户  改为通知服务人员
                /*List<ChannelInfo> listChannelInfo = channelInfoService.getOnlineWorkChannelInfoByUserId(mainOrder.getMerchantId());
                if( mainOrder != null && mainOrder.getOrderNo() != null){
					channelInfoService.pushMessageToBatch4Merchant(listChannelInfo, title, descriptionWithOrderNo.replaceAll("[1]", mainOrder.getOrderNo()));
				}else{
					channelInfoService.pushMessageToBatch4Merchant(listChannelInfo, title, description);
				}*/

                //需要服务人员接单
                if (mainOrder.getDeliverType() != null && mainOrder.getDeliverType() == OrderStatic.DELIVER_TYPE_SEND) {
                    channelInfoService.pushNoticeToStationServiceProvider(mainOrder.getStationId(),
                            mainOrder.getServiceTypeId(), "新订单", "有新订单[" + mainOrder.getOrderNo() + "]等待接单，点击查看详情");
                }

                //发送收货码
                SendMSG.sendCheckCode(mainOrder.getCustomerPhoneNo(), mainOrder.getReceiveCode());

                //5,增加商品销量
                Integer sailsInMonth = 0;

                logger.info("增加商品销量");
                List<SubOrder> listSubOrder = subOrderService.findSubOrdersByMainOrderId(orderId);
                logger.info("listSubOrder.size():" + listSubOrder.size());
                if (0 < listSubOrder.size()) {
                    for (SubOrder subOrder : listSubOrder) {
                        sailsInMonth = sailsInMonth + subOrder.getProductCount();

                        Product product = productDao.findOne(subOrder.getProductId());
                        if (null != product) {
                            logger.info("原销量:" + product.getSales());
                            if (null == product.getSales()) {
                                product.setSales(subOrder.getProductCount());
                            } else {
                                product.setSales(product.getSales() + subOrder.getProductCount());
                            }
                            logger.info("现销量:" + product.getSales());
                            productDao.save(product);
                        }
                    }
                }

                //6，添加商户销量
                logger.info("sailsInMonth:" + sailsInMonth);
                Merchant merchant = merchantDao.findOne(mainOrder.getMerchantId());
                if (null != merchant) {
                    if (null == merchant.getSailsInMonth()) {
                        merchant.setSailsInMonth(sailsInMonth);
                    } else {
                        merchant.setSailsInMonth(merchant.getSailsInMonth() + sailsInMonth);
                    }
                    merchantDao.save(merchant);
                }

                //7,将订单放入定时任务的map中
                //TimedTaskSchedule.waitAcceptOrders.put(order.getOrderId(), order.getCreateDate());

                //8，添加订单状态记录
                logger.info("添加订单状态记录!orderId:" + orderId);
                orderStatusRecordService.addRecordNoException(orderId, OrderStatic.MAINORDER_STATUS_ACCEPT, null);

                res.getWriter().write(weixinNotifyResult);
                return;
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

    }

    public String getParameter(String notifyResult, String parameter) {
        String start = "<".concat(parameter).concat("><![CDATA[");
        String end = "]]></".concat(parameter).concat(">");
        return notifyResult.substring(notifyResult.indexOf(start) + start.length(), notifyResult.indexOf(end));
    }

    public static String getTotalFee(String notifyResult) {
        String start = "<total_fee>";
        String end = "</total_fee>";
        return notifyResult.substring(notifyResult.indexOf(start) + start.length(), notifyResult.indexOf(end));
    }

    public static void main(String args[]) {
        String notifyResult = "<xml><appid><![CDATA[wxe5976fada5d432e3]]></appid><attach><![CDATA[8a9d4f085acc3b20015acc3cec4f0001]]></attach><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[2]]></cash_fee><device_info><![CDATA[WEB]]></device_info><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1365949902]]></mch_id><nonce_str><![CDATA[0.8246138420702113]]></nonce_str><openid><![CDATA[oo-KAs_n8AL0PSWW4UzaMvagYGjk]]></openid><out_trade_no><![CDATA[20170314175349118]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[1C0DDEBF1642D5166CE05F01FE2F6EDF]]></sign><time_end><![CDATA[20170314175358]]></time_end><total_fee>2</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4004322001201703143361044806]]></transaction_id></xml>";
        String totalFee = getTotalFee(notifyResult);
        System.out.println(totalFee);
    }
}
