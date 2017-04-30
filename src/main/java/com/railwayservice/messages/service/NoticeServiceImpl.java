package com.railwayservice.messages.service;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.JSonUtils;
import com.railwayservice.application.util.SendMSG;
import com.railwayservice.messages.dao.NoticeDao;
import com.railwayservice.messages.entity.Notice;
import com.railwayservice.messages.vo.OrderNotice;
import com.railwayservice.order.entity.MainOrder;
import com.railwayserviceWX.config.WeixinConfig;
import com.railwayserviceWX.controller.WechatAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知服务类。
 *
 * @author Ewing
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    private final Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);

    private NoticeDao noticeDao;

    @Autowired
    public void setNoticeDao(NoticeDao noticeDao) {
        this.noticeDao = noticeDao;
    }

    @Override
    @Transactional
    public Notice addNotice(Notice notice) {
        if (notice.getStatus() == null)
            notice.setStatus(MessagesStatic.NOTICE_STATUS_NOT_DEAL);
        logger.info("通知服务层：新增通知：通知内容：" + notice.getContent());
        notice.setCreateTime(new Date());
        return noticeDao.save(notice);
    }

    @Override
    public List<Notice> getNoticeByReceiverId(String receiverId) {
        if (!StringUtils.hasText(receiverId)) {
            throw new AppException("通知接收人的ID为空！");
        }
        logger.info("通知服务层：查询通知列表：接收人ID：" + receiverId);
        return noticeDao.getNoticeByReceiverId(receiverId);
    }

    @Override
    @Transactional
    public void deleteNoticeByReceiverId(String receiverId) {
        if (!StringUtils.hasText(receiverId))
            throw new AppException("请提供通知接收人的ID！");
        logger.info("通知服务层：删除通知：接收人ID：" + receiverId);
        List<Notice> listNotice = noticeDao.getNoticeByReceiverId(receiverId);
        if (0 == listNotice.size()) {
            return;
        }
        noticeDao.deleteInBatch(listNotice);
    }

    /**
     * 触发一条通知，不抛出异常。
     */
    @Override
    @Transactional
    public void noticeToUserNoException(MainOrder mainOrder) {
        logger.info("商户人员业务实现：商户触发订单变动通知：订单ID：" + (mainOrder == null ? "" : mainOrder.getOrderId()));
        try {
            // 保存通知到用户消息中
            OrderNotice orderNotice = noticeDao.getNoticeOrderInfo(mainOrder.getOrderId());
            orderNotice.setCreateTime(new Date());
            Notice notice = new Notice();
            String jsonNotice = JSonUtils.toJSon(orderNotice);
            logger.info("保存用户的消息通知：" + jsonNotice);
            notice.setContent(jsonNotice);
            notice.setReceiverId(mainOrder.getUserId());
            notice.setType(MessagesStatic.NOTICE_TYPE_MAIN_ORDER);
            this.addNotice(notice);

            // 发送通知给用户的微信
            Map<String, Object> body = new HashMap<>();
            body.put("touser", orderNotice.getOpenid());
            body.put("template_id", WeixinConfig.MESSAGE_TEMPLATE_MERCHANT_ACCEPT);
            body.put("url", WeixinConfig.HOST + "/wechat/orderDetail.html?orderId=" + mainOrder.getOrderId() + "&userId=" + mainOrder.getUserId());
            body.put("topcolor", "#FF6600");

            Map<String, Object> first = new HashMap<>();
            first.put("value", orderNotice.getOrderStatusName());
            first.put("color", "#FF6600");

            Map<String, Object> keyword1 = new HashMap<>();
            keyword1.put("value", orderNotice.getMerchantName());
            keyword1.put("color", "#FF6600");

            Map<String, Object> keyword2 = new HashMap<>();
            keyword2.put("value", new SimpleDateFormat(AppConfig.DATE_TIME_FORMAT).format(new Date()));
            keyword2.put("color", "#FF6600");

            Map<String, Object> remark = new HashMap<>();
            remark.put("value", "您购买的 " + orderNotice.getProductDetail() + " 的订单状态已更新。");
            remark.put("color", "#FF6600");

            Map<String, Object> data = new HashMap<>();
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("remark", remark);

            body.put("data", data);

            // 发送通知给微信用户
            String jsonData = JSonUtils.toJSon(body);
            logger.info("发送通知给用户的微信：数据：" + jsonData);
            WechatAccessor.sendMessageToUser(jsonData);

        } catch (Exception e) {
            logger.error("商户触发订单变动通知异常：订单ID：" + (mainOrder == null ? "null" : mainOrder.getOrderId()), e);
        }
    }

    /**
     * 短信（SMS）通知用户取货。
     */
    @Override
    public void smsToUserGetGoods(MainOrder mainOrder) {
        SendMSG.sendGoodsDelivery(mainOrder.getCustomerPhoneNo(),mainOrder.getDeliverAddress());
    }

}
