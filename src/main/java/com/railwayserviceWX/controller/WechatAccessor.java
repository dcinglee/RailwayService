package com.railwayserviceWX.controller;

import com.alibaba.fastjson.JSONObject;
import com.railwayservice.user.entity.User;
import com.railwayserviceWX.cache.AccessTokenCache;
import com.railwayserviceWX.cache.JsApiTicketCache;
import com.railwayserviceWX.config.WeixinConfig;
import com.railwayserviceWX.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 微信服务器访问器。
 *
 * @author Ewing
 * @date 2017/4/7
 */
public class WechatAccessor {
    private static Logger logger = LoggerFactory.getLogger(WechatAccessor.class);

    /**
     * 获取jsapi_ticket
     *
     * @return String jsapi_ticket
     * @author lid
     * @date 2017.3.21
     */
    public static String getJsApiTicket() {
        logger.info("getJSApiTicket");

        //先从缓存中获取ticket，如果没有则重新获取
        String ticket = JsApiTicketCache.get("jsApiTicket");
        if (StringUtils.hasText(ticket)) {
            logger.info("ticket:" + ticket);
            return ticket;
        }

        //先从缓存获取accessToken
        String accessToken = AccessTokenCache.get("accessToken");
        if (!StringUtils.hasText(accessToken)) {
            //如果缓存中没有accessToken，则重新获取并保存到缓存
            accessToken = WechatAccessor.getAccessToken();
            if (null == accessToken) {
                logger.info("accessToken获取失败");
                return null;
            }
            AccessTokenCache.put("accessToken", accessToken);
        }
        String requestUrl = WeixinConfig.GET_JSAPI_TICKET.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = HttpUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            logger.info("jsonObject:" + jsonObject.toJSONString());
            ticket = jsonObject.getString("ticket");
            JsApiTicketCache.put("jsApiTicket", ticket);
        }
        return ticket;
    }

    /**
     * 获取公众号accessToken
     *
     * @return String
     * @author lid
     * @date 2017.2.24
     */
    public static String getAccessToken() {
        String accessToken = null;
        String requestUrl = WeixinConfig.URL_ACCESSTOKEN.replace("CREDENTIAL", WeixinConfig.GRANTTYPE).replace("APPID", WeixinConfig.APPID).replace("APPSECRET", WeixinConfig.SECRET);

        logger.info("requestUrl:" + requestUrl);

        JSONObject jsonObject = HttpUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            logger.info("jsonObject:" + jsonObject.toJSONString());
            accessToken = jsonObject.getString("access_token");
        }
        return accessToken;
    }

    /**
     * 根据openid获取用户信息
     *
     * @param openid
     * @return
     */
    public static User getUserInfo(String openid) {
        logger.info("getUserInfo,openid:" + openid);
        //保存用户信息
        //先从缓存获取accessToken
        String accessToken = AccessTokenCache.get("accessToken");
        if (!StringUtils.hasText(accessToken)) {
            //如果缓存中没有accessToken，则重新获取并保存到缓存
            accessToken = WechatAccessor.getAccessToken();
            if (null == accessToken) {
                logger.info("accessToken获取失败");
                return null;
            }
            AccessTokenCache.put("accessToken", accessToken);
        }
        logger.info("accessToken:"+accessToken);
        //获取用户信息
        String requestUrl = WeixinConfig.GET_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replaceAll("OPENID", openid);
       
        logger.info("requestUrl:"+requestUrl);
        
        JSONObject jsonObject = HttpUtil.httpsRequest(requestUrl, "GET", null);

        logger.info("jsonObject:"+jsonObject);
        if (null == jsonObject) {
            logger.info("用户信息获取失败");
            return null;
        }

        if (null != jsonObject.getString("errcode")) {
            logger.info("用户信息获取失败");
            return null;
        }

        //设置用户信息
        User user = new User();
        user.setSubscribeType(Integer.valueOf(jsonObject.getString("subscribe")));
        user.setOpenid(jsonObject.getString("openid"));
        user.setNickName(filterEmoji(jsonObject.getString("nickname")));
        user.setGender(Integer.valueOf(jsonObject.getString("sex")));
        user.setLanguage(jsonObject.getString("language"));
        user.setCity(jsonObject.getString("city"));
        user.setProvince(jsonObject.getString("province"));
        user.setCountry(jsonObject.getString("country"));
        user.setHeadimgUrl(jsonObject.getString("headimgurl"));
        user.setSubscribeTime(jsonObject.getString("subscribe_time"));
        user.setGroupId(jsonObject.getString("groupid"));
        if (null != jsonObject.getString("unionid")) {
            user.setUnionid(jsonObject.getString("unionid"));
        }
        return user;
    }

    /**
     * 根据code获取token，进而获得用户基本信息，只适用于网页版授权
     *
     * @param code     换取token值的code值
     * @param fullinfo 是否取用户完整数据的标识
     * @return user
     * @author lid
     */
    public static String getAuthedUserInfo(String code, boolean fullinfo) {
        logger.info("getAuthedUserInfo,code:" + code + ",fullinfo=" + fullinfo);
        //通过code换取网页授权access_token
        String requestUrl = WeixinConfig.WEBPAGE_TOKEN_URL.replace("APPID", WeixinConfig.APPID)
                .replace("SECRET", WeixinConfig.SECRET)
                .replace("CODE", code);
        JSONObject jsonObject = HttpUtil.httpsRequest(requestUrl, "GET", null);
        logger.info("jsonObject" + jsonObject.toJSONString());

        if ((null != jsonObject) && (null == jsonObject.getString("errcode"))) {

            //成功返回json数据包   获取 access_token 和 openid
            String access_token = jsonObject.getString("access_token");

            logger.info("access_token:" + access_token);

            String openid = jsonObject.getString("openid");

            logger.info("openid:" + openid);

            //如果没有获取到openid直接返回空
            if (StringUtils.hasText(openid)) {
                return openid;
            }
        }
        return null;
    }

    /**
     * 发送消息给微信用户，详见微信公众平台相关文档。
     *
     * @param jsonData 填充消息模板的内容。
     * @return 发送结果。
     */
    public static boolean sendMessageToUser(String jsonData) {
        logger.info("sendMessageToUser：jsonData：" + jsonData);
        //先从缓存获取accessToken
        String accessToken = AccessTokenCache.get("accessToken");
        if (!StringUtils.hasText(accessToken)) {
            //如果缓存中没有accessToken，则重新获取并保存到缓存
            accessToken = WechatAccessor.getAccessToken();
            if (null == accessToken) {
                logger.info("获取accessToken失败");
                return false;
            }
            AccessTokenCache.put("accessToken", accessToken);
        }

        //发送消息给微信用户
        String requestUrl = WeixinConfig.WX_MESSAGE_SEND.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = HttpUtil.httpsRequest(requestUrl, "GET", jsonData);

        if (jsonObject != null && 0 == jsonObject.getInteger("errcode")) {
            logger.info("发送消息给微信用户成功！");
            return true;
        } else {
            logger.info("发送消息给微信用户失败：" + (jsonObject == null ? "null" : jsonObject.toJSONString()));
            return false;
        }
    }

    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (!StringUtils.hasText(source)) {
            return false;
        }
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

}
