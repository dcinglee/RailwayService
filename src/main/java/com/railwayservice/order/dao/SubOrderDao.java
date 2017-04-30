package com.railwayservice.order.dao;

import com.railwayservice.order.entity.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 订单子单数据库访问接口
 *
 * @author lid
 * @date 2017.2.10
 */
public interface SubOrderDao extends JpaRepository<SubOrder, String>, JpaSpecificationExecutor<SubOrder> {
    /**
     * 根据id查找对应的子订单
     *
     * @author lid
     * @date 2017.2.4
     */
    SubOrder findSubOrderBySubOrderId(String id);

    List<SubOrder> findBySubOrderId(String suborderId);
}
