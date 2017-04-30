package com.railwayservice.serviceprovider.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.messages.service.NoticeService;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.service.MainOrderService;
import com.railwayservice.order.service.OrderStatic;
import com.railwayservice.order.service.OrderStatusRecordService;
import com.railwayservice.serviceprovider.dao.DeliverDao;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.vo.CountOrdersVO;
import com.railwayservice.serviceprovider.vo.DeliverOrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 餐饮服务类。
 *
 * @author Ewing
 */
@Service
public class DeliverServiceImpl implements DeliverService {
    private final Logger logger = LoggerFactory.getLogger(DeliverServiceImpl.class);

    private DeliverDao deliverDao;
    private MainOrderService mainOrderService;
    private OrderStatusRecordService orderStatusRecordService;
    private NoticeService noticeService;

    @Autowired
    public void setDeliverDao(DeliverDao deliverDao) {
        this.deliverDao = deliverDao;
    }

    @Autowired
    public void setMainOrderService(MainOrderService mainOrderService) {
        this.mainOrderService = mainOrderService;
    }

    @Autowired
    public void setOrderStatusRecordService(OrderStatusRecordService orderStatusRecordService) {
        this.orderStatusRecordService = orderStatusRecordService;
    }

    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * 查询需要送货的订单。
     */
    @Override
    public PageData queryOrders(PageParam pageParam, String serviceProviderId, String stationId, Integer orderStatus) {
        if (orderStatus == null || !StringUtils.hasText(stationId) || !StringUtils.hasText(serviceProviderId))
            throw new AppException("服务员ID、车站ID和订单状态不能为空！");
        logger.info("配送服务：查询订单：服务员ID：" + serviceProviderId + " 车站ID：" + stationId + "，订单状态：" + orderStatus);

        // 如果是查询商家已接单的订单，则查询全站的订单，否则只查询自己的订单。
        if (orderStatus == OrderStatic.MAINORDER_STATUS_ACCEPT) {
            return deliverDao.queryAcceptOrders(pageParam, serviceProviderId, stationId, orderStatus);
        } else if (orderStatus == OrderStatic.MAINORDER_STATUS_SERVICER_GET_GOOD) {
            // 待送达的订单包含配送员已取货和等待用户取货
            return deliverDao.queryMyOrders(pageParam, serviceProviderId, stationId,
                    OrderStatic.MAINORDER_STATUS_SERVICER_GET_GOOD, OrderStatic.MAINORDER_STATUS_WAIT_USER_GET);
        } else {
            return deliverDao.queryMyOrders(pageParam, serviceProviderId, stationId, orderStatus);
        }
    }

    /**
     * 配送员接收订单。
     */
    @Override
    @Transactional
    public void acceptOrder(ServiceProvider serviceProvider, String orderId) {
        if (serviceProvider == null || !StringUtils.hasText(serviceProvider.getServiceProviderId()) || !StringUtils.hasText(orderId))
            throw new AppException("服务员ID或订单ID为空！");
        logger.info("配送服务：接受订单：订单ID：" + orderId);

        // 判断订单是否符合条件。
        MainOrder mainOrder = mainOrderService.findMainOrderByOrderId(orderId);
        if (mainOrder == null)
            throw new AppException("订单不存在或已被删除！");
        if (!serviceProvider.getStationId().equals(mainOrder.getStationId()))
            throw new AppException("您不可以接别的车站的订单哦。");
        if (mainOrder.getOrderStatus() == OrderStatic.MAINORDER_STATUS_SERVICER_ACCEPT)
            throw new AppException("该订单已被其他人员抢单了哦。");
        if (mainOrder.getOrderStatus() != OrderStatic.MAINORDER_STATUS_ACCEPT)
            throw new AppException("该订单已被处理了，请刷新当前页。");

        // 更新订单状态和订单的服务人员信息。
        mainOrder.setUpdateDate(new Date());
        mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_SERVICER_ACCEPT);
        mainOrder.setServiceProviderId(serviceProvider.getServiceProviderId());
        mainOrderService.updateMainOrder(mainOrder);

