package com.railwayservice.user.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.user.entity.TravelRoute;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.TravelRouteService;
import com.railwayservice.user.vo.TravelRouteVo;
import com.railwayserviceWX.config.WeixinConfig;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 行程模块请求控制器。
 *
 * @author lid
 * @date 2017.2.4
 */
@Controller
@RequestMapping(value = "/travelRoute", produces = {"application/json;charset=UTF-8"})
@Api(value = "行程模块请求控制器", description = "行程模块的相关操作")
public class TravelRouteController {

    private final Logger logger = LoggerFactory.getLogger(TravelRouteController.class);

    private TravelRouteService travelRouteService;

    @Autowired
    public void setTravelRouteService(TravelRouteService travelRouteService) {
        this.travelRouteService = travelRouteService;
    }

    /**
     * 添加行程
     *
     * @param userId
     * @param customerName
     * @param customerPhone
     * @param lineNo
     * @param carriageNumber
     * @param seatNumber
     * @return ResultMessage
     * @author lid
     * @date 2017.2.17
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addTravelRoute(@RequestBody TravelRouteVo vo, HttpServletRequest req, HttpServletResponse res) {
        logger.info("行程控制层->添加行程->车次:" + vo.getLineNo());
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null == user) {
            String jumpUrl = WeixinConfig.HOST + "/entrance/index";
            try {
                res.sendRedirect(jumpUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
        }

        try {
            TravelRoute travelRoute = travelRouteService.addTravelRoute(user, vo);
            return ResultMessage.newSuccess("行程录入成功！").setData(travelRoute);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("行程录入异常：", e);
            return ResultMessage.newFailure("行程录入异常！");
        }
    }

    /**
     * 获取用户的行程信息
     * 1，判断当前用户是否存在行程信息，没有则返回空。
     * 2，获取最近的一条行程记录，判断用户是否还处于当前行程中，如果处于当前行程则返回行程信息，不处于则返回空。
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("/get")
    public ResultMessage getTravelRoute(HttpServletRequest req) {
        logger.info("行程控制层->获取行程信息");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null == user) {
            return ResultMessage.newFailure("未指定需要查询的用户");
        }

        try {
            TravelRoute travelRoute = travelRouteService.getTravelRouteByUserId(user.getUserId());
            return ResultMessage.newSuccess("获取行程信息成功").setData(travelRoute);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取行程信息异常：", e);
            return ResultMessage.newFailure("获取行程信息异常！");
        }
    }

    /**
     * 修改行程信息
     *
     * @param travelRoute
     * @return ResultMessage
     * @author lid
     * @date 2017.2.18
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateTravelRoute(TravelRoute travelRoute) {
        if (null == travelRoute) {
            return ResultMessage.newFailure("用户行程不能为空！");
        }

        logger.info("行程控制层->更新行程信息->车次：" + travelRoute.getLineNo());

        try {
            TravelRoute newTravelRoute = travelRouteService.updateTravelRoute(travelRoute);
            return ResultMessage.newSuccess("修改行程信息成功").setData(newTravelRoute);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改行程信息异常：", e);
            return ResultMessage.newFailure("修改行程信息异常！");
        }

    }

}
