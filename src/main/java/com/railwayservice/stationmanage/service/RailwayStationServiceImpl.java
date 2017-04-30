package com.railwayservice.stationmanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.TimeUtil;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.service.CommonService;
import com.railwayservice.stationmanage.dao.LineStationDao;
import com.railwayservice.stationmanage.dao.RailwayStationDao;
import com.railwayservice.stationmanage.entity.LineStation;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.entity.StationForImageRela;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

/**
 * @author lidx
 * @date 2017/2/9
 * @describe 车站服务类接口实现
 */
@Service
public class RailwayStationServiceImpl implements RailwayStationService {

    private final Logger logger = LoggerFactory.getLogger(RailwayStationServiceImpl.class);

    private RailwayStationDao railwayStationDao;

    private LineStationDao lineStationDao;

    private CommonService commonService;

    private StationForImageRelaService stationForImageRelaService;

    @Autowired
    public void setStationForImageRelaService(StationForImageRelaService stationForImageRelaService) {
        this.stationForImageRelaService = stationForImageRelaService;
    }

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Autowired
    public void setLineStationDao(LineStationDao lineStationDao) {
        this.lineStationDao = lineStationDao;
    }

    @Autowired
    public void setRailwayStationDao(RailwayStationDao railwayStationDao) {
        this.railwayStationDao = railwayStationDao;
    }

    /**
     * 校验站点的合法性。
     *
     * @param railwayStation 站点。
     */
    private void validateStation(RailwayStation railwayStation) {
        if (railwayStation == null)
            throw new AppException("车站对象不能为空！");
        if (!StringUtils.hasText(railwayStation.getStationName()))
            throw new AppException("站点名称不能为空！");
        if (!StringUtils.hasText(railwayStation.getStationNameAbbr()))
            throw new AppException("站点名称缩写不能为空！");
        if (!StringUtils.hasText(railwayStation.getProvince()))
            throw new AppException("站点所在省份不能为空！");
        if (!StringUtils.hasText(railwayStation.getCity()))
            throw new AppException("站点所在城市不能为空！");
        if (!StringUtils.hasText(railwayStation.getCity()))
            throw new AppException("站点状态不能为空！");
    }

    @Override
    public PageData queryRailwayStationPage(PageParam param, String stationName, Integer status) {
        return railwayStationDao.queryRailwayStationPage(param, stationName, status);
    }

    @Override
    @Transactional
    public RailwayStation addStation(RailwayStation station, String imageId) {
        // 校验传入参数合法性。
        validateStation(station);
        logger.info("车站服务层->车站添加->车站名称：" + station.getStationName());

        // 防止与已有的对象重复。
        if (railwayStationDao.countByStationName(station.getStationName()) > 0)
            throw new AppException("此站点已存在！");
//        ImageInfo imageInfo = commonService.getImageInfoById(imageId);

        // 缺省属性给默认值。
        station.setCreateDate(new Date());
//        station.setImgUrl(imageInfo.getUrl());

        // 保存实体对象。
        station = railwayStationDao.save(station);

        // 保存车站、示意图的关联。
        StationForImageRela stationForImageRela = new StationForImageRela();
        stationForImageRela.setStationId(station.getStationId());
        stationForImageRela.setImageId(imageId);
        stationForImageRelaService.addStationForImageRela(stationForImageRela);

        return station;
    }

    @Override
    @Transactional
    public RailwayStation updateStation(RailwayStation station, String imageId) {
        // 校验传入参数合法性。
        validateStation(station);
        logger.info("车站服务层->车站更新->车站名称：" + station.getStationName());

        if (!StringUtils.hasText(station.getStationId()))
            throw new AppException("请选择要更新的车站！");

        // 车站是否存在。
        RailwayStation railwayStation = railwayStationDao.findOne(station.getStationId());
        if (railwayStation == null)
            throw new AppException("该车站不存在或已删除！");

        // 更新需要修改的字段。
        railwayStation.setStationName(station.getStationName());
        railwayStation.setStationNameAbbr(station.getStationNameAbbr());
        railwayStation.setProvince(station.getProvince());
        railwayStation.setCity(station.getCity());
        railwayStation.setCityId(station.getCityId());
        railwayStation.setStatus(station.getStatus());
        railwayStation.setLatitude(station.getLatitude());
        railwayStation.setLongitude(station.getLongitude());

        // 保存已存在的实体对象。
        railwayStation = railwayStationDao.save(railwayStation);

        //解除关联
        stationForImageRelaService.deleteStationForImageRelaOfStation(railwayStation.getStationId());

        StationForImageRela stationForImageRela = new StationForImageRela();

        stationForImageRela.setImageId(imageId);
        stationForImageRela.setStationId(railwayStation.getStationId());

        stationForImageRelaService.addStationForImageRela(stationForImageRela);

        return railwayStation;
    }

    @Override
    @Transactional
    public void deleteStation(String stationId) {
        if (!StringUtils.hasText(stationId))
            throw new AppException("请选择要删除的车站！");
        logger.info("车站服务层->车站删除->车站ID：" + stationId);

        railwayStationDao.delete(stationId);
    }

