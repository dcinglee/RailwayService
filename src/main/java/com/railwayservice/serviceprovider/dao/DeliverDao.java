package com.railwayservice.serviceprovider.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.serviceprovider.vo.CountOrdersVO;
import com.railwayservice.serviceprovider.vo.DeliverOrderVO;

import java.util.Date;
import java.util.List;

/**
 * 送货数据访问接口。
 *
 * @author Ewing
 * @date 2017/3/3
 */
public interface DeliverDao {
    /**
     * 查询商户已接单的订单。
     *
     * @param pageParam         分页参数。
     * @param serviceProviderId
     * @param stationId         服务人员车站ID。
     * @param orderStatus       订单状态。   @return 订单数据。
     */
    PageData queryAcceptOrders(PageParam pageParam, String serviceProviderId, String stationId, Integer orderStatus);

    /**
     * 统计时间大于传入时间的订单。
     *
     * @param startTime 开始时间。
     * @return 统计结果。
     */
    CountOrdersVO countOrdersTimeGreaterEquals(String serviceProviderId, Date startTime);

    /**
     * 查询自己的订单。
     *
     * @param pageParam         分页参数。
     * @param serviceProviderId
     * @param stationId         服务人员车站ID。
     * @param orderStatus       订单状态。
     * @return 订单数据。
     */
    PageData queryMyOrders(PageParam pageParam, String serviceProviderId, String stationId, Integer... orderStatus);

    /**
     * 查询今天已完成的订单。
     */
    List<DeliverOrderVO> queryCompleteOrdersToday(String serviceProviderId, Date beginTime);

    /**
     * 查询今天被取消的订单。
     */
    List<DeliverOrderVO> queryCancelOrdersToday(String serviceProviderId, Date beginTime);
}
