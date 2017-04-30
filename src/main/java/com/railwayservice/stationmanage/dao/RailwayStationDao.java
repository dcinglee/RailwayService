package com.railwayservice.stationmanage.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.vo.StationStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author lidx
 * @date 2017/2/9
 * @describe 车站数据库访问接口
 */
public interface RailwayStationDao extends JpaRepository<RailwayStation, String>, JpaSpecificationExecutor<RailwayStation> {

    /**
     * 根据车站简称进行排序
     *
     * @param pageable
     * @return
     */
    Page<RailwayStation> findAllByOrderByStationNameAbbr(Pageable pageable);

    /**
     * 根据站点名称查询车站分页信息
     *
     * @param stationName 站点名称
     * @return 成功分页的车站对象
     */
    PageData queryRailwayStationPage(PageParam param, String stationName, Integer status);

    /**
     * 根据站点id查找车站对象
     *
     * @param stationId
     * @return RailwayStation
     */
    RailwayStation findByStationId(String stationId);

//    @Query("SELECT rs.city, rs.province, rs.stationName, rs.stationNameAbbr, rs.createDate, rs.latitude, rs.longitude, rs.status, rs.spell, rs.cityId, ii.url as imgUrl FROM RailwayStation rs LEFT JOIN StationForImageRela si ON rs.stationId = si.stationId LEFT JOIN ImageInfo ii ON si.imageId = ii.imageId WHERE rs.stationId = :stationId")
//    RailwayStation findStation(@Param("stationId") String stationId);

    /**
     * 根据站点名称查询车站对象
     *
     * @param stationName 站点名称
     * @return 车站对象
     */
    RailwayStation findByStationName(String stationName);

    /**
     * 根据站点名称统计
     *
     * @param stationName
     * @return 统计总数
     */
    long countByStationName(String stationName);

    /**
     * 根据日期分组降序查询第一条记录
     *
     * @return 成功查询的车站对象
     */
    RailwayStation findFirstByOrderByCreateDateDesc();

    /**
     * 通过经纬度查询车站信息
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return RailwayStation
     */
    @Query(nativeQuery = true, value = "SELECT stationId, city, province, stationName, stationNameAbbr, " +
            "createDate, latitude, longitude, status, cityId, spell, (POWER(MOD(ABS(longitude - :longitude),360),2) + " +
            "POWER(ABS(latitude - :latitude),2)) AS distance FROM RailwayStation where status = 17001 ORDER BY distance LIMIT 1 ")
    RailwayStation findRailwayStation(@Param("longitude") Double longitude, @Param("latitude") Double latitude);

    @Query(nativeQuery = true, value = "SELECT * from RailwayStation where status = " + StationStatic.RAILWAYSTATION_STATUS_ON_SERVICE)
    List<RailwayStation> findOnLineStation();
    
    /**
     * 查找所有高铁站
     * @author lid
     */
    @Query(nativeQuery = true, value = "select * from RailwayStation where stationName in (select station from LineStation where type='高铁' )")
    List<RailwayStation> findAllSpeedRailWayStation();
    
   
}
