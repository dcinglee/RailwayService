package com.railwayservice.stationmanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.common.service.CommonService;
import com.railwayservice.stationmanage.dao.StationForImageRelaDao;
import com.railwayservice.stationmanage.entity.StationForImageRela;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author lidx
 * @date 2017/4/6
 * @describe 车站示意图关联服务接口实现类
 */
@Service
public class StationForImageRelaServiceImpl implements StationForImageRelaService {

    private final Logger logger = LoggerFactory.getLogger(StationForImageRelaServiceImpl.class);

    private StationForImageRelaDao stationForImageRelaDao;

    private CommonService commonService;

    @Autowired
    public void setStationForImageRelaDao(StationForImageRelaDao stationForImageRelaDao) {
        this.stationForImageRelaDao = stationForImageRelaDao;
    }

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public List<String> findByStationId(String stationId) {
        if (!StringUtils.hasText(stationId)) {
            throw new AppException("车站ID不能为空！");
        }
        logger.info("车站示意图关联记录服务层：查询车站示意图关联记录服务：车站示意图关联ID：" + stationId);
        return stationForImageRelaDao.findByStationId(stationId);
    }

    @Override
    public StationForImageRela findByRelaId(String relaId) {
        if (!StringUtils.hasText(relaId)) {
            throw new AppException("车站示意图关联ID不能为空！");
        }
        logger.info("车站示意图关联记录服务层：查询车站示意图关联记录服务：车站示意图关联ID：" + relaId);
        return stationForImageRelaDao.findByRelaId(relaId);
    }

    @Override
    public StationForImageRela findByImageId(String imageId) {
        return stationForImageRelaDao.findByImageId(imageId);
    }

    @Override
    @Transactional
    public StationForImageRela addStationForImageRela(StationForImageRela stationForImageRela) {
        if (null == stationForImageRela) {
            throw new AppException("车站示意图关联对象不能为空！");
        }
        logger.info("车站示意图关联记录服务层：添加车站示意图关联记录服务：车站示意图关联ID：" + stationForImageRela.getRelaId());
        //添加默认值
        stationForImageRela.setCreateDate(new Date());

        //保存实体对象
        return stationForImageRelaDao.save(stationForImageRela);
    }

    @Override
    @Transactional
    public Integer deleteStationForImageRelaOfStation(String stationId) {
        if (!StringUtils.hasText(stationId)) {
            throw new AppException("车站ID不能为空！");
        }
        logger.info("车站示意图关联记录服务层：解除车站示意图关联：车站ID：" + stationId);
        return stationForImageRelaDao.deleteStationForImageRelaOfStation(stationId);
    }

//    @Override
//    public List<StationForImageRela> findByStationId(String stationId) {
//        if (null == stationId) {
//            throw new AppException("未指定要查的车站！");
//        }
//
//        return stationForImageRelaDao.findByStationId(stationId);
//    }

}
