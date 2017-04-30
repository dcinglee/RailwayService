package com.railwayservice.stationmanage.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.stationmanage.entity.LineStation;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.service.LineStationService;
import com.railwayservice.stationmanage.service.RailwayStationService;
import com.railwayservice.stationmanage.vo.DestinationStationVo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidx
 * @date 2017/2/9
 * @describe 车站模块请求控制器
 */
@Controller
@RequestMapping(value = "/railwayStation", produces = {"application/json;charset=UTF-8"})
@Api(value = "车站模块请求控制器", description = "车站模块的相关操作")
@Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_SYSTEM_STATION_MANAGE"})
public class RailwayStationController {

    private final Logger logger = LoggerFactory.getLogger(RailwayStationController.class);

    private RailwayStationService railwayStationService;

    private LineStationService lineStationService;

    @Autowired
    public void setLineStationService(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    /**
     * @param lineNo
     * @return
     * @author lid
     */
    @ResponseBody
    @RequestMapping("/getLineStationBylineNo")
    public ResultMessage getLineStationBylineNo(String lineNo) {
        logger.info("getLineStationBylineNo：" + lineNo);
        try {
            List<LineStation> listLineStation = lineStationService.getLineStationBylineNo(lineNo);
            return ResultMessage.newSuccess("查询数据成功").setData(listLineStation);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }


    @ResponseBody
    @RequestMapping("/queryByStationId")
    public ResultMessage queryByStationId(String stationId) {
        logger.info("车站控制层->查询车站->车站ID：" + stationId);
        try {
            RailwayStation railwayStation = railwayStationService.findByStationId(stationId);
            return ResultMessage.newSuccess().setData(railwayStation);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 车站查询
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultMessage queryRailwayServiceStation(String stationName, Integer status, PageParam param) {
        logger.info("车站控制层->查询车站->车站名称：" + stationName);
        try {
            PageData dataPage = railwayStationService.queryRailwayStationPage(param, stationName, status);
            return ResultMessage.newSuccess().setData(dataPage);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 查询所有车站
     */
    @ResponseBody
    @RequestMapping("/queryAllStations")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage queryAllStations() {
        logger.info("车站控制层->查询所有车站");
        try {
            List<RailwayStation> railwayStationList = railwayStationService.queryAllStations();
            return ResultMessage.newSuccess().setData(railwayStationList);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 查询所有高铁车站
     */
    @ResponseBody
    @RequestMapping("/queryAllSpeedRailWayStations")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage queryAllSpeedRailWayStations() {
        logger.info("车站控制层->查询所有车站");
        try {
            List<RailwayStation> railwayStationList = railwayStationService.findAllSpeedRailWayStation();
            return ResultMessage.newSuccess().setData(railwayStationList);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 查询所有车站
     */
    @ResponseBody
    @RequestMapping("/queryStationsByUser")
    public ResultMessage queryAllStationsByUser(HttpServletRequest request) {
        logger.info("车站控制层->查询所有车站");
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            List<RailwayStation> railwayStationList = railwayStationService.queryAllStationsByUser(adminLogined);
            return ResultMessage.newSuccess().setData(railwayStationList);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 新增车站
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addStation(RailwayStation station, String imageId) {
        logger.info("车站控制层->新增车站->车站名称：" + station.getStationName());
        try {
            railwayStationService.addStation(station, imageId);
            return ResultMessage.newSuccess("新增车站成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增车站异常：", e);
            return ResultMessage.newFailure("新增车站异常！");
        }
    }

    /**
     * 修改车站
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateStation(RailwayStation station, String imageId) {
        logger.info("车站控制层->修改车站->车站名称：" + station.getStationName());
        try {
            railwayStationService.updateStation(station, imageId);
            return ResultMessage.newSuccess("修改车站成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改车站异常：", e);
            return ResultMessage.newFailure("修改车站异常！");
        }
    }

    /**
     * 删除单个车站
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage deleteStation(String stationId) {
        logger.info("车站控制层->删除车站->车站ID：" + stationId);
        try {
            //不允许删除车站，只允许将车站的状态改为启用或不启用。
            return ResultMessage.newFailure("不允许删除车站");
//            railwayStationService.deleteStation(stationId);
//            return ResultMessage.newSuccess("删除站点成功");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除车站异常：", e);
            return ResultMessage.newFailure("删除车站异常");
        }
    }

    /**
     * 删除多个车站
     */
    @ResponseBody
    @RequestMapping("/deleteInBatch")
    public ResultMessage deleteInBatchStation(List<String> stationIds) {
        logger.info("车站控制层->删除车站");
        try {
            railwayStationService.deleteStationInBatch(stationIds);
            return ResultMessage.newSuccess("删除站点成功");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除车站异常：", e);
            return ResultMessage.newFailure("删除车站异常");
        }
    }

    /**
     * 通过经纬度查询车站信息
     */
    @ResponseBody
    @RequestMapping("/queryStation")
    public ResultMessage queryStation(Double longitude, Double latitude) {
        logger.info("车站控制层->查询车站信息");
        try {
            RailwayStation station = railwayStationService.findRailwayStation(longitude, latitude);
            return ResultMessage.newSuccess("查询站点成功").setData(station);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询车站异常：", e);
            return ResultMessage.newFailure("查询车站异常");
        }
    }

    /**
     * 查询当前站点所有车次，以及该车次在该站点的发车时间
     * 只获取当前时间之后的车次以及按发车时间排序
     * 通过参数确定获取高铁和动车的分类
     * type:D 动车   G:高铁
     *
     * @param stationName
     * @return
     * @author lid
     * @date 2017.3.11
     */
    @ResponseBody
    @RequestMapping("/queryLineStationByStationName")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage queryLineStationByStationName(String stationName) {
        logger.info("车站控制层->查询车站信息->车站名称：" + stationName);
        try {
            List<LineStation> listLineStationG = railwayStationService.getListStationInfo(stationName, "G");
            List<LineStation> listLineStationD = railwayStationService.getListStationInfo(stationName, "D");
            Map<String, List<LineStation>> map = new HashMap<String, List<LineStation>>();
            map.put("G", listLineStationG);
            map.put("D", listLineStationD);
            return ResultMessage.newSuccess("查询车次及发车时间成功").setData(map);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询车次及发车时间异常：", e);
            return ResultMessage.newFailure("查询车次及发车时间异常");
        }
    }

    /**
     * 根据始发站和车次找到可以到达的目的车站
     *
     * @param lineNo
     * @param stationName
     * @return
     * @author lid
     * @date 2017.3.11
     */
    @ResponseBody
    @RequestMapping("/getDestinationByStationAndNo")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage getDestinationByStationAndNo(String lineNo, String stationName) {
        logger.info("车站控制层->根据始发站和车次找到可以到达的目的车站->车站名称：" + stationName);
        try {
            List<DestinationStationVo> listDestination = lineStationService.getDestinationByStationAndNo(lineNo, stationName);
            return ResultMessage.newSuccess("查询车次及发车时间成功").setData(listDestination);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询车次及发车时间异常：", e);
            return ResultMessage.newFailure("查询车次及发车时间异常");
        }
    }

    @ResponseBody
    @RequestMapping("/getOnLineStation")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage getOnLineStation() {
        logger.info("车站控制层->查询提供服务的车站");
        try {
            List<RailwayStation> listRailwayStation = railwayStationService.getOnLineStation();
            return ResultMessage.newSuccess("查询提供服务的车站成功").setData(listRailwayStation);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询提供服务的车站异常：", e);
            return ResultMessage.newFailure("查询提供服务的车站异常");
        }
    }

    /**
     * 更新车站示意图图片信息。
     */
    @ResponseBody
    @RequestMapping("/updateImage")
    public ResultMessage addImage(String stationId, @RequestParam("imageFile") MultipartFile imageFile) {
        logger.info("车站控制器：更新车站示意图图片信息：车站ID：" + stationId);
        try {
            RailwayStation railwayStation = railwayStationService.updateImage(stationId, imageFile.getInputStream());
            return ResultMessage.newSuccess("更新车站示意图图片成功！").setData(railwayStation);
        } catch (Exception e) {
            logger.error("更新车站示意图图片异常：", e);
            return ResultMessage.newFailure("更新车站示意图图片失败！");
        }
    }

    /**
     * 查询有所火车站和高铁站
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findAllStationForBooking")
    public ResultMessage findAllStationForBooking() {
        logger.info("车站控制器：查询所有的高铁站和火车站");
        try {
            List<RailwayStation> railwayStation = railwayStationService.findAllStation();
            return ResultMessage.newSuccess("查询所有的高铁站和火车站成功").setData(railwayStation);
        } catch (Exception e) {
            logger.error("查询所有的高铁站和火车站：", e);
            return ResultMessage.newFailure("查询所有的高铁站和火车站！");
        }
    }
}
