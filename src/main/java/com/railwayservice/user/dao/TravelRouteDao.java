package com.railwayservice.user.dao;

import com.railwayservice.user.entity.TravelRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户行程数据库访问接口
 *
 * @author lid
 * @date 2017.2.16
 */
public interface TravelRouteDao extends JpaRepository<TravelRoute, String>, JpaSpecificationExecutor<TravelRoute> {
    /**
     * 根据routeId获取行程信息
     *
     * @param routeId
     * @return TravelRoute
     * @author lid
     * @date 2017.2.18
     */
    TravelRoute getTravelRouteByRouteId(String routeId);
}
