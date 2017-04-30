package com.railwayservice.merchantmanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.common.service.TimedTaskSchedule;
import com.railwayservice.merchantmanage.dao.MerchantDao;
import com.railwayservice.merchantmanage.dao.MerchantmenDao;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.merchantmanage.vo.MerchantAchievement;
import com.railwayservice.merchantmanage.vo.MerchantMainOrder;
import com.railwayservice.merchantmanage.vo.Merchantmen;
import com.railwayservice.messages.service.ChannelInfoService;
import com.railwayservice.messages.service.NoticeService;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.entity.SubOrder;
import com.railwayservice.order.service.MainOrderService;
import com.railwayservice.order.service.OrderStatic;
import com.railwayservice.order.service.OrderStatusRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理员服务类。
 *
 * @author Ewing
 */
@Service
public class MerchantmenServiceImpl implements MerchantmenService {
    private final Logger logger = LoggerFactory.getLogger(MerchantmenServiceImpl.class);

    private MerchantDao merchantDao;
    private MerchantmenDao merchantmenDao;
    private MainOrderService mainOrderService;
    private OrderStatusRecordService orderStatusRecordService;
    private ChannelInfoService channelInfoService;
    private NoticeService noticeService;

    @Autowired
    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    @Autowired
    public void setChannelInfoService(ChannelInfoService channelInfoService) {
        this.channelInfoService = channelInfoService;
    }

    @Autowired
    public void setMerchantmenDao(MerchantmenDao merchantmenDao) {
        this.merchantmenDao = merchantmenDao;
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
     * 把冗余的列表数据转换成层级关系的树型结构。
     */
    private List<MerchantMainOrder> convertMainOrdersTree(List<MerchantMainOrder> mainOrdersFrom) {
        // 把列表冗余的结构封装成树形结构，主订单->多个子订单。
        List<MerchantMainOrder> mainOrdersNeed = new ArrayList<>();
        for (MerchantMainOrder merchantMainOrder : mainOrdersFrom) {

            // 如果有子订单信息，取出子订单的属性。
            SubOrder subOrder = null;
            if (merchantMainOrder.getSubOrderId() != null) {
                subOrder = new SubOrder();
                subOrder.setMainOrderId(merchantMainOrder.getMainOrderId());
                subOrder.setSubOrderId(merchantMainOrder.getSubOrderId());
                subOrder.setProductId(merchantMainOrder.getProductId());
                subOrder.setProductName(merchantMainOrder.getProductName());
                subOrder.setProductCount(merchantMainOrder.getProductCount());
                subOrder.setProductPrice(merchantMainOrder.getProductPrice());
            }

            // 注意：使用indexOf时元素必须重写hashCode和equals方法！详见MerchantMainOrder类。
            int index = mainOrdersNeed.indexOf(merchantMainOrder);
            if (index >= 0) { // 如果主订单已存在，且子订单存在，直接添加子订单。
                if (subOrder != null)
                    mainOrdersNeed.get(index).getSubOrders().add(subOrder);
            } else { // 否则先添加主订单，再添加子订单集合。
                List<SubOrder> subOrders = new ArrayList<>();
                if (subOrder != null)
                    subOrders.add(subOrder);
                merchantMainOrder.setSubOrders(subOrders);
                mainOrdersNeed.add(merchantMainOrder);
            }
        }
        return mainOrdersNeed;
    }

    @Override
    public List<MerchantMainOrder> queryWaitDealOrders(Merchant merchant) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息为空！");
        logger.info("商户人员业务实现：查询待处理的订单：商户名称：" + merchant.getName());

        // 一次性查询出所有订单信息包括子订单，减少数据库和网络访问次数。
        List<MerchantMainOrder> mainOrdersFrom = merchantmenDao.queryWaitDealOrders(merchant.getMerchantId());

        // 把冗余的列表结构封装成树形结构，主订单->多个子订单。
        return convertMainOrdersTree(mainOrdersFrom);
    }

    @Override
    public List<MerchantMainOrder> queryWaitUserOrders(Merchant merchant) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息为空！");
        logger.info("商户人员业务实现：查询待取货的订单:商户名称：" + merchant.getName());

        // 一次性查询出所有订单信息包括子订单，减少数据库和网络访问次数。
        List<MerchantMainOrder> mainOrdersFrom = merchantmenDao.queryWaitUserOrders(merchant.getMerchantId());

