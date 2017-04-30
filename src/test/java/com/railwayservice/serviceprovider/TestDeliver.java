package com.railwayservice.serviceprovider;

import com.railwayservice.application.util.RandomString;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.common.dao.ImageInfoDao;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.merchantmanage.dao.MerchantDao;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.order.dao.MainOrderDao;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.service.OrderStatic;
import com.railwayservice.serviceprovider.dao.ServiceProviderDao;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.service.DeliverService;
import com.railwayservice.serviceprovider.vo.CountOrdersVO;
import com.railwayservice.user.dao.UserDao;
import com.railwayservice.user.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 餐饮服务测试类。
 *
 * @author Ewing
 * @date 2017/2/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestDeliver {
    private Logger logger = LoggerFactory.getLogger(TestDeliver.class);

    private DeliverService deliverService;

    private UserDao userDao;
    private MainOrderDao mainOrderDao;
    private MerchantDao merchantDao;
    private ImageInfoDao imageInfoDao;
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    public void setDeliverService(DeliverService deliverService) {
        this.deliverService = deliverService;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setMainOrderDao(MainOrderDao mainOrderDao) {
        this.mainOrderDao = mainOrderDao;
    }

    @Autowired
    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    @Autowired
    public void setImageInfoDao(ImageInfoDao imageInfoDao) {
        this.imageInfoDao = imageInfoDao;
    }

    @Autowired
    public void setServiceProviderDao(ServiceProviderDao serviceProviderDao) {
        this.serviceProviderDao = serviceProviderDao;
    }

    private User createUser() {
        User user = new User();
        user.setName("用户" + RandomString.randomString(5));
        user.setPhoneNo("186" + RandomString.randomNumberString(8));
        return userDao.save(user);
    }

    private ServiceProvider createServiceProvider() {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName("服务员" + RandomString.randomString(5));
        serviceProvider.setPhoneNo("187" + RandomString.randomNumberString(8));
        return serviceProviderDao.save(serviceProvider);
    }

    private ImageInfo createImage() {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setUrl("/images/default.jpg");
        return imageInfoDao.save(imageInfo);
    }

    private Merchant createMerchant(ImageInfo image) {
        Merchant merchant = new Merchant();
        merchant.setName("商户" + RandomString.randomString(5));
        merchant.setPhoneNo("188" + RandomString.randomNumberString(8));
        merchant.setAddress("长沙南站商户");
        merchant.setStationId(RandomString.generate64UUID().replace("-", ""));
        merchant.setImageId(image.getImageId());
        return merchantDao.save(merchant);
    }

    private MainOrder createMainOrder(User user, Merchant merchant, ServiceProvider serviceProvider) {
        MainOrder mainOrder = new MainOrder();
        mainOrder.setUserId(user.getUserId());
        mainOrder.setMerchantId(merchant.getMerchantId());
        mainOrder.setCustomerName(user.getName());
        mainOrder.setCustomerPhoneNo(user.getPhoneNo());
        mainOrder.setDeliverAddress("长沙南站");
        mainOrder.setStationId(merchant.getStationId());
        mainOrder.setDeliverType(OrderStatic.DELIVER_TYPE_SEND);
        mainOrder.setServiceProviderId(serviceProvider.getServiceProviderId());
        mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_COMPLETED);
        mainOrder.setUpdateDate(new Date());
        mainOrder.setLatestServiceTime(new Date(System.currentTimeMillis() + 1000L * 60 * 30));
        return mainOrderDao.save(mainOrder);
    }

    private void cleanTestDate(User user, ServiceProvider serviceProvider, ImageInfo image, Merchant merchant, MainOrder mainOrder) {
        if (user != null) userDao.delete(user);
        if (serviceProvider != null) serviceProviderDao.delete(serviceProvider);
        if (image != null) imageInfoDao.delete(image);
        if (merchant != null) merchantDao.delete(merchant);
        if (mainOrder != null) mainOrderDao.delete(mainOrder);
    }

    @Test
    @Transactional
    public void queryMainOrderTest() {
        // 准备测试数据。
        User user = createUser();
        ImageInfo image = createImage();
        Merchant merchant = createMerchant(image);
        ServiceProvider serviceProvider = createServiceProvider();
        MainOrder mainOrder = createMainOrder(user, merchant, serviceProvider);
        // 开始测试。
        PageData pageData = deliverService.queryOrders(new PageParam(), serviceProvider.getServiceProviderId(), mainOrder.getStationId(), mainOrder.getOrderStatus());
        // 清理测试数据。
        cleanTestDate(user, serviceProvider, image, merchant, mainOrder);
        // 判断测试结果。
        Assert.assertTrue(pageData.getRows().size() > 0);
    }

    @Test
    @Transactional
    public void deliveredGoodsTest() {
        User user = createUser();
        ImageInfo image = createImage();
        Merchant merchant = createMerchant(image);
        ServiceProvider serviceProvider = createServiceProvider();
        MainOrder mainOrder = createMainOrder(user, merchant, serviceProvider);
        deliverService.deliveredGoods(serviceProvider, mainOrder.getOrderId());
        cleanTestDate(user, serviceProvider, image, merchant, mainOrder);
        Assert.assertTrue(true);
    }

    @Test
    @Transactional
    public void countOrdersTodayTest() {
        User user = createUser();
        ImageInfo image = createImage();
        Merchant merchant = createMerchant(image);
        ServiceProvider serviceProvider = createServiceProvider();
        MainOrder mainOrder = createMainOrder(user, merchant, serviceProvider);
        CountOrdersVO countOrdersVO = deliverService.countOrdersToday(serviceProvider.getServiceProviderId());
        cleanTestDate(user, serviceProvider, image, merchant, mainOrder);
        Assert.assertTrue(countOrdersVO.getCompleteOrdersToday() > 0);
    }

}
