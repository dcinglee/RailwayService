package com.railwayservice.messages.service;

import com.railwayservice.messages.entity.Notice;
import com.railwayservice.order.entity.MainOrder;

import java.util.List;

/**
 * 消息服务接口
 *
 * @author lid
 * @date 2017.3.9
 */
public interface NoticeService {
    /**
     * 添加消息
     *
     * @return
     */
    Notice addNotice(Notice notice);

    /**
     * 查询用户消息
     */
    List<Notice> getNoticeByReceiverId(String receiverId);

    /**
     * 删除消息
     *
     * @param receiverId
     * @return
     * @author lid
     */
    void deleteNoticeByReceiverId(String receiverId);

    /**
     * 触发一条通知，不抛出异常。
     */
    void noticeToUserNoException(MainOrder mainOrder);

    /**
     * 短信（SMS）通知用户取货。
     *
     * @param mainOrder
     */
    void smsToUserGetGoods(MainOrder mainOrder);
}
