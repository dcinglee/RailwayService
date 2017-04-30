package com.railwayservice.messages.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.messages.dao.MessageDao;
import com.railwayservice.messages.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;

/**
 * 消息服务类。
 *
 * @author Ewing
 */
@Service
public class MessageServiceImpl implements com.railwayservice.messages.service.MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    private MessageDao messageDao;

    @Autowired
    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    /**
     * 规范化新的消息对象，给出默认值。
     */
    private void standardizeMessage(Message message) {
        if (message.getCreateTime() == null)
            message.setCreateTime(new Date());
    }

    @Override
    @Transactional
    public Message addMessage(Message message) {
        logger.info("消息服务层：新增消息：消息内容："+message.getContent());
        standardizeMessage(message);
        return messageDao.save(message);
    }

    @Override
    @Transactional
    public Message updateMessage(Message message) {
        logger.info("消息服务层：更新消息：消息内容："+message.getContent());
        standardizeMessage(message);
        Message oldMgr = messageDao.findOne(message.getMessageId());
        if (oldMgr == null)
            throw new AppException("该消息不存在或已删除！");
        return messageDao.save(oldMgr);
    }

    @Override
    public Page<Message> queryMessages(String name, Pageable pageable) {
        logger.info("消息服务层：查询消息：");
        // 函数式接口（使用Lambda表达式）
        Specification<Message> specification = (root, query, builder) -> {
            // 创建一个且(and)条件（述语）
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            return predicate;
        };
        return messageDao.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public void deleteMessage(String messageId) {
        messageDao.delete(messageId);
    }

}
