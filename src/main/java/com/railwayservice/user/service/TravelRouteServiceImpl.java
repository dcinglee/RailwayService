package com.railwayservice.user.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.TimeUtil;
import com.railwayservice.stationmanage.entity.LineStation;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.service.LineStationService;
import com.railwayservice.stationmanage.service.RailwayStationService;
import com.railwayservice.stationmanage.vo.StationStatic;
import com.railwayservice.user.dao.TravelRouteDao;
import com.railwayservice.user.entity.TravelRoute;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.vo.TravelRouteVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用户行程服务类实现
 *
 * @author lid
 * @date 2017.2.16
 */
@Service
public class TravelRouteServiceImpl implements TravelRouteService {

    private final Logger logger = LoggerFactory.getLogger(TravelRouteServiceImpl.class);

    private TravelRouteDao travelRouteDao;

    private LineStationService lineStationService;

    private RailwayStationService railwayStationService;

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    @Autowired
    public void setLineStationService(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @Autowired
    public void setTravelRouteDao(TravelRouteDao travelRouteDao) {
        this.travelRouteDao = travelRouteDao;
    }

    @Override
    @Transactional
    public TravelRoute addTravelRoute(User user, TravelRouteVo vo) {
        logger.info("行程服务层->添加行程->车次:" + vo.getLineNo());

        TravelRoute travelRoute = new TravelRoute();

        if (null == user) {
            throw new AppException("该用户不存在或已删除！");
        }

        travelRoute.setUserId(user.getUserId());
        travelRoute.setCreateDate(new Date());

        //设置行程的主体信息，如果输入了姓名则保存，没有输入则默认为用户本人信息
        if (StringUtils.hasText(vo.getCustomerName())) {
            travelRoute.setCustomerName(vo.getCustomerName());
        } else {
            //如果已经获取真实姓名，则设置为真实姓名，否则设置为微信昵称
            if (StringUtils.hasText(user.getName())) {
                travelRoute.setCustomerName(user.getName());
            } else {
                travelRoute.setCustomerName(user.getNickName());
            }
        }

        //与上同理
        if (StringUtils.hasText(vo.getCustomerPhone())) {
            travelRoute.setCustomerPhone(vo.getCustomerPhone());
        } else {
            if (StringUtils.hasText(user.getPhoneNo())) {
                travelRoute.setCustomerPhone(user.getPhoneNo());
            }
        }

        //保存车厢和座位信息
        if (StringUtils.hasText(vo.getCarriageNumber())) {
            travelRoute.setCarriageNumber(vo.getCarriageNumber());
        }

        if (StringUtils.hasText(vo.getSeatNumber())) {
            travelRoute.setSeatNumber(vo.getSeatNumber());
        }

        //如果没有输入车次信息，则直接保存并返回
        if (!StringUtils.hasText(vo.getLineNo())) {
            return travelRouteDao.save(travelRoute);
        }
        travelRoute.setLineNo(vo.getLineNo());

        //获取当前车次的所有停靠信息
        List<LineStation> listLineStation = lineStationService.getLineStationBylineNo(vo.getLineNo());
        logger.info("listLineStation.size()" + listLineStation.size());

        //设置出发车站以及到达车站在本车次中的排序
        Integer startLineNo = 0;
        Integer arrivedLineNo = 100;
        
        //设置出发车站和出发时间
        if (StringUtils.hasText(vo.getAboardStation())) {
            travelRoute.setAboardStation(vo.getAboardStation());
            for (LineStation lineStation : listLineStation) {
                if (lineStation.getStation().equals(vo.getAboardStation())) {
                    String aboardTime = lineStation.getDepartTime();
                    startLineNo = lineStation.getSortNo();
                    travelRoute.setAboardTime(TimeUtil.getDateByString(aboardTime));
                    break;
                }
            }
        }
        logger.info("设置出发车站和出发时间完成");
        //设置到达车站和到达时间
        if (StringUtils.hasText(vo.getArrivedStation())) {
            RailwayStation arrivedStation = railwayStationService.findByStationName(vo.getArrivedStation());
            if (null != arrivedStation) {
                travelRoute.setArrivedStation(vo.getArrivedStation());
                if (StringUtils.hasText(arrivedStation.getCityId())) {
                    travelRoute.setCityId(arrivedStation.getCityId());
                }
                for (LineStation lineStation : listLineStation) {
                    if (lineStation.getStation().equals(vo.getArrivedStation())) {
                        String arrivedTime = lineStation.getArriveTime();
                        travelRoute.setArrivedTime(TimeUtil.getDateByString(arrivedTime));
                        arrivedLineNo = lineStation.getSortNo();
                        break;
                    }
                }
            }else{
            	/**
            	 * 如果到达车站为空，则不保存车站的cityId
            	 */
            	travelRoute.setArrivedStation(vo.getArrivedStation());
            	for (LineStation lineStation : listLineStation) {
                    if (lineStation.getStation().equals(vo.getArrivedStation())) {
                        String arrivedTime = lineStation.getArriveTime();
                        travelRoute.setArrivedTime(TimeUtil.getDateByString(arrivedTime));
                        arrivedLineNo = lineStation.getSortNo();
                        break;
                    }
                }
            }
        }
        logger.info("设置到达车站和到达时间完成");
        /**
         * 获取沿途站点
         */
        List<RailwayStation> listStationOnTheWay = new ArrayList<RailwayStation>();
        
        logger.info("startLineNo:"+startLineNo);
        logger.info("arrivedLineNo:"+arrivedLineNo);
        
        for (LineStation lineStation : listLineStation) {
            if ((StringUtils.hasText(lineStation.getStation()))
            		&& (lineStation.getSortNo() >= startLineNo)
            		&& (lineStation.getSortNo() <= arrivedLineNo)) {
                RailwayStation railwayStation = railwayStationService.findByStationName(lineStation.getStation());
                if (null != railwayStation) {
                    listStationOnTheWay.add(railwayStation);
                }
            }
        }
        travelRoute.setListStationOnTheWay(listStationOnTheWay);
        logger.info("获取沿途站点完成");
        return travelRouteDao.save(travelRoute);
    }

    @Override
    @Transactional
    public TravelRoute getTravelRouteByUserId(String userId) {
        if (null == userId) {
            throw new AppException("缺少userId参数！");
        }
        logger.info("行程服务层->获取行程信息");

        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<TravelRoute> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();
            // 如果名称不为空，添加到查询条件。
            if (StringUtils.hasText(userId)) {
                predicate = builder.and(predicate, builder.like(root.get("userId"), "%" + userId + "%"));
            }
            query.orderBy(builder.desc(root.get("createDate")));
            return predicate;
        };

        // 调用JPA标准查询接口查询数据。
        List<TravelRoute> listTravelRoute = travelRouteDao.findAll(specification);
        if (0 == listTravelRoute.size()) {
            return null;
        }
        TravelRoute travelRoute = listTravelRoute.get(0);
        if (isInPresentRoute(travelRoute)) {
            /**
             * 获取沿途站点
             */
            List<RailwayStation> listStationOnTheWay = new ArrayList<RailwayStation>();
            List<LineStation> listLineStation = lineStationService.getLineStationBylineNo(travelRoute.getLineNo());
            
            //设置出发车站以及到达车站在本车次中的排序
            Integer startLineNo = 0;
            Integer arrivedLineNo = 100;
            
            for (LineStation lineStation : listLineStation) {
            	if(lineStation.getStation().equals(travelRoute.getAboardStation())){
            		startLineNo = lineStation.getSortNo();
            	}
            	
            	if(lineStation.getStation().equals(travelRoute.getArrivedStation())){
            		arrivedLineNo = lineStation.getSortNo();
            	}
            }
            
            
            for (LineStation lineStation : listLineStation) {
                if ((StringUtils.hasText(lineStation.getStation())) &&
                		(lineStation.getSortNo() >= startLineNo)&& 
                		(lineStation.getSortNo() <= arrivedLineNo)) {
                    RailwayStation railwayStation = railwayStationService.findByStationName(lineStation.getStation());
                    //如果当前车站服务状态为开启则显示
                    if ((null != railwayStation) && 
                    		(StationStatic.RAILWAYSTATION_STATUS_ON_SERVICE == railwayStation.getStatus())) {
                        listStationOnTheWay.add(railwayStation);
                    }
                }
            }
            travelRoute.setListStationOnTheWay(listStationOnTheWay);
            return travelRoute;
        }
        return null;
    }

