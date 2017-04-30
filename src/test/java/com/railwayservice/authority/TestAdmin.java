package com.railwayservice.authority;

import com.railwayservice.application.util.EncodeUtil;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.dao.AdminDao;
import com.railwayservice.authority.dao.AdminRoleRelaDao;
import com.railwayservice.authority.dao.RoleDao;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.entity.AdminRoleRela;
import com.railwayservice.authority.entity.Role;
import com.railwayservice.authority.service.AdminService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * 管理员测试类。
 *
 * @author Ewing
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestAdmin {

    private AdminService adminService;
    private AdminDao adminDao;
    private AdminRoleRelaDao adminRoleRelaDao;
    private RoleDao roleDao;
    private Admin admin;
    private Admin cAdmin;
    private AdminRoleRela adminRoleRela;
    private Role role;

    @Autowired
    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Autowired
    public void setOperateManageService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setAdminRoleRelaDao(AdminRoleRelaDao adminRoleRelaDao) {
        this.adminRoleRelaDao = adminRoleRelaDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Before
    public void setUp() {
        admin = new Admin();
        admin.setName("123");
        admin.setAccount("123");

        admin.setPassword("123");
        admin.setBelongId("2");

        cAdmin = new Admin();
        cAdmin.setBelongId(admin.getBelongId());

        role = new Role();
        role.setName("testCaseRole1");
        role.setDescription("单元测试role1");
        role.setCreateDate(new Date());
        role = roleDao.save(role);

        admin = adminService.addAdmin(cAdmin, admin, new String[]{role.getRoleId()});
    }

    @After
    public void tearDown() {
        if (admin != null) {
            adminService.deleteAdmin(cAdmin, admin);
            admin = null;
        }
        if (role != null) {
            roleDao.delete(role);
            role = null;
        }
    }

    /**
     * 登陆测试
     */
    @Test
    public void testLogin() {
        String account = admin.getAccount();
        String password = "123";
        ResultMessage resultMessage = adminService.loginByAccount(account, password);
        Assert.assertTrue(resultMessage.isSuccess());
    }

    /**
     * 添加管理员测试
     */
    @Test
    public void testAddAdmin() {
        Admin admin = new Admin();
        admin.setName("456");
        admin.setAccount("456");
        admin.setPassword("456");
        admin.setBelongId(cAdmin.getBelongId());
        admin = adminService.addAdmin(cAdmin, admin, new String[]{role.getRoleId()});
        List<AdminRoleRela> listAdminRoleRela = adminRoleRelaDao.findAdminRoleRelaByAdmin(admin);
        adminRoleRela = listAdminRoleRela.get(0);
        Assert.assertTrue(!admin.equals(null));
        adminService.deleteAdmin(cAdmin, admin);
    }

    /**
     * 管理员查询测试
     */
    @Test
    public void testFindAdmin() {
        List<Admin> adminList = adminService.findAdminByRoleId(cAdmin, admin, role.getRoleId());
        Assert.assertTrue(adminList.size() > 0);
    }

    /**
     * 密码重置测试
     */
    @Test
    public void test() {

        String oldPassword = admin.getPassword();
        String newPassword = "123456";
        admin = adminService.resetPassWord(admin.getAdminId(), oldPassword, newPassword);
        Assert.assertTrue(admin.getPassword().equals(EncodeUtil.encodePassword(newPassword, admin.getName())));

    }

    /**
     * 删除管理员测试
     */
    @Test
    public void testDeleteAdmin() {
        adminService.deleteAdmin(cAdmin, admin);
        admin = adminDao.findOne(admin.getAdminId());
        Assert.assertTrue(admin == null);
    }

    /**
     * 修改管理员测试
     */
    @Test
    public void testUpdateAdmin() {
        String adminName = "原形";
        admin.setName(adminName);
        admin = adminService.updateAdmin(cAdmin, admin, null);
        Assert.assertTrue(adminName.equals(admin.getName()));
        List result = adminRoleRelaDao.getAdminRoleRelaByAdmin(admin);//.getRole();
        Assert.assertEquals(0, result.size());
    }

    /**
     * 查询管理员
     */
    @Test
    public void testQueryAdmin() {

        Admin testAdmin = new Admin();

        PageParam p = new PageParam();
        p.setLimit(10);
        p.setOffset(0);
        Page<Admin> adminPage = adminService.findAdminByNameOrRoleId(testAdmin, "", "desc", p.newPageable());
        Assert.assertNotNull(adminPage);
    }
    
    @Test
    public void testQueryRoleByAdminId(){
    	List<Role> roles = roleDao.getRoleByAdminId(admin.getAdminId());
    	
    	Assert.assertNotNull(roles);
    	
    	Assert.assertEquals(1, roles.size());
    }

}
