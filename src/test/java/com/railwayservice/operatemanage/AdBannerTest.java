package com.railwayservice.operatemanage;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.dao.AdminDao;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.operatemanage.dao.AdBannerDao;
import com.railwayservice.operatemanage.entity.AdBanner;
import com.railwayservice.operatemanage.service.AdBannerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @author lidx
 * @date 2017/3/10
 * @describe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class AdBannerTest {
    private AdBannerService adBannerService;
    private AdBannerDao adBannerDao;
    private AdminDao adminDao;

    @Autowired
    public void setAdBannerService(AdBannerService adBannerService) {
        this.adBannerService = adBannerService;
    }

    @Autowired
    public void setAdBannerDao(AdBannerDao adBannerDao) {
        this.adBannerDao = adBannerDao;
    }

    @Autowired
    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    /**
     * 准备测试数据
     */
    public AdBanner createAdBanner() {
        Admin admin = createAdmin();
        AdBanner adBanner = new AdBanner();
        adBanner.setAdType(1);
        adBanner.setContent("秀秀的最爱");
        adBanner.setLinkUrl("www.xiaodianying.com");
        adBanner.setAdWeight(1);
        adBanner.setHits(998l);
        adBanner.setCreateDate(new Date());
        adBanner.setTitle("滴滴");
        adBanner.setDuration(1);
        adBanner.setAdPosition(1);
        adBanner.setAdminId(admin.getAdminId());
        adBanner = adBannerDao.save(adBanner);
        adBanner = adBannerService.updateImage(adBanner.getAdBannerId(), AdBannerTest.class.getResourceAsStream("/images/default.jpg"));
        return adBanner;
    }

    public Admin createAdmin() {
        return adminDao.findOne("admin_super");
    }

    /**
     * 删除测试数据
     */
    public void deleteData(AdBanner adBanner) {
        if (adBanner != null) {
            adBannerService.deleteAdBanner(createAdmin(), adBanner.getAdBannerId());
        }

    }

    /**
     * 添加广告信息测试
     */
    @Test
    public void addAdBannerTest() {
        AdBanner adBanner = new AdBanner();
        adBanner.setAdType(1);
        adBanner.setContent("秀秀的最爱");
        adBanner.setLinkUrl("www.xiaodianying.com");
        adBanner.setAdWeight(1);
        adBanner.setHits(998l);
        adBanner.setCreateDate(new Date());
        adBanner.setTitle("滴滴");
        adBanner.setDuration(1);
        adBanner.setAdPosition(1);
        Admin admin = createAdmin();
        adBanner = adBannerService.addAdBanner(admin, adBanner);
        adBanner = adBannerService.updateImage(adBanner.getAdBannerId(), AdBannerTest.class.getResourceAsStream("/images/default.jpg"));
        Assert.assertTrue(adBannerDao.countByTitle(adBanner.getTitle()) > 0);
        deleteData(adBanner);
    }

    /**
     * 删除广告信息测试
     */
    @Test
    public void deleteAdBannerTest() {
        Admin admin = createAdmin();
        AdBanner adBanner = createAdBanner();
        adBannerService.deleteAdBanner(admin, adBanner.getAdBannerId());
        Assert.assertTrue(adBannerDao.countByTitle(adBanner.getTitle()) == 0);
    }

    /**
     * 修改广告信息测试
     */
    @Test
    public void updateAdBannerTest() {
        Admin admin = createAdmin();
        AdBanner adBanner = createAdBanner();
        adBanner.setTitle("666");
        adBannerService.updateAdBanner(admin, adBanner);
        adBanner = adBannerService.updateImage(adBanner.getAdBannerId(), AdBannerTest.class.getResourceAsStream("/images/default.jpg"));
        Assert.assertTrue(adBannerDao.countByTitle("666") > 0);
        deleteData(adBanner);
    }

    /**
     * 查询广告信息测试
     */
    @Test
    public void queryAdBannerTest() {
        AdBanner adBanner = createAdBanner();
        PageParam param = new PageParam();
        PageData adBannerPage = adBannerService.queryAdBanners(param, adBanner.getTitle());
        Assert.assertTrue(adBannerPage.getTotal() > 0);
        deleteData(adBanner);
    }

}
