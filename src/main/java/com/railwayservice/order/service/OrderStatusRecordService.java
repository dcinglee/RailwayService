package com.railwayservice.order.service;

import com.railwayservice.order.entity.OrderStatusRecord;

import java.util.List;

/**
 * 订单状态记录服务接口
 *
 * @author Administrator
 */
public interface OrderStatusRecordService {
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

    /**
     * 增加订单记录
     *
     * @param orderId
     * @param orderStatus
     * @return
     */
    OrderStatusRecord addOrderStatusRecord(String orderId, Integer orderStatus, String remark);

    /**
     * 增加订单记录不抛出异常
     * 适用于对订单状态记录要求不高的场合
     */
    OrderStatusRecord addRecordNoException(String orderId, Integer orderStatus, String remark);
}
