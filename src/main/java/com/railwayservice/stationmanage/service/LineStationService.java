package com.railwayservice.stationmanage.service;

import com.railwayservice.stationmanage.entity.LineStation;
import com.railwayservice.stationmanage.vo.DestinationStationVo;

import java.util.List;

/**
 * @author lid
 * @date 2017/2/16
 * @describe 高铁线路信息服务接口
 */
public interface LineStationService {
    /**
     * 根据车次查找该车次的线路信息，如：起始车站，中途停靠站点，停靠时间，出发时间以及到达时间
     *
     * @param lineNo
     * @return List
     * @author lid
     * @date 2017.2.16
     */
    List<LineStation> getLineStationBylineNo(String lineNo);
    
    /**
     * 根据始发站和车次找到可以到达的目的车站
     * @param lineNo
     * @param stationName
     * @return
     * @author lid
     * @date 2017.3.11
     */
    List<DestinationStationVo> getDestinationByStationAndNo(String lineNo, String stationName);
    
    /**
     * 根据始发站和车次查找停靠记录
     * @param station
     * @param lineNo
     * @return
     * @author lid
     * @date 2017.3.13
     */
    LineStation findByStationAndLineNo(String station, String lineNo);
}
