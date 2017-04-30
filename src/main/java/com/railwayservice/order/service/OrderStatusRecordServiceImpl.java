package com.railwayservice.order.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.order.dao.OrderStatusRecordDao;
import com.railwayservice.order.entity.OrderStatusRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

/**
 * 订单状态记录服务接口
 */
@Service
public class OrderStatusRecordServiceImpl implements OrderStatusRecordService {

    private final Logger logger = LoggerFactory.getLogger(OrderStatusRecordServiceImpl.class);

    private OrderStatusRecordDao orderStatusRecordDao;

    @Autowired
    public void setOrderStatusRecordDao(OrderStatusRecordDao orderStatusRecordDao) {
        this.orderStatusRecordDao = orderStatusRecordDao;
    }

    @Override
    public List<OrderStatusRecord> findByOrderId(String orderId) {
        logger.info("订单状态记录服务层：查询订单：订单ID：" + orderId);
        Specification<OrderStatusRecord> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(orderId)) {
                predicate = builder.and(predicate, builder.like(root.get("orderId"), "%" + orderId + "%"));
            }
            query.orderBy(builder.desc(root.get("createDate")));
            return predicate;
        };
        List<OrderStatusRecord> listOrderStatusRecord = orderStatusRecordDao.findAll(specification);
        return listOrderStatusRecord;
    }

    @Override
    public OrderStatusRecord findByOrderStatusRecordId(String orderStatusRecordId) {
        logger.info("订单状态记录服务层：查询订单状态记录：订单状态记录ID：" + orderStatusRecordId);
        if (!StringUtils.hasText(orderStatusRecordId)) {
            throw new AppException("id参数为空");
        }
        return orderStatusRecordDao.findByOrderStatusRecordId(orderStatusRecordId);
    }

    @Override
    @Transactional
    public OrderStatusRecord addOrderStatusRecord(String orderId, Integer orderStatus, String remark) {
        logger.info("添加订单状态记录:" + orderId + ",orderStatus:" + orderStatus);
        if (!StringUtils.hasText(orderId)) {
            throw new AppException("orderId参数为空");
        }

        if (null == orderStatus) {
            throw new AppException("orderStatus参数为空");
        }
        OrderStatusRecord record = new OrderStatusRecord();
        record.setCreateDate(new Date());
        record.setOrderId(orderId);
        record.setOrderStatus(orderStatus);
        record.setRemark(remark);
        return orderStatusRecordDao.save(record);
    }

    @Override
    @Transactional
    public OrderStatusRecord addRecordNoException(String orderId, Integer orderStatus, String remark) {
        logger.info("订单状态记录服务层：添加订单状态记录不抛出异常：订单ID：" + orderId);
        try {
            return this.addOrderStatusRecord(orderId, orderStatus, remark);
        } catch (Exception e) {
            logger.error("添加订单状态记录异常：", e);
            return null;
        }
    }
}
