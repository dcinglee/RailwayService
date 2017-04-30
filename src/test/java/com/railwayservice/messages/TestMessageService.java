package com.railwayservice.messages;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.railwayservice.application.util.TimeUtil;
import com.railwayservice.common.service.CommonStatic;
import com.railwayservice.messages.dao.ChannelInfoDao;
import com.railwayservice.messages.entity.ChannelInfo;
import com.railwayservice.messages.service.ChannelConst;
import com.railwayservice.messages.service.ChannelInfoService;
import com.railwayservice.serviceprovider.dao.ServiceByProviderRelaDao;
import com.railwayservice.serviceprovider.dao.ServiceProviderDao;
import com.railwayservice.serviceprovider.dao.ServiceTypeDao;
import com.railwayservice.serviceprovider.entity.ServiceByProviderRela;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.entity.ServiceType;
import com.railwayservice.serviceprovider.service.ServiceProviderStatic;
import com.railwayservice.serviceprovider.service.ServiceTypeStatic;
import com.railwayservice.stationmanage.dao.RailwayStationDao;
import com.railwayservice.stationmanage.entity.RailwayStation;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:common.properties")
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestMessageService {
	
	private Logger logger = LoggerFactory.getLogger(TestMessageService.class);

	@Autowired
    private ChannelInfoService channelInfoService;

	@Value("${heartBeatInteral}")
	private long heartBeatInterval;

	@Autowired
	private ChannelInfoDao channelInfoDao;
	@Autowired
	private RailwayStationDao railwayStationDao;
	@Autowired
	private ServiceTypeDao serviceTypeDao;
	@Autowired
	private ServiceProviderDao serviceProviderDao;
	@Autowired
	private ServiceByProviderRelaDao serviceByProviderRelaDao;
	
	
	
	public ServiceByProviderRelaDao getServiceByProviderRelaDao() {
		return serviceByProviderRelaDao;
	}

	public void setServiceByProviderRelaDao(ServiceByProviderRelaDao serviceByProviderRelaDao) {
		this.serviceByProviderRelaDao = serviceByProviderRelaDao;
	}

	public ServiceProviderDao getServiceProviderDao() {
		return serviceProviderDao;
	}

	public void setServiceProviderDao(ServiceProviderDao serviceProviderDao) {
		this.serviceProviderDao = serviceProviderDao;
	}

	public ServiceTypeDao getServiceTypeDao() {
		return serviceTypeDao;
	}

	public void setServiceTypeDao(ServiceTypeDao serviceTypeDao) {
		this.serviceTypeDao = serviceTypeDao;
	}
	
	public ChannelInfoDao getChannelInfoDao() {
		return channelInfoDao;
	}

	public void setChannelInfoDao(ChannelInfoDao channelInfoDao) {
		this.channelInfoDao = channelInfoDao;
	}
	
	
	public RailwayStationDao getRailwayStationDao() {
		return railwayStationDao;
	}

	public void setRailwayStationDao(RailwayStationDao railwayStationDao) {
		this.railwayStationDao = railwayStationDao;
	}


	private ChannelInfo info,info1;
	private RailwayStation station;
	private ServiceType serviceType;
	private ServiceProvider serviceProvidera;
	private ServiceByProviderRela serviceByProviderRela;
	
	
	@Before
	public void setup() {
			
		Long outTime = ( TimeUtil.getTimestamp() - heartBeatInterval*60*1000)-2;
		Long inTime = ( TimeUtil.getTimestamp() - (heartBeatInterval-1)*60*1000);
			
			
			//建一个测试车站
			
			station = new RailwayStation();
			station.setStationName("原形科技DEMO");
			station.setStationNameAbbr("YXKJ");
			station.setProvince("长沙");
			station.setCity("长沙市");
			station.setStatus(1000);
			station.setLongitude(66.6);
			station.setLatitude(88.8);
			station.setCreateDate(new Date());
			station = railwayStationDao.save(station);
	        
			//建一个服务类型
			serviceType = new ServiceType();
			serviceType.setName("testDemo");
			serviceType.setTypeId("testDemo1");
			serviceType.setDistributionCosts(new BigDecimal("3.3"));
			serviceType.setStatus(ServiceTypeStatic.SERVICE_TYPE_NORMAL);
			serviceType = serviceTypeDao.save(serviceType);
			
			//建一个测试服务人员 和 服务类型的关系
			serviceProvidera = new ServiceProvider();
	        serviceProvidera.setAge(20);
	        serviceProvidera.setCreateDate(new Date());
	        serviceProvidera.setIdentityCardNo("4301221988012303761");
	        serviceProvidera.setName("老王");
	        serviceProvidera.setPhoneNo("13316483222");
	        serviceProvidera.setAccount("lid");
	        serviceProvidera.setPassword("lid");
	        serviceProvidera.setStationId(station.getStationId());
	        serviceProvidera.setStatus(ServiceProviderStatic.SERVICEPROVIDER_NORMAL);
	        serviceProvidera = serviceProviderDao.save(serviceProvidera);

	        serviceByProviderRela = new ServiceByProviderRela();
	        serviceByProviderRela.setServiceProvider(serviceProvidera.getServiceProviderId());
	        serviceByProviderRela.setServiceTypeId(serviceType.getTypeId());

	        serviceByProviderRelaDao.save(serviceByProviderRela);
	        
			//建一个channelInfo
	        
	      //10分钟以外的
			info = new ChannelInfo();
			
			info.setChannelId("testChannelId1");
			info.setUserId(serviceProvidera.getServiceProviderId());
			info.setLastTime(outTime);
			info.setDeviceIdNo("dd");
			info.setStatus(ChannelConst.CHANNEL_DEVICE_WORK);;

			channelInfoDao.save(info);
			
			//10分钟以内的
			info1= new ChannelInfo();
//			info1.setChannelInfoId(null);
//			info1 = new ChannelInfo();
			info1.setChannelId("testChannelId2");
			info1.setUserId(serviceProvidera.getServiceProviderId());
			info1.setLastTime(inTime);
			info.setDeviceIdNo("dd3");
			info1.setStatus(ChannelConst.CHANNEL_DEVICE_WORK);;

			channelInfoDao.save(info1);
			
	    }

	    @After
	    public void tearDown() {
	    	if(info != null){
	    		channelInfoDao.delete(info);
	    	}
	    	if(info1 != null){
	    		channelInfoDao.delete(info1);
	    	}
	    	if(serviceByProviderRela != null ) serviceByProviderRelaDao.delete(serviceByProviderRela);
	    	if(serviceProvidera != null ) serviceProviderDao.delete(serviceProvidera);
	    	if(serviceType != null ) serviceTypeDao.delete(serviceType);
	    	if(station != null ) railwayStationDao.delete(station);
	    	
	    }	

    public ChannelInfoService getChannelInfoService() {
		return channelInfoService;
	}


	public void setChannelInfoService(ChannelInfoService channelInfoService) {
		this.channelInfoService = channelInfoService;
	}


	@Test
    public void updateTest() {
		
		
		ChannelInfo info = channelInfoService.updateChannelInfo("user", "channelId3", CommonStatic.ANDROID, ChannelConst.CHANNEL_DEVICE_WORK);
        Assert.assertNotNull(info);
		
        Assert.assertEquals(info.getStatus().intValue(), ChannelConst.CHANNEL_DEVICE_WORK);
        
//        info = channelInfoService.updateChannelInfo("userA", "channelId2", CommonStatic.ANDROID, ServiceProviderStatic.SERVICEPROVIDER_REST);
//        System.out.println("TimeUtil.getTimestamp()="+ TimeUtil.getTimestamp());
//        
//        
//        List<ChannelInfo> result = channelInfoService.getOnlineWorkChannelInfoByUserId("userA");
//        
//        Assert.assertEquals(result.size(), 1);
//        Assert.assertEquals(result.get(0).getChannelId(), "channelId1");
//        
//        result = channelInfoService.getChannelInfoByUserIdAndStatus("userA",ServiceProviderStatic.SERVICEPROVIDER_REST);
//        
//        Assert.assertEquals(result.size(), 1);
//        Assert.assertEquals(result.get(0).getChannelId(), "channelId2");
//        Assert.assertTrue(StringUtils.hasText(merchant.getMerchantId()));
    }
	
	
	@Test
    public void getOnlineChannelTest() {
		
        List<ChannelInfo> result = channelInfoService.getOnlineWorkChannelInfoByUserId(info1.getUserId());
        
        Assert.assertEquals(result.size(), 1);
        
        List<ChannelInfo> list = channelInfoService.getOnlineWorkServiceProviderInfoByStationIdAndServiceType(station.getStationId(),serviceType.getTypeId());
        Assert.assertEquals(list.size(), 1);
        
//        
//        list = channelInfoService.getOnlineWorkMerchantInfoByStationIdAndServiceType("3",serviceType.getTypeId());
//        Assert.assertEquals(list.size(), 1);
        
    }
	
	
//	@Test
//    public void getOnlineOverTimeTest() {
//		
//		Date date = new Date(1489052829522L);
//		System.out.println("date1489052829522L="+date);
//		
//        
//        List<ChannelInfo> result = channelInfoService.getOnlineWorkChannelInfoByUserId("userC");
//        Assert.assertNull(result);
//    }
	
	
}
