package com.railwayserviceWX.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.railwayserviceWX.config.WeixinConfig;
import com.railwayserviceWX.controller.WechatAccessor;
import com.railwayserviceWX.exception.WeixinException;

/**
 * 菜单工具类
 * 提供创建、删除、查询菜单
 *
 * @author lid
 * @date 2017年2月23日
 */
public class MenuUtil {
    /**
     * 创建菜单
     *
     * @param json
     * @throws WeixinException
     */
    public static void create(String json) throws WeixinException {
        String url = WeixinConfig.URL_MENU_CREATE + "?access_token=" + WechatAccessor.getAccessToken();
        String result = HttpUtil.sendHttpsPOST(url, json);
        JSONObject obj = JSON.parseObject(result);
        int errcode = obj.getIntValue("errcode");
        if (errcode > 0) {
            throw new WeixinException(result);
        }
    }
}
