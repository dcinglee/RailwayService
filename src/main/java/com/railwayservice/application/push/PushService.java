package com.railwayservice.application.push;

public interface PushService {
	
	/**
	 * 推送消息到指定商户
	 * @param id	标识符（在不同的服务当中的标识符含义不同，在百度云推送中是channelId，在极光推送中可能是注册id）
	 * @param title
	 * @param msg
	 * @return
	 */
	public boolean pushSms4Merchant(String id,String title,String msg);
	
	/**
	 * 推送消息到指定服务人员
	 * @param id
	 * @param title
	 * @param msg
	 * @return
	 */
	public boolean pushSms4ServiceProvider(String id,String title,String msg);
}
