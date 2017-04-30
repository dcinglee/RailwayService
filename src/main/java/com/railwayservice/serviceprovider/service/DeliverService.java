package com.railwayservice.serviceprovider.service;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.vo.CountOrdersVO;
import com.railwayservice.serviceprovider.vo.DeliverOrderVO;

import java.util.List;

/**
 * 餐饮服务类。
 *
 * @author Ewing
 */
public interface DeliverService {

    /**
     * 查询需要送货的订单。
     *
     * @param pageParam         分页参数。
     * @param serviceProviderId
     * @param stationId         服务人员车站ID。
     * @param orderStatus       订单状态。   @return 订单数据。
     */
    PageData queryOrders(PageParam pageParam, String serviceProviderId, String stationId, Integer orderStatus);

    /**
     * 配送员接收订单。
     *
     * @param serviceProvider 配送员。
     * @param orderId         订单ID
     */
    void acceptOrder(ServiceProvider serviceProvider, String orderId);

    /**
     * 配送员已取货。
     *
     * @param serviceProvider 配送员。
     * @param orderId         订单ID。
     */
    void getOrderGoods(ServiceProvider serviceProvider, String orderId);

    /**
     * 配送员已将商品送达。
     *
     * @param serviceProvider 配送员。
     * @param orderId         订单ID。
     */
    void deliveredGoods(ServiceProvider serviceProvider, String orderId);

    /**
     * 统计今天处理的订单情况。
     *
     * @param serviceProviderId 配送员ID。
     * @return 统计数据。
     */
    CountOrdersVO countOrdersToday(String serviceProviderId);

    /**
     * 查询今天已完成的订单。
     */
    List<DeliverOrderVO> queryCompleteOrdersToday(ServiceProvider serviceProvider);

    /**
     * 查询今天被取消的订单。
     */
    List<DeliverOrderVO> queryCancelOrdersToday(ServiceProvider serviceProvider);

    /**
     * 服务员通知用户取货。
     *
     * @param orderId 订单ID。
     */
    void noticeUserGet(ServiceProvider serviceProvider, String orderId);
}