    @Override
    public TravelRoute getTravelRouteByRouteId(String routeId) {
        if (null == routeId) {
            throw new AppException("缺少routeId参数！");
        }
        logger.info("行程服务层->获取行程信息->routeId:" + routeId);

        return travelRouteDao.getTravelRouteByRouteId(routeId);
    }

    @Override
    @Transactional
    public TravelRoute updateTravelRoute(TravelRoute travelRoute) {
        if (null == travelRoute) {
            throw new AppException("请选择要修改的用户行程信息！");
        }
        logger.info("行程服务层->更新行程信息->车次:" + travelRoute.getLineNo());
        
        if (null == travelRoute.getRouteId()) {
            throw new AppException("请选择要修改的用户行程信息！");
        }
        TravelRoute oldTravelRoute = travelRouteDao.getTravelRouteByRouteId(travelRoute.getRouteId());

        //设置基本信息
        oldTravelRoute.setCarriageNumber(travelRoute.getCarriageNumber());
        oldTravelRoute.setSeatNumber(travelRoute.getSeatNumber());
        oldTravelRoute.setCustomerName(travelRoute.getCustomerName());
        oldTravelRoute.setCustomerPhone(travelRoute.getCustomerPhone());

        //如果修改了车次号，则同时修改出发车站、到达车站、出发时间、到达时间
        if ((!StringUtils.hasText(travelRoute.getLineNo()))
                || (oldTravelRoute.getLineNo().equals(travelRoute.getLineNo()))) {
            return travelRouteDao.save(oldTravelRoute);
        }

        oldTravelRoute = setValuesByLineNo(oldTravelRoute, travelRoute.getLineNo());
        return travelRouteDao.save(oldTravelRoute);
    }

