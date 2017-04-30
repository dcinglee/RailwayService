package com.railwayservice.stationmanage.service;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.stationmanage.entity.LineStation;
import com.railwayservice.stationmanage.entity.RailwayStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.util.List;

/**
 * @author lidx
 * @date 2017/2/9
 * @describe 车站服务接口
 */
public interface RailwayStationService {

    /**
     * 根据站点id查找车站对象
     *
     * @param stationId
     * @return RailwayStation
     */
    RailwayStation findByStationId(String stationId);

    /**
     * 根据站点名称查找车站信息
     *
     * @param stationName
     * @return RailwayStation
     * @author lid
     * @date 2017.3.11
     */
    RailwayStation findByStationName(String stationName);

    /**
     * 查询所有车站。
     *
     * @param stationName
     * @return 分页数据。
     */
    PageData queryRailwayStationPage(PageParam param, String stationName, Integer status);

    /**
     * 校验并添加单个车站。
     *
     * @param station 车站对象。
     * @return 添加成功的车站。
     */
    RailwayStation addStation(RailwayStation station, String imageId);

    /**
     * 校验并更新单个车站。
     *
     * @param station 车站对象。
     * @return 更新成功的商户。
     */
    RailwayStation updateStation(RailwayStation station, String imageId);

    /**
     * 删除单个站车站。
     *
     * @param stationId 车站对象。
     */
    void deleteStation(String stationId);

    /**
     * 删除多个站车站.
     *
     * @param station 车站对象
     */
    void deleteStationInBatch(List<String> station);

    /**
     * 查询所有车站信息
     *
     * @return RailwayStation
     */
    List<RailwayStation> queryAllStations();

    /**
     * 查询所有车站信息
     *
     * @return RailwayStation
     */
    List<RailwayStation> queryAllStationsByUser(Admin admi);

    /**
     * 通过经纬度查询车站信息
     *
     * @param longitude
     * @param latitude
     * @return
     */
    RailwayStation findRailwayStation(Double longitude, Double latitude);

    /**
     * 查询当前站点所有车次，以及该车次在该站点的发车时间
     *
     * @param stationName
     * @return
     * @author lid
     * @date 2017.3.11
     */
    List<LineStation> getListStationInfo(String stationName, String type);

    /**
     * 查询所有提供服务的车站
     *
     * @return List<RailwayStation>
     * @author lid
     * @date 2017.3.20
     */
    List<RailwayStation> getOnLineStation();

    RailwayStation updateImage(String stationId, InputStream inputStream);
    
    /**
     * 查找所有高铁站
     * @author lid
     */
    List<RailwayStation> findAllSpeedRailWayStation();
    
    /**
     * 查询所有的车站，并筛选其中的属性
     * @return
     */
    List<RailwayStation> findAllStation();
}
