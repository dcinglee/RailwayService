package com.railwayservice.stationmanage.service;


import com.railwayservice.stationmanage.entity.StationForImageRela;

import java.io.InputStream;
import java.util.List;

/**
 * @author lidx
 * @date 2017/4/6
 * @describe 车站示意图关联服务接口
 */
public interface StationForImageRelaService {

    /**
     * 根据车站ID查询车站示意图
     *
     * @param stationId 车站ID
     * @return
     */
    List<String> findByStationId(String stationId);

    /**
     * 根据车站示意图关联ID查询车站示意图关联对象
     *
     * @param relaId 车站示意图关联ID
     * @return 车站示意图关联对象
     */
    StationForImageRela findByRelaId(String relaId);


    StationForImageRela findByImageId(String imageId);
    /**
     * 添加车站示意图关联
     *
     * @param stationForImageRela
     * @return
     */
    StationForImageRela addStationForImageRela(StationForImageRela stationForImageRela);

    /**
     *
     * @param stationId
     * @return
     */
    Integer deleteStationForImageRelaOfStation(String stationId);

}
