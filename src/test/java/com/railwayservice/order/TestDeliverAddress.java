package com.railwayservice.order;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.railwayservice.order.entity.DeliverAddress;
import com.railwayservice.order.service.DeliverAddressService;

/**
 * 送货地址测试类。
 *
 * @author xuy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestDeliverAddress {
	
	@Autowired
	private DeliverAddressService deliverAddressService;
	
	
	public DeliverAddressService getDeliverAddressService() {
		return deliverAddressService;
	}

	public void setDeliverAddressService(DeliverAddressService deliverAddressService) {
		this.deliverAddressService = deliverAddressService;
	}

//	@Before
//    public void setUp() throws Exception {
//        
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        
//    }
    
    @Test
    public void addTest() {
    	DeliverAddress da = new DeliverAddress();
    	da.setAddress("test case addressNo1");
    	da.setStationId("1");
    	
    	DeliverAddress addOne = deliverAddressService.addDeliverAddress(da);
    	
    	da = deliverAddressService.findDeliverAddressById(addOne.getDeliverAddressId());
    	
    	Assert.assertEquals(da.getAddress(), addOne.getAddress());
    	
    	Assert.assertEquals(da.getStationId(), addOne.getStationId());
    	
    	Assert.assertEquals(da.getDeliverAddressId(), addOne.getDeliverAddressId());
    	
    	deliverAddressService.deleteDeliverAddress(addOne);
    	
    }
	

    @Test
    public void findTest() {
    	DeliverAddress da1 = new DeliverAddress();
    	da1.setAddress("test case address");
    	da1.setStationId("testCaseStationId");
    	
    	DeliverAddress da2 = new DeliverAddress();
    	da2.setAddress("test case address");
    	da2.setStationId("testCaseStationId");
    	
    	DeliverAddress a1 = deliverAddressService.addDeliverAddress(da1);
    	DeliverAddress a2 = deliverAddressService.addDeliverAddress(da2);
    	
    	List<DeliverAddress> das = deliverAddressService.findDeliverAddressByStationId("testCaseStationId");
    	
    	Assert.assertEquals(2, das.size());
    	
    	
    	deliverAddressService.deleteDeliverAddress(a1);
    	deliverAddressService.deleteDeliverAddress(a2);
    	
    }
    
}