        // 把冗余的列表结构封装成树形结构，主订单->多个子订单。
        return convertMainOrdersTree(mainOrdersFrom);
    }

    /**
     * 处理订单：确认接单、拒绝订单、顾客已到店取货、同意取消订单、拒绝取消订单。
     */
    @Override
    @Transactional
    public void dealOrderByStatus(Merchant merchant, String orderId, Integer newStatus, String reason) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息为空！");
        if (newStatus == null)
            throw new AppException("新的订单状态为空！");
        logger.info("商户人员业务实现：根据订单状态处理订单：订单ID：" + orderId + " 新的状态：" + newStatus + " 处理原因：" + reason);

        MainOrder mainOrder = mainOrderService.findMainOrderByOrderId(orderId);
        if (!merchant.getMerchantId().equals(mainOrder.getMerchantId()))
            throw new AppException("您不可以动别人家的订单哦。");

        Integer oldStatus = mainOrder.getOrderStatus();
        // 处理操作类型，尽量使用单层条件。
        if (oldStatus == OrderStatic.MAINORDER_STATUS_WAIT_ACCEPT
                && newStatus == OrderStatic.MAINORDER_STATUS_ACCEPT) {
            // 商家接单了：等待接单>商家已接单。
            mainOrder.setOrderStatus(newStatus);
            mainOrder.setUpdateDate(new Date());
            mainOrderService.updateMainOrder(mainOrder);

            TimedTaskSchedule.waitAcceptOrders.remove(mainOrder.getOrderId());

            //需要服务人员接单
            if (mainOrder.getDeliverType() != null && mainOrder.getDeliverType() == OrderStatic.DELIVER_TYPE_SEND) {
                channelInfoService.pushNoticeToStationServiceProvider(mainOrder.getStationId(),
                        mainOrder.getServiceTypeId(), "新订单", "有新订单[" + mainOrder.getOrderNo() + "]等待接单，点击查看详情");
            }
            // 记录状态并通知用户
            orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(), newStatus, null);
            noticeService.noticeToUserNoException(mainOrder);
        } else if (oldStatus == OrderStatic.MAINORDER_STATUS_WAIT_ACCEPT
                && newStatus == OrderStatic.MAINORDER_STATUS_REJECT) {
            // 商家拒绝接单：等待接单>拒绝接单。
            // 退款给用户。
            mainOrderService.refundOrder(mainOrder);
            // 更新订单状态。
            mainOrder.setOrderStatus(newStatus);
            mainOrder.setUpdateDate(new Date());
            mainOrder.setRefuseReason(reason);
            mainOrderService.updateMainOrder(mainOrder);

            // 记录状态并通知用户
            orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(), newStatus, reason);
            noticeService.noticeToUserNoException(mainOrder);
        } else if ((oldStatus == OrderStatic.MAINORDER_STATUS_ACCEPT
                || oldStatus == OrderStatic.MAINORDER_STATUS_WAIT_USER_GET)
                && newStatus == OrderStatic.MAINORDER_STATUS_COMPLETED) {
            // 顾客已到店取货：商家已接单或通知用户取货>订单完成。
            // TODO 还要转钱给商家呢
            mainOrder.setOrderStatus(newStatus);
            mainOrder.setUpdateDate(new Date());
            mainOrderService.updateMainOrder(mainOrder);

            // 记录状态并通知用户
            orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(), newStatus, null);
            noticeService.noticeToUserNoException(mainOrder);
        } else if (mainOrder.getOrderCancelStatus() == OrderStatic.MAINORDER_CANCEL_STATUS_APPEAL
                && newStatus == OrderStatic.MAINORDER_CANCEL_STATUS_AGREE) {
            // 商家同意取消订单：申请取消订单>商家同意取消。
            // 退款给用户。
            mainOrderService.refundOrder(mainOrder);
            // 更新订单状态。
            mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_CANCELED);
            mainOrder.setUpdateDate(new Date());
            mainOrder.setOrderCancelStatus(newStatus);
            mainOrderService.updateMainOrder(mainOrder);

            //需要服务人员接单,发送消息给服务人员，通知有订单被取消
            if (mainOrder.getDeliverType() != null && mainOrder.getDeliverType() == OrderStatic.DELIVER_TYPE_SEND) {
                //已有配送人员抢单
                if (mainOrder.getServiceProviderId() != null && !"".equals(mainOrder.getServiceProviderId())) {
                    channelInfoService.pushNoticeToServiceProvider(mainOrder.getServiceProviderId(),
                            "订单取消", "有订单[" + mainOrder.getOrderNo() + "]被取消，点击查看详情");
                } else {
                    //没有人抢单
                    channelInfoService.pushNoticeToStationServiceProvider(mainOrder.getStationId(),
                            mainOrder.getServiceTypeId(), "订单取消", "有订单[" + mainOrder.getOrderNo() + "]被取消，点击查看详情");
                }
            }

            // 记录状态并通知用户
            orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(), newStatus, null);
            orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(), OrderStatic.MAINORDER_STATUS_CANCELED, null);
            noticeService.noticeToUserNoException(mainOrder);
        } else if (mainOrder.getOrderCancelStatus() == OrderStatic.MAINORDER_CANCEL_STATUS_APPEAL
                && newStatus == OrderStatic.MAINORDER_CANCEL_STATUS_REFUSED) {
            // 商家拒绝取消订单：申请取消订单>商家拒绝取消。
            // 订单按原流程继续。
            mainOrder.setUpdateDate(new Date());
            mainOrder.setOrderCancelStatus(newStatus);
            mainOrder.setRefuseReason(reason);
            mainOrderService.updateMainOrder(mainOrder);

            // 记录状态并通知用户
            orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(), newStatus, reason);
            noticeService.noticeToUserNoException(mainOrder);
        } else {
            throw new AppException("当前订单状态不支持该操作！");
        }
    }

    /**
     * 商家查询历史的订单。
     */
    @Override
    public List<MerchantMainOrder> queryHistoryOrders(Merchant merchant, Date startDate, Date endDate, Integer orderStatus) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息为空！");
        logger.info("商户人员业务实现：查询历史的订单：商户ID：" + merchant.getMerchantId() + " 订单状态：" + orderStatus);

        // 一次性查询出所有订单信息包括子订单，减少数据库和网络访问次数。
        List<MerchantMainOrder> mainOrdersFrom;
        // 根据需求，查询时将商家已接单和配送人员已接单状态合并，且只查配送的。
        if (orderStatus != null && (orderStatus == OrderStatic.MAINORDER_STATUS_ACCEPT
                || orderStatus == OrderStatic.MAINORDER_STATUS_SERVICER_ACCEPT)) {
            mainOrdersFrom = merchantmenDao.queryHistoryOrders(merchant.getMerchantId(), startDate, endDate, OrderStatic.DELIVER_TYPE_SEND,
                    OrderStatic.MAINORDER_STATUS_ACCEPT, OrderStatic.MAINORDER_STATUS_SERVICER_ACCEPT);
        } else {
            mainOrdersFrom = merchantmenDao.queryHistoryOrders(merchant.getMerchantId(), startDate, endDate, null, orderStatus);
        }

        // 把列表冗余的结构封装成树形结构，主订单->多个子订单。
        return convertMainOrdersTree(mainOrdersFrom);
    }

    @Override
    @Transactional
    public void updateMerchantStatus(Merchant merchant, Integer status) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()) || status == null)
            throw new AppException("商户或新的状态为空！");
        logger.info("商户人员业务实现：更新商户状态:商户名称：" + merchant.getName());

        merchantDao.updateMerchantStatus(merchant.getMerchantId(), status);
    }

    /**
     * 商户统计自己的营业额。
     */
    @Override
    public MerchantAchievement achievement(PageParam pageParam, Merchant merchant) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息为空！");
        logger.info("商户人员业务实现：商户统计自己的营业额：商户名称：" + merchant.getName());

        // 查询总营业额
        MerchantAchievement achievement = merchantmenDao.queryTotalAchievement(merchant.getMerchantId());
        PageData pageData = merchantmenDao.queryDailyAchievement(pageParam, merchant.getMerchantId());

        // 查询日常营业额
        achievement.setDailyAchievement(pageData);
        return achievement;
    }

    @Override
    public Merchantmen getMerchantInfo(Merchant merchant) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户身份验证失败！");
        logger.info("商户人员业务实现：商户查询自己的资料信息：" + merchant.getMerchantId());

        return merchantmenDao.getMerchantInfo(merchant.getMerchantId());
    }

    @Override
    @Transactional
    public void updateMerchant(Merchant merchantmen, Merchant merchant) {
        if (merchantmen == null || !StringUtils.hasText(merchantmen.getMerchantId()))
            throw new AppException("商户身份验证失败！");
        if (merchant == null)
            throw new AppException("商户新的信息为空！");
        logger.info("商户人员业务实现：商户更新自己的信息：" + merchantmen.getMerchantId());

        Merchant merchantOld = merchantDao.findOne(merchantmen.getMerchantId());

        // 修改名称、地址、电话、公告
        if (StringUtils.hasText(merchant.getName()))
            merchantOld.setName(merchant.getName());
        if (StringUtils.hasText(merchant.getAddress()))
            merchantOld.setAddress(merchant.getAddress());
        if (StringUtils.hasText(merchant.getPhoneNo()))
            merchantOld.setPhoneNo(merchant.getPhoneNo());
        if (StringUtils.hasText(merchant.getAnnouncement()))
            merchantOld.setAnnouncement(merchant.getAnnouncement());
        // 营业时间
        if (merchant.getStartTime() != null)
            merchantOld.setStartTime(merchant.getStartTime());
        if (merchant.getStopTime() != null)
            merchantOld.setStopTime(merchant.getStopTime());

        merchantDao.save(merchantOld);
    }

}
