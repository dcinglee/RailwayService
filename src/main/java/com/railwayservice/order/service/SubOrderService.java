package com.railwayservice.order.service;

import com.railwayservice.order.entity.SubOrder;

import java.util.List;

/**
 * 订单子单服务接口
 *
 * @author lid
 * @date 2017.2.10
 */
public interface SubOrderService {

    /**
     * 根据id查找子单
     *
     * @param id
     * @return SubOrder
     * @author lid
     * @date 2017.2.10
     */
    SubOrder findSubOrderBySubOrderId(String id);

    /**
     * 根据订单号查找所有的子单
     *
     * @param mainOrderId
     * @return List
     * @author lid
     * @date 2017.2.10
     */
    List<SubOrder> findSubOrdersByMainOrderId(String mainOrderId);

    /**
     * 添加子单
     *
     * @param subOrder
     * @return SubOrder
     * @author lid
     * @date 2017.2.10
     */
    SubOrder addSubOrder(SubOrder subOrder);

    /**
     * 修改子单信息，目前只能修改备注：remark，
     *
     * @param subOrder
     * @return SubOrder
     * @author lid
     * @date 2017.2.10
     */
    SubOrder updateSubOrder(SubOrder subOrder);

    /**
     * 删除子订单信息
     *
     * @param subOrder
     * @author lid
     * @date 2017.2.10
     */
    void deleteSubOrder(String subOrderId);

    /**
     * 取消子订单
     *
     * @param SubOrder
     * @return SubOrder
     * @author lid
     * @date 2017.2.14
     */
    SubOrder cancelSubOrder(String subOrderId);
}
