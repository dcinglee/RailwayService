package com.railwayservice.stationmanage.dao;

import com.railwayservice.stationmanage.entity.LineStation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author lid
 * @date 2017/2/16
 * @describe 高铁线路数据库访问接口
 */
public interface LineStationDao extends JpaRepository<LineStation, String>, JpaSpecificationExecutor<LineStation> {

    @Query("select ls from LineStation ls" +
            " where ls.lineNo = :lineNo")
    LineStation findLineStation(@Param("lineNo") String lineNo);
    
    List<LineStation> findByStation(String stationName);
    
    List<LineStation> findByLineNo(String lineNo);
    
    LineStation findByStationAndLineNo(String station, String lineNo);
}
