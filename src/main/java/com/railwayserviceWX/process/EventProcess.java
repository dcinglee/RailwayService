package com.railwayserviceWX.process;

import com.railwayservice.user.entity.User;
import com.railwayserviceWX.controller.WechatAccessor;
import com.railwayserviceWX.msg.EventMsg;
import com.railwayserviceWX.msg.Msg;
import com.railwayserviceWX.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信事件消息处理类
 *
 * @author lid
 * @date 2017.2.24
 */
public class EventProcess {

    private static Logger logger = LoggerFactory.getLogger(EventProcess.class);

    /**
     * 关注事件处理,获取用户信息
     *
     * @param msg
     * @return User
     * @author lid
     */
    public static User subscribeProcess(EventMsg msg) {
        logger.info("subscribeProcess,msg:" + msg);
        //判断是否为关注事件
        if (!Msg.EVENT_TYPE_SUBSCRIBE.equals(msg.getEvent())) {
            logger.info("非关注事件");
            return null;
        }

        String openid = msg.getFromUserName();
        return WechatAccessor.getUserInfo(openid);
    }
}
