package com.railwayservice.stationmanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.stationmanage.dao.LineStationDao;
import com.railwayservice.stationmanage.entity.LineStation;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.vo.DestinationStationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lid
 * @date 2017/2/16
 * @describe 高铁线路信息服务接口实现
 */
@Service
public class LineStationServiceImpl implements LineStationService {
    private final Logger logger = LoggerFactory.getLogger(LineStationServiceImpl.class);

    private LineStationDao lineStationDao;

    private RailwayStationService railwayStationService;

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    @Autowired
    public void setLineStationDao(LineStationDao lineStationDao) {
        this.lineStationDao = lineStationDao;
    }

    @Override
    public List<LineStation> getLineStationBylineNo(String lineNo) {
        if (null == lineNo) {
            throw new AppException("未指定要查的车次！");
        }
        logger.info("车次服务层->车次列表->车次：" + lineNo);

        Specification<LineStation> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(lineNo)) {
                predicate = builder.and(predicate, builder.like(root.get("lineNo"), "%" + lineNo + "%"));
            }
            query.orderBy(builder.asc(root.get("sortNo")));
            return predicate;
        };
        return lineStationDao.findAll(specification);
    }

    @Override
    public List<DestinationStationVo> getDestinationByStationAndNo(String lineNo, String stationName) {
        if ((!StringUtils.hasText(lineNo))
                || (!StringUtils.hasText(stationName))) {
            throw new AppException("未指定要查的车次或者站点名称！");
        }
        logger.info("getDestinationByStationAndNo!lineNo:" + lineNo + ",stationName:" + stationName);

        List<LineStation> listLineStation = lineStationDao.findByLineNo(lineNo);
        List<DestinationStationVo> listDestination = new ArrayList<DestinationStationVo>();
        if (0 == listLineStation.size()) {
            return null;
        }

        LineStation station = null;

        for (LineStation lineStation : listLineStation) {
            if (lineStation.getStation().equals(stationName)) {
                station = lineStation;
                break;
            }
        }

        logger.info("listLineStation.size():" + listLineStation.size());

        for (LineStation lineStation : listLineStation) {
            if (lineStation.getSortNo() > station.getSortNo()) {

                if (StringUtils.hasText(lineStation.getStation())) {
                    logger.info("lineStation.getStation():" + lineStation.getStation());
                }

                RailwayStation railwayStation = railwayStationService.findByStationName(lineStation.getStation());

                DestinationStationVo vo = new DestinationStationVo();
                vo.setStation(lineStation.getStation());
                vo.setArriveTime(lineStation.getArriveTime());

                if (null == railwayStation) {
                    listDestination.add(vo);
                    continue;
                }

                if (StringUtils.hasText(railwayStation.getCityId())) {
                    vo.setCityId(railwayStation.getCityId());
                }
                vo.setSpell(railwayStation.getSpell());
                vo.setStationNameAbbr(railwayStation.getStationNameAbbr());
                vo.setStationId(railwayStation.getStationId());
                logger.info("null == CityId");
                listDestination.add(vo);
            }
        }
        return listDestination;
    }

    @Override
    public LineStation findByStationAndLineNo(String station, String lineNo) {
        if (!StringUtils.hasText(station)) {
            throw new AppException("站点名称为空！");
        }

        if (!StringUtils.hasText(lineNo)) {
            throw new AppException("车次信息为空！");
        }
        logger.info("findByStationAndLineNo!station:" + station + ",lineNo:" + lineNo);

        LineStation lineStation = lineStationDao.findByStationAndLineNo(station, lineNo);
        if (null == lineStation) {
            logger.info("null == lineStation");
        }
        return lineStation;
    }

}
