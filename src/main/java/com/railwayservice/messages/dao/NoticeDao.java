package com.railwayservice.messages.dao;

import com.railwayservice.messages.entity.Notice;
import com.railwayservice.messages.vo.OrderNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 通知数据库访问接口。
 * 该接口使用了Spring Data JPA提供的方法。
 *
 * @author Ewing
 */
public interface NoticeDao extends JpaRepository<Notice, String>, JpaSpecificationExecutor<Notice> {
    /**
     * 查询用户消息
     */
    List<Notice> getNoticeByReceiverId(String receiverId);

    /**
     * 查询要通知的内容。
     */
    OrderNotice getNoticeOrderInfo(String orderId);
}
