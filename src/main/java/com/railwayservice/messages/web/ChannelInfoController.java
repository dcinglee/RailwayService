package com.railwayservice.messages.web;

import com.railwayservice.application.cache.MerchantSessionCache;
import com.railwayservice.application.cache.ServiceProviderSessionCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.push.PushFactory;
import com.railwayservice.application.push.PushService;
import com.railwayservice.application.push.PushServiceType;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.messages.service.ChannelInfoService;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 在线设备终端（手机）控制器
 *
 * @author xuyu
 */
@Controller
@RequestMapping(value = "/channel", produces = {"application/json;charset=UTF-8"})
@Api(value = "在线设备终端（手机）控制器", description = "在线设备终端（手机）控制器")
public class ChannelInfoController {

    private final Logger logger = LoggerFactory.getLogger(ChannelInfoController.class);

    private MerchantSessionCache merchantSessionCache;

    private ServiceProviderSessionCache serviceProviderSessionCache;

    private ChannelInfoService channelInfoService;

    @Autowired
    public void setChannelInfoService(ChannelInfoService channelInfoService) {
        this.channelInfoService = channelInfoService;
    }

    @Autowired
    public void setMerchantSessionCache(MerchantSessionCache merchantSessionCache) {
        this.merchantSessionCache = merchantSessionCache;
    }

    @Autowired
    public void setServiceProviderSessionCache(ServiceProviderSessionCache serviceProviderSessionCache) {
        this.serviceProviderSessionCache = serviceProviderSessionCache;
    }

