package com.railwayservice.stationmanage.dao;

import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.stationmanage.entity.StationForImageRela;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

/**
 * 权限DAO实现类。
 *
 * @author Ewing
 * @date 2017/2/22
 */
public class StationForImageRelaDaoImpl extends BaseDaoImpl {
    private final Logger logger = LoggerFactory.getLogger(StationForImageRelaDaoImpl.class);

//    /**
//     * @param param
//     * @param
//     * @param
//     * @return
//     */
//    public PageData queryRailwayStationPage(PageParam param, String stationName, Integer status) {
//        logger.info("queryAuthorityPage");
//        List<Object> params = new ArrayList<>();
//
//        StringBuilder sqlBuilder = new StringBuilder("SELECT rs.stationId, rs.city, rs.province, rs.stationName, rs.stationNameAbbr, rs.createDate, rs.latitude, rs.longitude, rs.status, rs.spell, rs.cityId, ii.url as imgUrl, ii.imageId as imageId FROM RailwayStation rs LEFT JOIN StationForImageRela si ON rs.stationId = si.stationId LEFT JOIN ImageInfo ii ON si.imageId = ii.imageId WHERE 1=1");
//        // 处理参数，动态生成SQL。
//        if (StringUtils.hasText(stationName)) {
//            sqlBuilder.append(" and rs.stationName like ?");
//            params.add("%" + stationName + "%");
//        }
//        if (status != null) {
//            sqlBuilder.append(" and rs.status = ?");
//            params.add(status);
//        }
//        return this.findPageObject(param, sqlBuilder.toString(), RailwayStation.class, params.toArray());
//    }


    public List<String> findByStationId(String stationId) {
        StringBuilder sql = new StringBuilder("SELECT ii.url FROM StationForImageRela si LEFT JOIN ImageInfo ii ON si.imageId = ii.imageId WHERE si.stationId = ?");
        List<String> railwayStationList = this.getOperations().queryForList(sql.toString(),String.class,stationId);

        return railwayStationList;
    }

}