        // 记录订单状态变化
        orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(),
                OrderStatic.MAINORDER_STATUS_SERVICER_ACCEPT, null);
        noticeService.noticeToUserNoException(mainOrder);
    }

    /**
     * 配送员已取货。
     */
    @Override
    @Transactional
    public void getOrderGoods(ServiceProvider serviceProvider, String orderId) {
        if (serviceProvider == null || !StringUtils.hasText(serviceProvider.getServiceProviderId()) || !StringUtils.hasText(orderId))
            throw new AppException("服务员ID或订单ID为空！");
        logger.info("配送服务：服务员已取货：订单ID：" + orderId);

        // 判断订单是否符合条件。
        MainOrder mainOrder = mainOrderService.findMainOrderByOrderId(orderId);
        if (mainOrder == null)
            throw new AppException("订单不存在或已被删除！");
        if (!serviceProvider.getServiceProviderId().equals(mainOrder.getServiceProviderId()))
            throw new AppException("您不可以拿别人的订单的商品哦。");
        if (mainOrder.getOrderStatus() == OrderStatic.MAINORDER_STATUS_SERVICER_GET_GOOD)
            throw new AppException("该订单已被其他人员取货了哦。");
        if (mainOrder.getOrderStatus() != OrderStatic.MAINORDER_STATUS_SERVICER_ACCEPT)
            throw new AppException("该订单已被处理了，请刷新当前页。");

        // 更新订单状态信息。
        mainOrder.setUpdateDate(new Date());
        mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_SERVICER_GET_GOOD);
        mainOrderService.updateMainOrder(mainOrder);

        // 记录订单状态变化
        orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(),
                OrderStatic.MAINORDER_STATUS_SERVICER_GET_GOOD, null);
        noticeService.noticeToUserNoException(mainOrder);
    }

    /**
     * 服务员通知用户取货。
     */
    @Override
    public void noticeUserGet(ServiceProvider serviceProvider, String orderId) {
        if (serviceProvider == null || !StringUtils.hasText(serviceProvider.getServiceProviderId()) || !StringUtils.hasText(orderId))
            throw new AppException("服务员ID或订单ID为空！");
        logger.info("配送服务：服务员已取货：订单ID：" + orderId);

        // 判断订单是否符合条件。
        MainOrder mainOrder = mainOrderService.findMainOrderByOrderId(orderId);
        if (mainOrder == null)
            throw new AppException("订单不存在或已被删除！");
        if (!serviceProvider.getServiceProviderId().equals(mainOrder.getServiceProviderId()))
            throw new AppException("该订单不是你的哦。");
        if (mainOrder.getOrderStatus() == OrderStatic.MAINORDER_STATUS_WAIT_USER_GET)
            throw new AppException("该订单已经通知过用户取货了哦。");
        if (mainOrder.getOrderStatus() != OrderStatic.MAINORDER_STATUS_SERVICER_GET_GOOD)
            throw new AppException("该订单已被处理了，请刷新当前页。");

        // 更新订单状态信息。
        mainOrder.setUpdateDate(new Date());
        mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_WAIT_USER_GET);
        mainOrderService.updateMainOrder(mainOrder);

        // 记录订单状态变化
        orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(),
                OrderStatic.MAINORDER_STATUS_WAIT_USER_GET, null);
        noticeService.noticeToUserNoException(mainOrder);
        noticeService.smsToUserGetGoods(mainOrder);
    }

    /**
     * 配送员已将商品送达。
     */
    @Override
    @Transactional
    public void deliveredGoods(ServiceProvider serviceProvider, String orderId) {
        if (serviceProvider == null || !StringUtils.hasText(serviceProvider.getServiceProviderId()) || !StringUtils.hasText(orderId))
            throw new AppException("服务员ID或订单ID为空！");
        logger.info("配送服务：商品已送达客户：订单ID：" + orderId);

        // 判断订单是否符合条件。
        MainOrder mainOrder = mainOrderService.findMainOrderByOrderId(orderId);
        if (mainOrder == null)
            throw new AppException("订单不存在或已被删除！");
        if (!serviceProvider.getServiceProviderId().equals(mainOrder.getServiceProviderId()))
            throw new AppException("您不可以确认送达别人的订单哦。");
        if (mainOrder.getOrderStatus() == OrderStatic.MAINORDER_STATUS_COMPLETED)
            throw new AppException("该订单已经确认送达或已完成了哦。");
        if (mainOrder.getOrderStatus() != OrderStatic.MAINORDER_STATUS_SERVICER_GET_GOOD
                && mainOrder.getOrderStatus() != OrderStatic.MAINORDER_STATUS_WAIT_USER_GET)
            throw new AppException("该订单已被处理了，请刷新当前页。");

        // 更新订单状态信息。
        mainOrder.setUpdateDate(new Date());
        mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_COMPLETED);
        mainOrderService.updateMainOrder(mainOrder);

        // 记录订单状态变化
        orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(),
                OrderStatic.MAINORDER_STATUS_COMPLETED, null);
        noticeService.noticeToUserNoException(mainOrder);
    }

    /**
     * 统计今天处理的订单情况。
     */
    @Override
    public CountOrdersVO countOrdersToday(String serviceProviderId) {
        //今天零点零分零秒的毫秒数 一天是86400000毫秒
        long zero = System.currentTimeMillis() / 86400000L * 86400000L
                - TimeZone.getDefault().getRawOffset();
        Date todayStart = new Date(zero);
        return deliverDao.countOrdersTimeGreaterEquals(serviceProviderId, todayStart);
    }

    /**
     * 查询今天已完成的订单。
     */
    @Override
    public List<DeliverOrderVO> queryCompleteOrdersToday(ServiceProvider serviceProvider) {
        if (serviceProvider == null || !StringUtils.hasText(serviceProvider.getStationId()))
            throw new AppException("当前服务人员无效！");
        //今天零点零分零秒的毫秒数 一天是86400000毫秒
        long zero = System.currentTimeMillis() / 86400000L * 86400000L
                - TimeZone.getDefault().getRawOffset();
        Date beginTime = new Date(zero);
        return deliverDao.queryCompleteOrdersToday(serviceProvider.getServiceProviderId(), beginTime);
    }

    /**
     * 查询今天被取消的订单。
     */
    @Override
    public List<DeliverOrderVO> queryCancelOrdersToday(ServiceProvider serviceProvider) {
        if (serviceProvider == null || !StringUtils.hasText(serviceProvider.getStationId()))
            throw new AppException("当前服务人员无效！");
        //今天零点零分零秒的毫秒数 一天是86400000毫秒
        long zero = System.currentTimeMillis() / 86400000L * 86400000L
                - TimeZone.getDefault().getRawOffset();
        Date beginTime = new Date(zero);
        return deliverDao.queryCancelOrdersToday(serviceProvider.getServiceProviderId(), beginTime);
    }
}