    /**
     * 判断用户是否还处于当前行程
     * 当前判断逻辑：如果日期相同则处于当前行程
     *
     * @param travelRoute
     * @return
     */
    public boolean isInPresentRoute(TravelRoute travelRoute) {
        if (null == travelRoute.getArrivedTime()) {
            return false;
        }
        logger.info("行程服务层->判断用户是否处于当前行程—>车次：" + travelRoute.getLineNo());

        Calendar arrivedTime = Calendar.getInstance();
        arrivedTime.setTime(travelRoute.getArrivedTime());

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        //如果当前时间早于到达时间，则处于当前行程
        if (now.before(arrivedTime)) {
            return true;
        }

        //如果日期相同且在到达时间1个小时以内则处于当前行程
        if ((arrivedTime.get(Calendar.YEAR) == now.get(Calendar.YEAR))
                && (arrivedTime.get(Calendar.MONTH) == now.get(Calendar.MONTH))
                && (arrivedTime.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH))
                && (arrivedTime.get(Calendar.HOUR_OF_DAY)) > now.get(Calendar.HOUR_OF_DAY) - 1) {
            return true;
        }
        return false;
    }

    /**
     * 根据车次信息设置行程的起始车站、出发以及到达时间
     *
     * @param travelRoute
     * @param lineNo
     * @return TravelRoute
     * @author lid
     * @date 2017.2.20
     */
    public TravelRoute setValuesByLineNo(TravelRoute travelRoute, String lineNo) {
        travelRoute.setLineNo(lineNo);
        logger.info("行程服务层->根据车次信息设置行程的起始车站、出发以及到达时间—>车次：" + lineNo);

        //输入了车次信息，获取对应的起始车站
        List<LineStation> listLineStation = lineStationService.getLineStationBylineNo(lineNo);
        //当高铁线路信息列表大于1时才认为获取了正确的信息
        if (1 < listLineStation.size()) {
            String aboardStation = listLineStation.get(0).getStation();
            String arrivedStation = listLineStation.get(listLineStation.size() - 1).getStation();

            String startTime = listLineStation.get(0).getDepartTime();
            String arriveTime = listLineStation.get(listLineStation.size() - 1).getArriveTime();

            //设置出发站
            if (StringUtils.hasText(aboardStation)) {
                travelRoute.setAboardStation(aboardStation);
            }
            //设置到达站
            if (StringUtils.hasText(arrivedStation)) {
                travelRoute.setArrivedStation(arrivedStation);
            }

            //设置出发时间
            if (StringUtils.hasText(startTime)) {
                travelRoute.setAboardTime(TimeUtil.getDateByString(startTime));
            }

            //设置到达时间
            if (StringUtils.hasText(arriveTime)) {
                travelRoute.setArrivedTime(TimeUtil.getDateByString(arriveTime));
            }
        }
        return travelRoute;
    }

    @Override
    public void deleteTravelRoute(String routeId) {
        if (null == routeId) {
            throw new AppException("请选择要删除的行程记录！");
        }
        logger.info("行程服务层->删除行程记录->路线ID：" + routeId);

        TravelRoute travelRoute = travelRouteDao.findOne(routeId);
        if (null == travelRoute) {
            throw new AppException("未找到要删除的行程记录！");
        }
        travelRouteDao.delete(travelRoute);
    }

}
