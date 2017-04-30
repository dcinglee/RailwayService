package com.railwayservice.stationmanage.dao;

import com.railwayservice.authority.entity.Role;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.stationmanage.entity.StationForImageRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author lidx
 * @date 2017/4/6
 * @describe 车站示意图关联数据库访问接口
 */
public interface StationForImageRelaDao extends JpaRepository<StationForImageRela, String>, JpaSpecificationExecutor<StationForImageRela> {

    /**
     * 根据车站ID查询车站示意图关联对象
     *
     * @param stationId 车站ID
     * @return 车站示意图关联对象
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
     * 根据管理员ID删除管理员与角色的关联
     *
     * @param stationId
     * @return
     */
    @Modifying
    @Query("delete from StationForImageRela sfir where sfir.stationId=:stationId")
    Integer deleteStationForImageRelaOfStation(@Param("stationId") String stationId);


}
