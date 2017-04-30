package com.railwayservice.order;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.railwayservice.application.util.TimeUtil;
import com.railwayservice.order.service.FinanceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestFinance {

//	@Autowired
//	private FinanceDao financeDao;
	
	@Autowired
	private FinanceService financeService;


	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}
	
	
	@Test
    public void testGetMerchantOrders() {
		Date d1 = TimeUtil.convert2Date("20170201","yyyyMMdd");
		System.out.println("11="+TimeUtil.convert2String(d1, "yyyyMMdd HH24"));
		Date d2 = TimeUtil.convert2Date("20170328","yyyyMMdd");
//				new Date();
		List result = financeService.getMerchantOrders(null, null, d1, d2);
		System.out.println("==ok");
		
	}
	
	
}
