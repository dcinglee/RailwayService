package com.railwayservice.order.dao;

import com.railwayservice.order.entity.OrderStatusRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderStatusRecordDao extends JpaRepository<OrderStatusRecord, String>, JpaSpecificationExecutor<OrderStatusRecord> {
    /**
     * 根据订单id查找订单的所有状态信息
     *
     * @param orderId
     * @return
     * @author lid
     * @date 2017.3.1
     */
    List<OrderStatusRecord> findByOrderId(String orderId);

    /**
     * 根据记录id查找状态记录
     *
     * @param orderStatusRecordId
     * @return
     */
    OrderStatusRecord findByOrderStatusRecordId(String orderStatusRecordId);
}