    @Override
    @Transactional
    public void deleteStationInBatch(List<String> stationIds) {
        if (stationIds == null)
            throw new AppException("请选择要删除的车站！");
        logger.info("车站服务层->车站删除");

        List<RailwayStation> stations = new ArrayList<>(stationIds.size());
        for (String stationId : stationIds) {
            stations.add(new RailwayStation(stationId));
        }
        railwayStationDao.deleteInBatch(stations);
    }

    @Override
    public List<RailwayStation> queryAllStations() {
        logger.info("车站服务层->查询所有车站");

        List<RailwayStation> station = railwayStationDao.findAll();
        return station;
    }

    /**
     * 查询所有车站信息
     *
     * @return RailwayStation
     */
    @Override
    public List<RailwayStation> queryAllStationsByUser(Admin admin) {
        if (admin == null) {
            throw new AppException("请登陆，再执行此查询");
        }
        logger.info("车站服务层->查询所有车站: 所属车站管理员：" + admin.getName());

        if (admin.getBelongId() == null || "".equals(admin.getBelongId().trim())) {
            return queryAllStations();
        } else {
            RailwayStation station = railwayStationDao.findByStationId(admin.getBelongId());
            List<RailwayStation> stations = new ArrayList<RailwayStation>();
            stations.add(station);
            return stations;
        }
    }

    @Override
    public RailwayStation findRailwayStation(Double longitude, Double latitude) {
        if ((null == longitude) || (null == latitude))
            throw new AppException("定位异常！");
        return railwayStationDao.findRailwayStation(longitude, latitude);
    }

    @Override
    public RailwayStation findByStationId(String stationId) {
        if (null == stationId) {
            throw new AppException("未指定要查的车站！");
        }

        return railwayStationDao.findByStationId(stationId);
    }

    @Override
    public RailwayStation findByStationName(String stationName) {
        if (!StringUtils.hasText("stationName")) {
            throw new AppException("未指定要查的车站名称！");
        }
        return railwayStationDao.findByStationName(stationName);
    }

    @Override
    public List<LineStation> getListStationInfo(String stationName, String type) {
        if (!StringUtils.hasText(stationName)) {
            logger.info("未指定要查的车站名称！");
            throw new AppException("未指定要查的车站名称！");
        }
        logger.info("getListStationInfoVo!stationName:" + stationName);

        Specification<LineStation> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(stationName)) {
                predicate = builder.and(predicate, builder.like(root.get("station"), "%" + stationName + "%"));
            }

            if (StringUtils.hasText(type)) {
            	if("D".equals(type)){
            		//只找动车
            		predicate = builder.and(predicate, builder.like(root.get("lineNo"), "%D%"));
            	}else if("G".equals(type)){
            		//只找高铁
            		predicate = builder.and(predicate, builder.like(root.get("lineNo"), "%G%"));
            	}
            }
            query.orderBy(builder.asc(root.get("departTime")));
            return predicate;
        };

       //查找途径当前站点的所有车次
        List<LineStation> listLineStation = lineStationDao.findAll(specification);
        logger.info("listLineStation.size:"+listLineStation.size());

        //如果当前车站为终点站，则去除
        //如果到达时间和出发时间相同，则表示为终点站
        for (int index = listLineStation.size() - 1; index >= 0; index--) {
            LineStation lineStation = listLineStation.get(index);
            if (lineStation.getArriveTime().equals(lineStation.getDepartTime())) {
                listLineStation.remove(index);
                continue;
            }
            
            //去除出发时间早于当前时间的车次
            Date date = new Date();
            if(!TimeUtil.ifBeforeDepartTime(date, lineStation.getDepartTime())){
            	listLineStation.remove(index);
                continue;
            }
            
        }
        return listLineStation;
    }

    @Override
    public List<RailwayStation> getOnLineStation() {
        return railwayStationDao.findOnLineStation();
    }

    @Override
    @Transactional
    public RailwayStation updateImage(String stationId, InputStream inputStream) {
        // 校验传入参数合法性。
        if (!StringUtils.hasText(stationId))
            throw new AppException("请选择要修改的车站！");
        logger.info("进入服务层：给车站添加图片: 车站ID：" + stationId);

        // 商户是否存在。
        RailwayStation railwayStationOld = railwayStationDao.findOne(stationId);
        if (railwayStationOld == null)
            throw new AppException("该车站不存在或已删除！");

        // 删除旧的图片。
        if (StringUtils.hasText(railwayStationOld.getImageId()))
            commonService.deleteImage(railwayStationOld.getImageId());

        // 保存图片信息。
        ImageInfo imageInfo = commonService.addImage(inputStream);

        // 更新商户的图片ID。
        railwayStationOld.setImageId(imageInfo.getImageId());
        return railwayStationDao.save(railwayStationOld);
    }

	@Override
	public List<RailwayStation> findAllSpeedRailWayStation() {
		logger.info("车站服务层->查询所有高铁站");

        List<RailwayStation> listStation = railwayStationDao.findAllSpeedRailWayStation();
        return listStation;
	}

	@Override
	public List<RailwayStation> findAllStation() {
		logger.info("车站服务层->查询所有火车站站以及高铁站");
		List<RailwayStation> railwayStation = railwayStationDao.findAll();
		return railwayStation;
	}
}
