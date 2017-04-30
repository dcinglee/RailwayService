package com.railwayservice.messages.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.railwayservice.messages.entity.ChannelInfo;

public interface ChannelInfoDao extends JpaRepository<ChannelInfo, String>,JpaSpecificationExecutor<ChannelInfo>{


	//@Query("select m.channelInfo from ChannelInfo m where m.userId= ?1 and m.lastTime > ?2")
	
	@Query("select c from ChannelInfo c,ServiceProvider p,ServiceByProviderRela r"+ 
	" where c.userId = p.serviceProviderId"+
	" and p.status = :serviceProviderStatus"+
	" and p.serviceProviderId = r.serviceProvider"+
	" and p.stationId = :stationId and c.status = :status and c.lastTime > :lastTime and r.serviceTypeId = :serviceType ")
	public List<ChannelInfo> getOnlineWorkServiceProviderChannelInfo(@Param("stationId") String stationId, @Param("serviceType") String serviceType,@Param("serviceProviderStatus") Integer serviceProviderStatus, @Param("status") Integer status, @Param("lastTime")Long lastTime);
	
	
	@Query("select c from ChannelInfo c,Merchant m"+ 
			" where c.userId = m.merchantId"+
			" and m.status = :merchantStatus"+			
			" and m.stationId = :stationId and c.status = :status and c.lastTime > :lastTime and m.serviceTypeId = :serviceType ")
			public List<ChannelInfo> getOnlineWorkMerchantChannelInfo(@Param("stationId") String stationId, @Param("serviceType") String serviceType,@Param("merchantStatus") Integer merchantStatus, @Param("status") Integer status, @Param("lastTime")Long lastTime);
			
	
	@Query("select c from ChannelInfo c,Merchant m"+ 
			" where c.userId = m.merchantId"+
			" and m.status = :merchantStatus"+			
			" and m.merchantId = :merchantId and c.status = :status and c.lastTime > :lastTime and m.serviceTypeId = :serviceType ")
			public List<ChannelInfo> getOnlineWorkMerchantChannelInfoByMerchantId(@Param("merchantId") String merchantId, @Param("merchantStatus") Integer merchantStatus, @Param("status") Integer status, @Param("lastTime")Long lastTime);
		
	
//	/**
//     * 查询管理员用户（运营管理员）
//     *
//     * @param roleId
//     * @return 成功查到的管理员集合
//     */
//    @Query("select arr.admin from AdminRoleRela arr where arr.admin.name like :adminName and arr.role.roleId = :roleId")
//    List<Admin> findByRoleId(@Param("adminName") String adminName, @Param("roleId") String roleId);
	
	
	public ChannelInfo findByUserIdAndChannelId(String userId,String channelId);
	
	
	public List<ChannelInfo> findByUserIdAndStatus(String userId,Integer status);
	
	
	
//	/**
//	 * 更新在线时间
//	 * @param userId
//	 * @param channelId
//	 * @param lastTime
//	 */
//    @Modifying
//    @Query("update ChannelInfo m set m.lastTime = :lastTime and m.channelId = :channelId and m.status = :status" +
//            " and m.status = :status and m.deviceType = :deviceType " +
//            "  where m.userId=:userId and m.channelId = :channelId")
//    void updateChannelInfo(@Param("userId") String userId,@Param("channelId") String channelId, @Param("lastTime") Long lastTime,
//    		@Param("status") Integer status, @Param("deviceType") String deviceType);
	
    
    
}
