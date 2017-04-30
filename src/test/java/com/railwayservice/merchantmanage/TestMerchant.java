package com.railwayservice.merchantmanage;

import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.service.AdminService;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.merchantmanage.service.MerchantService;
import com.railwayservice.merchantmanage.service.MerchantmenService;
import com.railwayservice.merchantmanage.vo.MerchantAchievement;
import com.railwayservice.merchantmanage.vo.MerchantMainOrder;
import com.railwayservice.merchantmanage.vo.Merchantmen;
import com.railwayservice.order.service.OrderStatic;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 商户管理服务测试类。
 *
 * @author Ewing
 * @date 2017/2/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestMerchant {
    private Logger logger = LoggerFactory.getLogger(TestMerchant.class);

    private MerchantService merchantService;
    private MerchantmenService merchantmenService;
    private AdminService adminService;

    @Autowired
    public void setMerchantService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setMerchantmenService(MerchantmenService merchantmenService) {
        this.merchantmenService = merchantmenService;
    }

    private Admin admin = new Admin();

    public Merchant createTestData() {
        // 准备测试数据。
        Merchant merchant = new Merchant();
        merchant.setName("星巴克322");
        merchant.setAccount("Starbucks");
        merchant.setPassword("123");
        merchant.setLinkman("星星");
        merchant.setPhoneNo("18888888888");
        merchant.setAddress("深圳火车站");
        merchant.setStationId("10");
        return merchantService.addMerchant(admin, merchant);
    }

    public void deleteTestData(Merchant merchant) {
        // 删除测试数据。
        merchantService.deleteMerchant(admin, merchant.getMerchantId());
    }

    @Test
    public void addMerchantTest() {
        admin = adminService.findByAccount("test1");
        Merchant merchant = new Merchant();
        merchant.setName("星巴克322");
        merchant.setAccount("Starbucks");
        merchant.setPassword("123");
        merchant.setLinkman("星星");
        merchant.setPhoneNo("18888888888");
        merchant.setAddress("深圳火车站");
        merchant = merchantService.addMerchant(admin, merchant);
        // 删除测试数据。
        deleteTestData(merchant);
        Assert.assertTrue(StringUtils.hasText(merchant.getMerchantId()));
    }

    @Test
    public void updateMerchantTest() {
        admin = adminService.findByAccount("test1");
        // 准备测试数据。
        Merchant merchant = createTestData();
        // 开始测试。
        String name = "星巴克295";
        merchant.setName(name);
        merchant.setPassword("12345");
        merchant.setLinkman("星星2");
        merchant.setPhoneNo("18888889999");
        merchant.setAddress("深圳火车站2");
        merchant = merchantService.updateMerchant(admin, merchant);
        Assert.assertTrue(name.equals(merchant.getName()));
        // 删除测试数据。
        deleteTestData(merchant);
    }

    @Test
    public void queryMerchantTest() {
        // 准备测试数据。
        Merchant merchant = createTestData();
        // 开始测试。
        Pageable pageable = new PageRequest(0, 10);
        Page merchants = merchantService.queryMerchants(admin, merchant.getName(), merchant.getPhoneNo(), pageable);
        Assert.assertTrue(merchants.getTotalElements() > 0);
        deleteTestData(merchant);
    }

    @Test
    public void deleteMerchantTest() {
        // 准备测试数据。
        Merchant merchant = createTestData();
        merchantService.deleteMerchant(admin, merchant.getMerchantId());
        Merchant merchantResult = merchantService.findMerchantById(merchant.getMerchantId());
        Assert.assertNull(merchantResult);
    }

    @Test
    public void queryWaitOrdersTest() {
        Merchant merchant = merchantService.findMerchantById("1");
        List<MerchantMainOrder> mainOrders = merchantmenService.queryWaitDealOrders(merchant);
        Assert.assertTrue(mainOrders.size() > 0);
    }

    @Test
    public void queryHistoryOrdersTest() {
        Merchant merchant = merchantService.findMerchantById("8a9d4f085af0808c015af39f8cb70006");
        List<MerchantMainOrder> mainOrders = merchantmenService.queryHistoryOrders(merchant,
                new Date(System.currentTimeMillis() - 1000L * 3600 * 24 * 30), new Date(), null);
        Assert.assertTrue(mainOrders.size() > 0);
    }

    @Test
    public void achievementTest() {
        Merchant merchant = merchantService.findMerchantById("8a9d4f085af0808c015af39f8cb70006");
        MerchantAchievement achievement = merchantmenService.achievement(new PageParam(), merchant);
        Assert.assertTrue(achievement.getDailyAchievement().getRows().size() > 0);
    }

    @Test
    public void getMerchantInfoTest() {
        Merchant merchant = merchantService.findMerchantById("1");
        Merchantmen merchantmen = merchantmenService.getMerchantInfo(merchant);
        Assert.assertTrue(merchantmen != null);
    }
}
