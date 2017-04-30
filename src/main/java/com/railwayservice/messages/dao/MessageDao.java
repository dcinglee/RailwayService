package com.railwayservice.messages.dao;

import com.railwayservice.messages.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 消息数据库访问接口。
 * 该接口使用了Spring Data JPA提供的方法。
 *
 * @author Ewing
 */
public interface MessageDao extends JpaRepository<Message, String>, JpaSpecificationExecutor<Message> {

}
