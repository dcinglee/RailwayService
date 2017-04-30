package com.railwayserviceWX.controller;

import com.railwayservice.application.cache.RailwayStationCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.service.RailwayStationService;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.UserService;
import com.railwayserviceWX.config.WeixinConfig;
import com.railwayserviceWX.msg.*;
import com.railwayserviceWX.process.EventProcess;
import com.railwayserviceWX.util.DefaultSession;
import com.railwayserviceWX.util.HandleMessageAdapter;
import com.railwayserviceWX.util.MySecurity;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 微信通用接口控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping(value = "/wechatService", produces = {"application/json;charset=UTF-8"})
@Api(value = "微信通用接口控制器", description = "微信通用接口控制器")
public class WechatController {
    private final Logger logger = LoggerFactory.getLogger(WechatController.class);

    private UserService userService;

    private RailwayStationService railwayStationService;

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/listen", method = RequestMethod.GET)
    public void servletGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("listenGET");
        // 微信加密签名
        String signature = request.getParameter("signature");

        // 时间戳
        String timestamp = request.getParameter("timestamp");

        // 随机数
        String nonce = request.getParameter("nonce");

        // 随机字符串
        String echostr = request.getParameter("echostr");

        // 得到三个参数的拼接字符串
        List<String> list = new ArrayList<String>(3) {
            private static final long serialVersionUID = 2621444383666420433L;

            public String toString() {
                return this.get(0) + this.get(1) + this.get(2);
            }
        };

        list.add(WeixinConfig.TOKEN);
        list.add(timestamp);
        list.add(nonce);
        // 排序
        Collections.sort(list);

        // SHA-1加密
        String tmpStr = new MySecurity().encode(list.toString(), MySecurity.SHA_1);
        Writer out = response.getWriter();
        if (signature.equals(tmpStr)) {
            // 请求验证成功，返回随机码
            out.write(echostr);
        } else {
            out.write("");
        }
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/listen", method = RequestMethod.POST)
    public void servletPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置编码格式
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        InputStream inputStream = request.getInputStream();
        OutputStream outputStream = response.getOutputStream();

        final DefaultSession session = DefaultSession.newInstance();

        //添加微信服务器消息的监听
        session.addOnHandleMessageListener(new HandleMessageAdapter() {

            @Override
            public void onEventMsg(final EventMsg msg) {
                //如果为关注事件，则判断是否已保存用户信息，否则保存用户信息
                String eventType = msg.getEvent();
                logger.info("事件 eventType：" + eventType);
                String openid = msg.getFromUserName();

                //处理关注时间
                if (Msg.EVENT_TYPE_SUBSCRIBE.equals(eventType)) {
                    logger.info("新关注用户：openid：" + openid);
                    User user = null;
                    user = userService.getUserByOpenid(msg.getFromUserName());
                    if (null == user) {
                        user = EventProcess.subscribeProcess(msg);
                        try {
                            userService.addUser(user);
                            logger.info("添加用户信息成功！");
                            request.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);
                        } catch (AppException ae) {
                            logger.error("新增用户异常：" + ae.getMessage());
                        } catch (Exception e) {
                            logger.error("新增用户异常：" + e.getMessage());
                        }
                    } else {
                        request.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);
                    }
                }

                //处理地理位置事件
                if (Msg.EVENT_TYPE_LOCATION.equals(eventType)) {
                    logger.info("收到地理位置事件消息！");
                    request.getSession().setAttribute("location_X", msg.getLongitude());
                    request.getSession().setAttribute("location_Y", msg.getLatitude());

                    logger.info("X:" + msg.getLongitude());
                    logger.info("Y:" + msg.getLatitude());

                    if ((StringUtils.hasText(msg.getLongitude()))
                            && (StringUtils.hasText(msg.getLatitude()))) {
                        RailwayStation presentStation = railwayStationService.findRailwayStation(Double.valueOf(msg.getLongitude()), Double.valueOf(msg.getLatitude()));
                        if (null != presentStation) {
                            logger.info("获取当前最近车站成功！station：" + presentStation.getStationName());
                            RailwayStationCache.put(openid, presentStation);
                        }
                    }
                }
            }

            @Override
            public void onTextMsg(TextMsg msg) {
                System.out.println("收到文本消息：" + msg.getContent());
                TextMsg textMsg = new TextMsg();
                textMsg.setContent(msg.getContent());
                textMsg.setFuncFlag("0");
                textMsg.setFromUserName(msg.getToUserName());
                textMsg.setToUserName(msg.getFromUserName());
                textMsg.setCreateTime(msg.getCreateTime());
                //回传消息
                session.callback(textMsg);
            }

            @Override
            public void onImageMsg(ImageMsg msg) {
                System.out.println("onImageMsg");
            }

            @Override
            public void onLinkMsg(LinkMsg msg) {
                System.out.println("收到URL：" + msg.getUrl());
            }

            @Override
            public void onLocationMsg(LocationMsg msg) {
                String location_X = msg.getLocation_X();
                String location_Y = msg.getLocation_Y();
                //地图缩放大小
                String scale = msg.getScale();

                logger.info("收到地理位置消息！");

                logger.info("X:" + location_X);
                logger.info("Y:" + location_Y);

                request.getSession().setAttribute("location_X", location_X);
                request.getSession().setAttribute("location_Y", location_Y);
                request.getSession().setAttribute("scale", scale);
            }

            @Override
            public void onErrorMsg(int errorCode) {
                System.out.println("onErrorMsg：" + errorCode);
            }

            @Override
            public void onVoiceMsg(VoiceMsg msg) {
                logger.warn("收到语音消息");
                String content = msg.getRecognition();
                logger.warn("content :" + content);
                TextMsg reMsg = new TextMsg();
                reMsg.setFromUserName(msg.getToUserName());
                reMsg.setToUserName(msg.getFromUserName());
                reMsg.setCreateTime(msg.getCreateTime());

                reMsg.setContent("您发送的语音识别结果为  ：" + content);
                reMsg.setFuncFlag("0");
                session.callback(reMsg);//回传消息
            }

            @Override
            public void onVideoMsg(VideoMsg msg) {
                System.out.println("onVideoMsg：");
            }
        });
        session.process(inputStream, outputStream);
        session.close();
    }

}
