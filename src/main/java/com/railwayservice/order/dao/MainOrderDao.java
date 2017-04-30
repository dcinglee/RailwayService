package com.railwayservice.order.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.vo.OrderQuartzVo;
import com.railwayservice.order.vo.QueryOrderParam;
import com.railwayservice.order.vo.UserOrdersVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe 订单信息数据库访问接口，该接口使用了Spring Data JPA提供的方法
 */
public interface MainOrderDao extends JpaRepository<MainOrder, String>, JpaSpecificationExecutor<MainOrder> {

    /**
     * 查询并分页。
     */
    PageData queryMainOrderPage(PageParam pageParam, QueryOrderParam param);

    /**
     * 根据用户信息查询订单，获取vo类列表
     *
     * @param userId
     * @return List<UserOrdersVo>
     */
    List<UserOrdersVo> getOrdersByUser(String userId);
    
    /**
     * 根据交易单号查找订单
     * @param transactionId
     * @return
     */
    MainOrder findByTransactionId(String transactionId);
    
    /**
     * 根据订单状态查找订单
     * @param mainOrderStatus
     * @return List<MainOrder>
     * @author lid
     * @date 2017.3.31
     */
    List<OrderQuartzVo> queryMainOrderByStatus(Integer mainOrderStatus);

}
