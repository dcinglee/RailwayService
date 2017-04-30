package com.railwayservice.serviceprovider;

import com.railwayservice.serviceprovider.dao.ServiceTypeDao;
import com.railwayservice.serviceprovider.entity.ServiceType;
import com.railwayservice.serviceprovider.service.ServiceTypeService;
import com.railwayservice.serviceprovider.service.ServiceTypeStatic;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * 服务管理测试类。
 *
 * @author lixs
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestService {

    ServiceType serviceTypea = null;
    ServiceType serviceTypeb = null;
    private ServiceTypeService serviceTypeService;
    private ServiceTypeDao serviceTypeDao;

    @Autowired
    public void setServiceTypeDao(ServiceTypeDao serviceTypeDao) {
        this.serviceTypeDao = serviceTypeDao;
    }

    @Autowired
    public void setServiceTypeService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @Before
    public void setUp() throws Exception {
        serviceTypea = new ServiceType();
        serviceTypea.setName("麦当劳");
        serviceTypea.setDistributionCosts(BigDecimal.valueOf(10.03));
        serviceTypea.setStatus(ServiceTypeStatic.SERVICE_TYPE_NORMAL);
//        serviceTypea.setStatus(ServiceTypeStatic.SERVICEPROVIDER_NORMAL);
        serviceTypea.setIntroduction("www");
        serviceTypeDao.save(serviceTypea);

    }

    @After
    public void tearDown() throws Exception {
        serviceTypeDao.delete(serviceTypea);
        serviceTypea = null;

    }

    /**
     * 服务查询测试。
     *
     * @author lixs
     */
    @Test
    public void testFind() {
        ServiceType serviceType = serviceTypeService.findServiceType(serviceTypea.getTypeId());
        Assert.assertTrue(serviceType != null);
    }

    /**
     * 服务更新测试。
     *
     * @author lixs
     */
    @Test
    public void testUpdate() {
        serviceTypea = serviceTypeService.updateServiceType(serviceTypea);
        Assert.assertTrue(serviceTypea != null);
    }

    /**
     * 服务添加测试
     */
    @Test
    public void testAdd() {
        serviceTypeb = new ServiceType();
        serviceTypeb.setTypeId("110");
        serviceTypeb.setName("华莱士");
        serviceTypeb.setIntroduction("尼玛坑爹啊");
        ServiceType serviceType = serviceTypeService.addServiceType(serviceTypeb);
        Assert.assertTrue(!serviceType.getName().equals(null));
    }

    /**
     * 服务删除测试
     */
    @Test
    public void testDelete() {
        serviceTypeService.deleteServiceType(serviceTypea.getTypeId());
        Assert.assertTrue(serviceTypeDao.countByName("麦当劳") == 0);
    }
}