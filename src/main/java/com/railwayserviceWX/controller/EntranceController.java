package com.railwayserviceWX.controller;

import com.railwayservice.application.cache.RailwayStationCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.stationmanage.entity.LineStation;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.service.LineStationService;
import com.railwayservice.stationmanage.service.RailwayStationService;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.UserService;
import com.railwayserviceWX.config.WeixinConfig;
import com.railwayserviceWX.util.CheckTrainUtil;
import com.railwayserviceWX.util.WXJSSDKSupport;
import com.railwayserviceWX.util.WeatherUtil;
import com.railwayserviceWX.vo.WeatherResultVo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 微信业务入口控制器。
 *
 * @author lid
 * @date 2017.2.26
 */
@Controller
@RequestMapping(value = "/entrance", produces = {"application/json;charset=UTF-8"})
@Api(value = "微信业务入口控制器", description = "微信业务入口控制器")
public class EntranceController {
    private final Logger logger = LoggerFactory.getLogger(EntranceController.class);

    private LineStationService lineStationService;

    private UserService userService;

    private RailwayStationService railwayStationService;

    private static final String attentionUrl = "https://mp.weixin.qq.com/s?__biz=MzI0Mzc4NTYzMg==&mid=2247483651&idx=1&sn=a5dac6cc5dd2dda2ec0419c35bd1e1b9&chksm=e9668bccde1102da0778137f9131a46a02e76a0c6003b3ac10cfe2c6f5d1f69d5a6e26d96163#rd";

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    @Autowired
    public void setLineStationService(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 站内点餐入口
     *
     * @param req
     * @param res
     * @return
     * @throws Exception
     * @author lid
     */
    @RequestMapping("index")
    public void getCodeByPageIndex(HttpServletRequest req, HttpServletResponse res) throws Exception {
        logger.info("getCodeByPageIndex");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
        if (null != user) {
            logger.info("null != user" + user.getNickName());

            String jumpUrl = WeixinConfig.HOST + "/wechat/index.html";
            logger.info("jumpUrl :" + jumpUrl);
            res.sendRedirect(jumpUrl);
            return;
        }
        logger.info("null == user");
        String jumpUrl = WeixinConfig.HOST + "/entrance/userInfo";

        logger.info("jumpUrl   :" + jumpUrl);
        String location = WeixinConfig.CODE_URL.replaceFirst("APPID", WeixinConfig.APPID)
                .replaceFirst("REDIRECT_URI", URLEncoder.encode(jumpUrl, "UTF-8"))
                .replaceFirst("SCOPE", "snsapi_base").replaceFirst("STATE", "RailwayService");

        logger.info("location:" + location);
        res.sendRedirect(location);
    }

    @RequestMapping("userInfo")
    public void pcenter(String code, HttpServletRequest req, HttpServletResponse res) throws Exception {
        logger.info("pcenter");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null != user) {
            logger.info("null != user");
            String jumpUrl = WeixinConfig.HOST + "/wechat/index.html";
            logger.info("jumpUrl :" + jumpUrl);
            res.sendRedirect(jumpUrl);
        }

        if (!StringUtils.hasText(code)) {
            logger.info("code == null");
            return;
        }
        logger.info("code:" + code);
        String openid = WechatAccessor.getAuthedUserInfo(code, true);
        if (null == openid) {
            logger.info("网页授权获取用户信息失败");
            return;
        }
        user = userService.getUserByOpenid(openid);
        //如果数据库没有记录，则说明用户没有关注，跳转到关注页面
        if (null == user) {
            logger.info("null == user");
            res.sendRedirect(attentionUrl);
            return;
        }

        //缓存用户信息
        req.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);

        logger.info("网页授权获取用户信息成功 ：" + user.getNickName());

