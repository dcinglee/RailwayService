package com.railwayservice.messages.service;

import com.railwayservice.messages.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 消息服务类。
 *
 * @author Ewing
 */
public interface MessageService {

    Message addMessage(Message message);

    Message updateMessage(Message message);

    Page<Message> queryMessages(String name, Pageable pageable);

    void deleteMessage(String messageId);

}
