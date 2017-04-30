package com.railwayservice.serviceprovider;

import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.serviceprovider.dao.ServiceByProviderRelaDao;
import com.railwayservice.serviceprovider.dao.ServiceProviderDao;
import com.railwayservice.serviceprovider.entity.ServiceByProviderRela;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.service.ServiceProviderService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * 服务人员测试类。
 *
 * @author lid
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class ServiceProviderTest {
    ServiceProvider serviceProvidera = null;
    ServiceProvider serviceProviderb = null;
    ServiceByProviderRela serviceByProviderRela = null;
    private ServiceProviderService serviceProviderService;
    private ServiceProviderDao serviceProviderDao;
    private ServiceByProviderRelaDao serviceByProviderRelaDao;

    @Autowired
    public void setServiceProviderService(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @Autowired
    public void setServiceProviderDao(ServiceProviderDao serviceProviderDao) {
        this.serviceProviderDao = serviceProviderDao;
    }

    @Autowired
    public void setServiceByProviderRelaDao(ServiceByProviderRelaDao serviceByProviderRelaDao) {
        this.serviceByProviderRelaDao = serviceByProviderRelaDao;
    }

    @Before
    public void setUp() {
        serviceProvidera = new ServiceProvider();
        serviceProvidera.setAge(20);
        serviceProvidera.setCreateDate(new Date());
        serviceProvidera.setIdentityCardNo("4301221988012303761");
        serviceProvidera.setName("老王");
        serviceProvidera.setPhoneNo("13316483222");
        serviceProvidera.setAccount("lid");
        serviceProvidera.setPassword("lid");
        serviceProvidera.setStationId("666");
        serviceProvidera = serviceProviderDao.save(serviceProvidera);

        serviceByProviderRela = new ServiceByProviderRela();
        serviceByProviderRela.setServiceProvider(serviceProvidera.getServiceProviderId());
        serviceByProviderRela.setServiceTypeId("1");

        serviceByProviderRelaDao.save(serviceByProviderRela);
    }

    @After
    public void tearDown() {

        if (serviceByProviderRela != null) {
            serviceByProviderRelaDao.delete(serviceByProviderRela);
        }

        if (serviceProvidera != null) {
            serviceProviderDao.delete(serviceProvidera);
        }
        if (serviceProviderb != null) {
            serviceProviderDao.delete(serviceProviderb);
        }

        serviceProvidera = null;
        serviceProviderb = null;
        serviceByProviderRela = null;
    }

    @Test
    public void addServiceProvider() {

        serviceProviderb = serviceProviderService.addServiceProvider(serviceProvidera, null);
        System.out.println(serviceProviderb.getIdentityCardNo());
        Assert.assertTrue(null != serviceProviderb);
    }

    @Test
    public void findServiceProvider() {
        serviceProviderb = serviceProviderDao.findOne(serviceProvidera.getServiceProviderId());
        Assert.assertTrue(null != serviceProviderb);

    }

    @Test
    public void findServiceProviderByName() {
        List<ServiceProvider> serviceProviderList = serviceProviderService.findByName(serviceProvidera.getName());
        Assert.assertTrue(serviceProviderList.size() > 0);

    }

    @Test
    public void updateServiceProvider() {
        serviceProviderb = serviceProviderService.findByServiceProviderId(serviceProvidera.getServiceProviderId());
        serviceProviderb.setAge(28);
        Assert.assertTrue(null != serviceProviderb);

    }

    @Test
    public void serviceProviderLogin(){
        ResultMessage message =serviceProviderService.loginByPhoneNo(serviceProvidera.getAccount(), serviceProvidera.getPassword());
        Assert.assertTrue(message.isSuccess());
    }


}