    @ResponseBody
    @RequestMapping("/merchantWorkStatus")
    public void merchantWorkStatus(@RequestParam("channelId") String channelId, @RequestParam("deviceType") String deviceType, @RequestParam("status") Integer status, HttpServletRequest request) {
        try {
            logger.info("收到手机端发送过来的setWorkStatus包");
            // 获取加密盐。
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (!StringUtils.hasText(token)) {
                logger.info("令牌环无效，请重试");
                return;
            }
            Merchant merchant = merchantSessionCache.get(token);
            String userId = (merchant == null ? null : merchant.getMerchantId());
            channelInfoService.updateChannelInfoAndMerchantServiceStatus(userId, channelId, deviceType, status);
//            return ResultMessage.newSuccess("UPDATE CHANNEL成功！");
        } catch (AppException ae) {
            logger.error("setWorkStatus异常：" + ae.getMessage());
            ae.printStackTrace();
        } catch (Exception e) {
            logger.error("setWorkStatus异常：", e);
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping("/serviceProviderWorkStatus")
    public void serviceProviderWorkStatus(@RequestParam("channelId") String channelId, @RequestParam("deviceType") String deviceType, @RequestParam("status") Integer status, HttpServletRequest request) {
        try {
            logger.info("收到手机端发送过来的setWorkStatus包");
            // 获取加密盐。
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (!StringUtils.hasText(token)) {
                logger.info("令牌环无效，请重试");
                return;
            }
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(token);
            String userId = (serviceProvider == null ? null : serviceProvider.getServiceProviderId());
            channelInfoService.updateChannelInfoAndMerchantServiceStatus(userId, channelId, deviceType, status);
//            return ResultMessage.newSuccess("UPDATE CHANNEL成功！");
        } catch (AppException ae) {
            logger.error("setWorkStatus异常：" + ae.getMessage());
            ae.printStackTrace();
        } catch (Exception e) {
            logger.error("setWorkStatus异常：", e);
            e.printStackTrace();
        }
    }

    /**
     * 新增消息。
     *
     * @param channelId
     * @param deviceType android或者ios
     * @param status     6001 工作  6002休息  空值 表示不知道当前状态，只更新在线时间
     */
    @ResponseBody
    @RequestMapping("/merchantHeartBreak")
    public void merchantHeartBreak(@RequestParam("channelId") String channelId, @RequestParam("deviceType") String deviceType, @RequestParam("status") Integer status, HttpServletRequest request) {
        try {
            logger.info("收到手机端发送过来的心跳包");
            // 获取加密盐。
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (!StringUtils.hasText(token)) {
                logger.info("令牌环无效，请重试");
                return;
            }
//          return ResultMessage.newFailure("验证令牌无效，请重试。");
            Merchant merchant = merchantSessionCache.get(token);
            String userId = (merchant == null ? null : merchant.getMerchantId());
            channelInfoService.updateChannelInfo(userId, channelId, deviceType, status);
//            return ResultMessage.newSuccess("UPDATE CHANNEL成功！");
        } catch (AppException ae) {
            logger.error("UPDATE CHANNEL异常：" + ae.getMessage());
            ae.printStackTrace();
//            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("UPDATE CHANNEL异常：", e);
            e.printStackTrace();
//            return ResultMessage.newFailure("UPDATE CHANNEL异常！");
        }
    }

    /**
     * 新增消息。
     *
     * @param channelId
     * @param deviceType android或者ios
     * @param status     6001 工作  6002休息  空值 表示不知道当前状态，只更新在线时间
     */
    @ResponseBody
    @RequestMapping("/serviceProviderHeartBreak")
    public void serviceProviderHeartBreak(@RequestParam("channelId") String channelId, @RequestParam("deviceType") String deviceType, @RequestParam("status") Integer status, HttpServletRequest request) {
        try {
            logger.info("收到手机端发送过来的心跳包");
            // 获取加密盐。
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (!StringUtils.hasText(token)) {
                logger.info("令牌环无效，请重试");
                return;
            }
//          return ResultMessage.newFailure("验证令牌无效，请重试。");
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(token);
            String userId = (serviceProvider == null ? null : serviceProvider.getServiceProviderId());
            channelInfoService.updateChannelInfo(userId, channelId, deviceType, status);
//            return ResultMessage.newSuccess("UPDATE CHANNEL成功！");
        } catch (AppException ae) {
            logger.error("UPDATE CHANNEL异常：" + ae.getMessage());
            ae.printStackTrace();
//            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("UPDATE CHANNEL异常：", e);
            e.printStackTrace();
//            return ResultMessage.newFailure("UPDATE CHANNEL异常！");
        }
    }

    /**
     * 测试发送消息
     *
     * @param channelId
     * @param title     title
     * @param msg       消息体
     */
    @ResponseBody
    @RequestMapping("/testSendMsg")
    public void testSendMsg(@RequestParam("channelId") String channelId, @RequestParam("title") String title, @RequestParam("msg") String msg) {
        try {
            logger.info("测试发送消息");

            PushService service = PushFactory.getPushService(PushServiceType.JIGUANG);
            service.pushSms4ServiceProvider(channelId, title, msg);

        } catch (AppException ae) {
            logger.error("testSendMsg异常：" + ae.getMessage());
            ae.printStackTrace();
//            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("testSendMsg异常：", e);
            e.printStackTrace();
//            return ResultMessage.newFailure("UPDATE CHANNEL异常！");
        }
    }

    /**
     * 测试发送消息
     *
     * @param channelId
     * @param title     title
     * @param msg       消息体
     */
    @ResponseBody
    @RequestMapping("/testSendMsg4Merchant")
    public void testSendMsg4Merchant(@RequestParam("channelId") String channelId, @RequestParam("title") String title, @RequestParam("msg") String msg) {
        try {
            logger.info("测试发送消息");
            try {
                PushService service = PushFactory.getPushService(PushServiceType.JIGUANG);
                service.pushSms4Merchant(channelId, title, msg);

//                new PushMessage().pushMessageToBatch4Merchant(channelId, 3, title, msg);
            } catch (Exception e) {
                logger.error("testSendMsg4Merchant发生异常" + e);
                e.printStackTrace();
                throw new AppException("testSendMsg4Merchant发生异常" + e);
            }
//            return ResultMessage.newSuccess("UPDATE CHANNEL成功！");
        } catch (AppException ae) {
            logger.error("testSendMsg4Merchant异常：" + ae.getMessage());
            ae.printStackTrace();
//            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("testSendMsg4Merchant异常：", e);
            e.printStackTrace();
//            return ResultMessage.newFailure("UPDATE CHANNEL异常！");
        }
    }

}
