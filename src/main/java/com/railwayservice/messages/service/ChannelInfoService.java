package com.railwayservice.messages.service;

import java.util.List;

import com.railwayservice.messages.entity.ChannelInfo;

/**
 * 获取在线设备信息服务类
 * 
 * 
 * 
 * @author xuyu
 *
 */
public interface ChannelInfoService {

	
	/**
	 * 更新在线设备类型表
	 * @param userId
	 * @param channelId
	 * @param deviceType
	 * @param status
	 */
	public ChannelInfo updateChannelInfo(String userId,String channelId,String deviceType,Integer status);
	
	/**
	 * 更新在线设备类型，并且根据在线类型设置同步Merchant和ServiceProvider表的status字段
	 * @param userId
	 * @param channelId
	 * @param deviceType
	 * @param status
	 * @return
	 */
	 public ChannelInfo updateChannelInfoAndMerchantServiceStatus(String userId, String channelId, String deviceType, Integer status);
	
//	/***
//	 * 通过用户ID和CHANNEL ID获取指定channelInfo
//	 * @param userId
//	 * @param channelId
//	 * @return
//	 */
//	public ChannelInfo getChannelInfoByUserIdAndChannelId(String userId,String channelId);
	
	
	/**
	 * 根据userId和status取得设备信息（包含服务人员和商家）
	 * @param userId serviceProviderId或者merchantId
	 * @param status 
	 * @return
	 */
//	public List<ChannelInfo> getChannelInfoByUserIdAndStatus(String userId,Integer status);
	
	
	
	/**
	 * 取得在线的用户信息（包含服务人员和商家）
	 * @param userId serviceProviderId或者merchantId
	 * @return
	 */
	public List<ChannelInfo> getOnlineWorkChannelInfoByUserId(String userId);
	
	
	
	/**
	 * 获取指定车站的指定服务类型的在线工作状态的服务人员
	 * @param stationId
	 * @param serviceType
	 * @return
	 */
	public List<ChannelInfo> getOnlineWorkServiceProviderInfoByStationIdAndServiceType(String stationId,String serviceType);
	
	
	/**
	 * 获取指定车站的指定服务类型的在线工作状态的商家
	 * @param stationId
	 * @param serviceType
	 * @return
	 */
	public List<ChannelInfo> getOnlineWorkMerchantInfoByStationIdAndServiceType(String stationId,String serviceType);
	
	
	/**
	 * 推送消息到指定的设备
	 * @param channelInfos 需要接受消息的设备列表
	 * @param title 消息标题
	 * @param description  发送给设备的信息
	 */
	public void pushMessageToBatch4ServiceProvider(List<ChannelInfo> channelInfos, String title,String description);
	
	public void pushMessageToBatch4Merchant(List<ChannelInfo> channelInfos, String title,String description);

    void pushNoticeToServiceProvider(String serviceProviderId, String title, String content);

    void pushNoticeToStationServiceProvider(String stationId, String serviceTypeId, String title, String content);
    
    /**
	 * 判断当前是否有服务人员在线
	 * @param stationId		所属车站
	 * @param serviceType	服务类型
	 * @return
	 */
	public boolean hasServiceProviderOnline(String stationId,String serviceType);
	
	
	/**
	 * 判断商户是否在线
	 * @param merchantId
	 * @return
	 */
	public boolean hasMerchantOnline(String merchantId);
	
	/**
	 * 判断商家是否在线
	 * @param merchantId
	 * @return
	 */
    public List<ChannelInfo> getOnlineWorkMerchantChannelInfoByMerchantId(String merchantId);

}
