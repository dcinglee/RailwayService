package com.railwayserviceWX.util;

import java.util.HashMap;
import java.util.Map;

import com.railwayserviceWX.controller.WechatAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.railwayserviceWX.config.WeixinConfig;

/**
 * 微信JSSDK工具的支持类
 * @author lid
 * @date 2017.3.21
 *
 */
public class WXJSSDKSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(WXJSSDKSupport.class);
	
	private static final String title = "以为科技";
	
	private static final String desc = "高铁点餐、充电宝、站内购物一键搞定！";
	
	private static final String imgUrl = WeixinConfig.HOST + "/images/logo.jpg";
	
	/**
	 * 生成微信端JS-SDK工具接入所需要的校验信息
	 * @param url 请求路径
	 * @return Map
	 * 		以键值对的形式返回
	 */
	public static Map<String,String> getValidateInfo(String url){
		//时间戳
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
		
        //随机字符串
		String nonceStr=String.valueOf(Math.random());
		
		//获取jsapi
		String jsapi_ticket = WechatAccessor.getJsApiTicket();
		
		if(StringUtils.hasText(jsapi_ticket)){
			String []array = {"timestamp=".concat(timeStamp), "noncestr=".concat(nonceStr), 
					"jsapi_ticket=".concat(jsapi_ticket), "url=".concat(url)};
			
			SignUtil.sort(array);
			String stringA = new String() ;
			
			for(int i = 0 ; i < array.length - 1 ; i++)
			{
				stringA = stringA.concat(array[i].concat("&"));
			}
			stringA = stringA.concat(array[array.length - 1]);
			
			logger.info("stringA:"+stringA);
			
			//sha1加密
			MySecurity mySecurity = new MySecurity();
			String signature = mySecurity.encode(stringA, "SHA-1");
			logger.info("signature:"+signature);
			Map<String,String> validinfo = new HashMap<String,String>();
			validinfo.put("title", title);
			validinfo.put("desc", desc);
			validinfo.put("link", url);
			validinfo.put("imgUrl",imgUrl);
			validinfo.put("noncestr", nonceStr);
			validinfo.put("timestamp", timeStamp);
			validinfo.put("jsSignature", signature);
			validinfo.put("appid",WeixinConfig.APPID);
			return validinfo;
		}
		return null;
	}
}
