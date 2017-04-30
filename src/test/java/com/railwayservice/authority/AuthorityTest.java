package com.railwayservice.authority;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.dao.AdminDao;
import com.railwayservice.authority.dao.AuthorityDao;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.service.AuthorityService;
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
 * 权限测试类。
 *
 * @author lid
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class AuthorityTest {
    private AuthorityService authorityService;
    private AuthorityDao authorityDao;
    private AdminDao adminDao;
    private Authority authority = null;
    private Admin admin = null;

    @Autowired
    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Autowired
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Autowired
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    @Before
    public void setup() {
        authority = new Authority();
        authority.setDescription("lidu测试权限");
        authority.setName("lidu测试权限");
        authority.setCreateDate(new Date());
        authority.setMenuUrl("www.baidu.com");
        authority.setCode("WEB_USER_MERCHANT_MANAGE");
        authority.setOrderNo(100);
        authority.setStatus(0);
        authorityDao.save(authority);

        List<Admin> listAdmin = adminDao.findAll();
        if (0 < listAdmin.size()) {
            admin = listAdmin.get(0);
        }
    }

    @After
    public void tearDown() {
        if (null != authority) {
            authorityDao.delete(authority);
        }
        authority = null;
    }

    @Test
    public void testGetAuthoritys() {
        List<Authority> list = authorityService.getAllAuthority();
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testFindAuthorityById() {
        Authority newAuthority = authorityService.findAuthorityById(authority.getAuthorityId());
        Assert.assertTrue(null != newAuthority);
    }

    @Test
    public void updateAuthority() {
        authority.setName("lidu测试权限update");
        authorityService.updateAuthority(authority);
        Authority au = authorityService.findAuthorityById(authority.getAuthorityId());
        Assert.assertTrue(au.getName().equals(authority.getName()));
    }

    @Test
    public void findByAdminId() {
        List<Authority> list = authorityService.findAuthorityByAdminId(admin.getAdminId());
        System.out.println("list.size():" + list.size());
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void findAuthorityInfo() throws InterruptedException {
        PageData pageData = authorityService.queryAuthorityPage(new PageParam(0, 10, true), null, null);
        Assert.assertTrue(pageData.getTotal() > 0);
    }
}
