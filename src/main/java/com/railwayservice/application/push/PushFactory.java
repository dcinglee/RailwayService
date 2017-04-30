package com.railwayservice.application.push;

public class PushFactory {
	
	
	
	public static PushService getPushService(int type){
		if( PushServiceType.JIGUANG == type){
			return new JiGuangService();
		}else if( PushServiceType.BAIDU == type ){
			return new BiaDuService();
		}
		return null;
	}
}
