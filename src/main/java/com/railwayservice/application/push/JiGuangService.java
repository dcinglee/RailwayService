package com.railwayservice.application.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class JiGuangService implements PushService {

	private final Logger logger = LoggerFactory.getLogger(JiGuangService.class);
	
	private static final String PROVIDER_APP_KEY="e8ace2909d22189414f68623";
	
	private static final String PROVIDER_MASTER_SECRET="e4c63096b578fddc9a3a4252";
	
	
	private static final String MERCHANT_APP_KEY="9746bb8b4324fb2fbf263ac6";
	
	private static final String MERCHANT_MASTER_SECRET="a689ef46d6c4bfc439338b97";
	
	
	private static JPushClient MerchantJpushClient = null;
	
	private static JPushClient ServicerJpushClient = null;
	
	/* 
     * 保存离线的时长。秒为单位。最多支持10天（864000秒）。 
     * 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。 
     * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒 
     */  
    private static long timeToLive =  60 * 60 * 24;  
    
	@Override
	public boolean pushSms4Merchant(String id, String title, String msg) {
		if( MerchantJpushClient == null){
			MerchantJpushClient = new JPushClient(MERCHANT_MASTER_SECRET, MERCHANT_APP_KEY);
//			MerchantJpushClient = new JPushClient(MERCHANT_MASTER_SECRET, MERCHANT_APP_KEY, null, ClientConfig.getInstance());
		}
		
		
		if(logger.isInfoEnabled()){
    		logger.info("调用jpush for merchant 开始发送消息:id="+id+",title="+title+",msg="+msg);
    	}
		
		// For push, all you need do is to build PushPayload object.
	    PushPayload payload = buildPushObject(id,msg,title);

	    PushResult result = null;
	    try {
	         result = MerchantJpushClient.sendPush(payload);
	        logger.info("Got result - " + result);

	    } catch (APIConnectionException e) {
	        // Connection error, should retry later
	    	logger.error("Connection error, should retry later", e);
	    	return false;
	    } catch (APIRequestException e) {
	        // Should review the error, and fix the request
	    	logger.error("Should review the error, and fix the request", e);
	    	logger.info("HTTP Status: " + e.getStatus());
	    	logger.info("Error Code: " + e.getErrorCode());
	    	logger.info("Error Message: " + e.getErrorMessage());
	    	return false;
	    }
	    
	    System.out.println("发送极光消息结果 :"+result);
		return true;
	}
	
	public static PushPayload buildPushObject(String id, String ALERT,String TITLE) {
		return PushPayload.newBuilder()
                .setPlatform(Platform.android())
//                .setAudience(Audience.tag("tag1"))
                .setAudience(Audience.registrationId(id))
                .setNotification(Notification.android(ALERT, TITLE, null))
                .build();
    }

	@Override
	public boolean pushSms4ServiceProvider(String id, String title, String msg) {
		if( ServicerJpushClient == null){
//			ServicerJpushClient = new JPushClient(PROVIDER_MASTER_SECRET, PROVIDER_APP_KEY, null, ClientConfig.getInstance());
			ServicerJpushClient = new JPushClient(PROVIDER_MASTER_SECRET, PROVIDER_APP_KEY);
		}
		
		
		if(logger.isInfoEnabled()){
    		logger.info("调用jpush for servicer开始发送消息:id="+id+",title="+title+",msg="+msg);
    	}
		// For push, all you need do is to build PushPayload object.
	    PushPayload payload = buildPushObject(id,msg,title);

	    PushResult result = null;
	    try {
	        result = ServicerJpushClient.sendPush(payload);
	        logger.info("Got result - " + result);

	    } catch (APIConnectionException e) {
	        // Connection error, should retry later
	    	logger.error("Connection error, should retry later", e);
	    	return false;
	    } catch (APIRequestException e) {
	        // Should review the error, and fix the request
	    	logger.error("Should review the error, and fix the request", e);
	    	logger.info("HTTP Status: " + e.getStatus());
	    	logger.info("Error Code: " + e.getErrorCode());
	    	logger.info("Error Message: " + e.getErrorMessage());
	    	return false;
	    }
	    
	    System.out.println("发送极光消息结果 :"+result);
		return true;
	}

}
