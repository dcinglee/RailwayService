package com.railwayservice.stationmanage.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.stationmanage.entity.RailwayStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限DAO实现类。
 *
 * @author Ewing
 * @date 2017/2/22
 */
public class RailwayStationDaoImpl extends BaseDaoImpl {
    private final Logger logger = LoggerFactory.getLogger(RailwayStationDaoImpl.class);

    /**
     * @param param
     * @param
     * @param
     * @return
     */
    public PageData queryRailwayStationPage(PageParam param, String stationName, Integer status) {
        logger.info("queryAuthorityPage");
        List<Object> params = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder("SELECT rs.stationId, rs.city, rs.province, rs.stationName, rs.stationNameAbbr, rs.createDate, rs.latitude, rs.longitude, rs.status, rs.spell, rs.cityId, ii.url as imgUrl, ii.imageId as imageId FROM RailwayStation rs LEFT JOIN StationForImageRela si ON rs.stationId = si.stationId LEFT JOIN ImageInfo ii ON si.imageId = ii.imageId WHERE 1=1");
        // 处理参数，动态生成SQL。
        if (StringUtils.hasText(stationName)) {
            sqlBuilder.append(" and rs.stationName like ?");
            params.add("%" + stationName + "%");
        }
        if (status != null) {
            sqlBuilder.append(" and rs.status = ?");
            params.add(status);
        }
        return this.findPageObject(param, sqlBuilder.toString(), RailwayStation.class, params.toArray());
    }


    public RailwayStation findByStationId(String stationId) {
        StringBuilder sql = new StringBuilder("SELECT rs.stationId, rs.city, rs.province, rs.stationName, rs.stationNameAbbr, rs.createDate, rs.latitude, rs.longitude, rs.status, rs.spell, rs.cityId, ii.url as imgUrl FROM RailwayStation rs LEFT JOIN StationForImageRela si ON rs.stationId = si.stationId LEFT JOIN ImageInfo ii ON si.imageId = ii.imageId WHERE rs.stationId = ?");
        return this.findOneObject(sql.toString(), RailwayStation.class, stationId);
    }

}