        String jumpUrl = WeixinConfig.HOST + "/wechat/index.html";
        logger.info("jumpUrl :" + jumpUrl);
        res.sendRedirect(jumpUrl);
    }

    @RequestMapping("test")
    public void test(HttpServletRequest req, HttpServletResponse res) throws Exception {
        logger.info("pcenter");
        String openid = req.getParameter("openid");
        String redirectUrl = req.getParameter("redirectUrl");
        if (openid == null || "".equals(openid)) {
            openid = "ooD1YwTQBJ3eApmj7JLA0q45IQUY";
        }
        if (redirectUrl == null || "".equals(redirectUrl)) {
            redirectUrl = req.getRequestURL().toString();
            redirectUrl = redirectUrl.substring(0, redirectUrl.indexOf(req.getServletPath()));
        }
        User user = userService.getUserByOpenid(openid);
        //如果数据库没有记录，则说明用户没有关注，跳转到关注页面
        if (null == user) {
            logger.info("null == user");
            res.sendRedirect(attentionUrl);
            return;
        }

        //缓存用户信息
        req.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);

        logger.info("网页授权获取用户信息成功 ：" + user.getNickName());

        String jumpUrl = redirectUrl + "/wechat/index.html";
        logger.info("jumpUrl :" + jumpUrl);
        res.sendRedirect(jumpUrl);
    }

    /**
     * 抢票入口
     *
     * @param req
     * @param res
     * @return
     * @throws Exception
     * @author xuy
     */
    @RequestMapping("ticket")
    public void ticket(HttpServletRequest req, HttpServletResponse res) throws Exception {
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
        if (null != user) {
            String jumpUrl = WeixinConfig.HOST + "/wechat/trainTickets/index.html";
            logger.info("jumpUrl :" + jumpUrl);
            res.sendRedirect(jumpUrl);
            return;
        }

        String jumpUrl = WeixinConfig.HOST + "/entrance/ticketIndex";

        logger.info("jumpUrl:" + jumpUrl);
        String location = WeixinConfig.CODE_URL.replaceFirst("APPID", WeixinConfig.APPID)
                .replaceFirst("REDIRECT_URI", URLEncoder.encode(jumpUrl, "UTF-8"))
                .replaceFirst("SCOPE", "snsapi_base").replaceFirst("STATE", "RailwayService");

        logger.info("location:" + location);
        res.sendRedirect(location);
    }

    @RequestMapping("ticketIndex")
    public void getTicketPageIndex(String code, HttpServletRequest req, HttpServletResponse res) throws Exception {
        logger.info("getTicketPageIndex");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null != user) {
            logger.info("null != user");
            String jumpUrl = WeixinConfig.HOST + "/wechat/trainTickets/index.html";
            logger.info("jumpUrl :" + jumpUrl);
            res.sendRedirect(jumpUrl);
        }

        if (!StringUtils.hasText(code)) {
            logger.info("code == null");
            return;
        }
        logger.info("code:" + code);
        String openid = WechatAccessor.getAuthedUserInfo(code, true);
        if (null == openid) {
            logger.info("网页授权获取用户信息失败");
            return;
        }
        user = userService.getUserByOpenid(openid);
        //如果数据库没有记录，则说明用户没有关注，跳转到关注页面
        if (null == user) {
            logger.info("null == user");
            res.sendRedirect(attentionUrl);
            return;
        }

        //缓存用户信息
        req.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);

        logger.info("网页授权获取用户信息成功 ：" + user.getNickName());

        String jumpUrl = WeixinConfig.HOST + "/wechat/trainTickets/index.html";
        logger.info("jumpUrl :" + jumpUrl);
        res.sendRedirect(jumpUrl);
    }

    /**
     * 我的订单入口
     *
     * @param req
     * @param res
     * @return
     * @throws Exception
     * @author lid
     */
    @RequestMapping("myOrdersIndex")
    public void getCodeByPageMyOrders(HttpServletRequest req, HttpServletResponse res) throws Exception {
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
        if (null != user) {
            String jumpUrl = WeixinConfig.HOST + "/wechat/order.html";
            logger.info("jumpUrl :" + jumpUrl);
            res.sendRedirect(jumpUrl);
            return;
        }

        String jumpUrl = WeixinConfig.HOST + "/entrance/myOrders";

        logger.info("jumpUrl:" + jumpUrl);
        String location = WeixinConfig.CODE_URL.replaceFirst("APPID", WeixinConfig.APPID)
                .replaceFirst("REDIRECT_URI", URLEncoder.encode(jumpUrl, "UTF-8"))
                .replaceFirst("SCOPE", "snsapi_base").replaceFirst("STATE", "RailwayService");

        logger.info("location:" + location);
        res.sendRedirect(location);
    }

    @RequestMapping("myOrders")
    public void myOrders(String code, HttpServletRequest req, HttpServletResponse res) throws Exception {
        logger.info("pcenter");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null != user) {
            logger.info("null != user");
            String jumpUrl = WeixinConfig.HOST + "/wechat/order.html";
            logger.info("jumpUrl :" + jumpUrl);
            res.sendRedirect(jumpUrl);
        }

        if (!StringUtils.hasText(code)) {
            logger.info("code == null");
            return;
        }
        logger.info("code:" + code);
        String openid = WechatAccessor.getAuthedUserInfo(code, true);
        if (null == openid) {
            logger.info("网页授权获取用户信息失败");
            return;
        }
        user = userService.getUserByOpenid(openid);
        //如果数据库没有记录，则说明用户没有关注，跳转到关注页面
        if (null == user) {
            logger.info("null == user");
            res.sendRedirect(attentionUrl);
            return;
        }

        //缓存用户信息
        req.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);

        logger.info("网页授权获取用户信息成功 ：" + user.getNickName());

        String jumpUrl = WeixinConfig.HOST + "/wechat/order.html";
        logger.info("jumpUrl :" + jumpUrl);
        res.sendRedirect(jumpUrl);
    }

    @ResponseBody
    @RequestMapping("getCurrentUser")
    public ResultMessage getCurrentUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
        logger.info("getCurrentUser");

        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
        if (null != user) {
            logger.info("null != user");

            /**
             * 测试环境流程代码
             *//*
            if (WechatControllerTest.isTest) {
                logger.info("openid:" + user.getOpenid() + ",nickname:" + user.getNickName() + "currentOpenid:" + WechatControllerTest.currentOpenid.toString());
                if (user.getOpenid().equals(WechatControllerTest.currentOpenid.toString())) {
                    logger.info("user.getOpenid().equals(WechatControllerTest.currentOpenid.toString())");
                    return ResultMessage.newSuccess("获取当前用户成功！").setData(user);
                }
                user = userService.getUserByOpenid(WechatControllerTest.currentOpenid.toString());
                logger.info("openid:" + user.getOpenid() + ",nickname:" + user.getNickName());
                req.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);
                logger.info("保存当前用户成功！nickname:" + user.getNickName());
                return ResultMessage.newSuccess("获取当前用户成功！").setData(user);
            }*/

            return ResultMessage.newSuccess("获取当前用户成功！").setData(user);
        }

        logger.info("null == user");


        /**
         * 测试环境流程代码
         */
        if (WechatControllerTest.isTest) {
            //如果是测试环境则获取用户信息
            //先从缓存中通过ip获取用户信息，获取失败则从内从中读取openid并获取用户信息保存到缓存

        	/*String ipAddress = IpAddressUtils.getIpAddr(req);
        	logger.info("ipAddress:"+ipAddress);
        	user = UserCache.get("ipAddress");
        	if(null == user){
        		logger.info(WechatControllerTest.currentOpenid.toString());
                user = userService.getUserByOpenid(WechatControllerTest.currentOpenid.toString());
                UserCache.put(ipAddress, user);
                logger.info("保存当前用户成功！nickname:" + user.getNickName());
                return ResultMessage.newSuccess("获取当前用户成功！").setData(user);
        	}
        	logger.info("获取当前用户成功！nickname:" + user.getNickName());
            return ResultMessage.newSuccess("获取当前用户成功！").setData(user);*/

            logger.info("WechatControllerTest.isTest");
            user = userService.getUserByOpenid(WechatControllerTest.currentOpenid.toString());
            logger.info("openid:" + (user != null ? user.getOpenid() : "") + ",nickname:" + (user != null ? user.getNickName() : ""));
            req.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);
            logger.info("保存当前用户成功！nickname:" + (user != null ? user.getNickName() : ""));
            return ResultMessage.newSuccess("获取当前用户成功！").setData(user);

        }


        String jumpUrl = WeixinConfig.HOST + "/entrance/index";
        res.sendRedirect(jumpUrl);
        return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
    }

    @ResponseBody
    @RequestMapping("getCurrentStation")
    public ResultMessage getCurrentStation(HttpServletRequest req, HttpServletResponse res) throws Exception {

        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
        if (null == user) {
            String jumpUrl = WeixinConfig.HOST + "/entrance/index";
            res.sendRedirect(jumpUrl);
            return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
        }

        RailwayStation presentStation = RailwayStationCache.get(user.getOpenid());
        if (null != presentStation) {
            logger.info("获取当前站点成功！presentStationName:" + presentStation.getStationName());
            return ResultMessage.newSuccess("获取当前站点成功！").setData(presentStation);
        }

        RailwayStation station = railwayStationService.findByStationName("深圳北");
        req.getSession().setAttribute("presentStation", station);
        logger.info("获取当前站点失败！");
        return ResultMessage.newFailure("获取当前站点失败,返回深圳北站！").setData(station);
    }

    @ResponseBody
    @RequestMapping("getWeather")
    public ResultMessage getWeather(String cityId) {
        if (!StringUtils.hasText(cityId)) {
            return ResultMessage.newFailure("城市编码为空！");
        }
        try {
            Map<String, Object> map = WeatherUtil.getWeather(cityId);

            WeatherResultVo vo = new WeatherResultVo();
            vo.setCity(map.get("city").toString());
            vo.setLowTemp(map.get("temp1").toString());
            vo.setUpTemp(map.get("temp2").toString());
            vo.setWeather(map.get("weather").toString());
            return ResultMessage.newSuccess().setData(vo);

        } catch (Exception e) {
            logger.error("查询数据异常：" + e.getMessage());
            return ResultMessage.newFailure("查询数据异常！");
        }

    }

    @ResponseBody
    @RequestMapping("checkTrain")
    public ResultMessage CheckTrain(String station, String lineNo) {
        logger.info("CheckTrain！");

        if (!StringUtils.hasText(station)) {
            return ResultMessage.newFailure("站点名称为空！");
        }

        if (!StringUtils.hasText(lineNo)) {
            return ResultMessage.newFailure("车次信息为空！");
        }

        try {
            String time = CheckTrainUtil.getCheckInfo(station, lineNo);

            if (!StringUtils.hasText(time)) {
                return ResultMessage.newSuccess().setData("暂无到达时间信息");
            }

            LineStation lineStation = lineStationService.findByStationAndLineNo(station, lineNo);
            String arriveTime = lineStation.getArriveTime();

            //如果是始发站，则到达时间为“----”,此时为准点
            if ((arriveTime.equals("----"))
                    || (arriveTime.equals(time))) {
                return ResultMessage.newSuccess().setData("正点");
            }

            String delayTime = CheckTrainUtil.calDelayTime(arriveTime, time);
            if (delayTime.contains("-")) {
                return ResultMessage.newSuccess().setData("正点");
            }
            return ResultMessage.newSuccess().setData("晚点" + delayTime);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping("wxshare")
    public ResultMessage wxshare(String url, HttpServletRequest req, HttpServletResponse res) {
        logger.info("wxshare!url:" + url);
        url = URLDecoder.decode(url);
        logger.info("url:" + url);
        Map<String, String> mapResult = WXJSSDKSupport.getValidateInfo(url);
        return ResultMessage.newSuccess().setData(mapResult);
    }

    /*==============================Ajax方式授权======================================*/

    /**
     * 前台得到获取授权Code的URL。
     *
     * @param req
     * @return
     * @throws Exception
     * @author lid
     */
    @ResponseBody
    @RequestMapping("preAuth")
    public ResultMessage preAuth(HttpServletRequest req) throws Exception {
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
        if (null != user) {
            return ResultMessage.newSuccess();
        }

        String jumpUrl = WeixinConfig.HOST + "/entrance/userInfo";

        String location = WeixinConfig.CODE_URL.replaceFirst("APPID", WeixinConfig.APPID)
                .replaceFirst("REDIRECT_URI", URLEncoder.encode(jumpUrl, "UTF-8"))
                .replaceFirst("SCOPE", "snsapi_base").replaceFirst("STATE", "RailwayService");

        logger.info("location:" + location);
        return ResultMessage.newFailure().setData(location);
    }

    /**
     * 通过前台从微信得到的Code去获取用户信息。
     *
     * @param code
     * @param req
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("userAuth")
    public ResultMessage userAuth(String code, HttpServletRequest req) throws Exception {
        logger.info("userAuth");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
        if (null != user)
            return ResultMessage.newSuccess();

        logger.info("code:" + code);
        if (!StringUtils.hasText(code))
            return ResultMessage.newFailure("授权码不能为空！");

        String openid = WechatAccessor.getAuthedUserInfo(code, true);
        if (null == openid) {
            logger.info("网页授权获取用户信息失败");
            return ResultMessage.newFailure("网页授权获取用户信息失败！");
        }

        user = userService.getUserByOpenid(openid);
        //如果数据库没有记录，则说明用户没有关注，跳转到关注页面
        if (null == user) {
            logger.info("用户" + openid + "未关注，返回关注页面地址。");
            return ResultMessage.newFailure().setCode(3).setData(attentionUrl);
        }

        //缓存用户信息
        req.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);

        logger.info("网页授权获取用户信息成功 ：" + user.getNickName());

        return ResultMessage.newSuccess();
    }

}
