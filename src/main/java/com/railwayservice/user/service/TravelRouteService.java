package com.railwayservice.user.service;

import com.railwayservice.user.entity.TravelRoute;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.vo.TravelRouteVo;

/**
 * 用户行程服务类
 *
 * @author lid
 * @date 2017.2.16
 */
public interface TravelRouteService {
    /**
     * 根据routeId删除行程
     *
     * @param routeId
     * @author lid
     * @date 2017.2.22
     */
    void deleteTravelRoute(String routeId);

    /**
     * 根据routeId获取行程信息
     *
     * @param routeId
     * @return TravelRoute
     * @author lid
     * @date 2017.2.18
     */
    TravelRoute getTravelRouteByRouteId(String routeId);

    /**
     * 修改travelRoute
     *
     * @param travelRoute
     * @return TravelRoute
     * @author lid
     * @date 2017.2.18
     */
    TravelRoute updateTravelRoute(TravelRoute travelRoute);

    /**
     * @param userId
     * @param customerName
     * @param customerPhone
     * @param lineNo
     * @param carriageNumber
     * @param seatNumber
     * @return TravelRoute
     * @author lid
     * @date 2017.2.20
     */
    TravelRoute addTravelRoute(User user, TravelRouteVo vo);

    /**
     * 根据userid获取行程列表
     *
     * @param userId
     * @return List<TravelRoute>
     * @author lid
     * @date 2017.2.18
     */
    TravelRoute getTravelRouteByUserId(String userId);
}
