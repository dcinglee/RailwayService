package com.railwayservice.authority;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.railwayservice.authority.dao.AdminRoleRelaDao;
import com.railwayservice.authority.dao.AuthorityDao;
import com.railwayservice.authority.entity.AdminRoleRela;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.entity.Role;
import com.railwayservice.authority.service.RoleService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 角色测试类。
 *
 * @author lid
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class RoleTest {
    Role role = null;
    String[] listAuthorityId = null;
    private RoleService roleService;
    private AuthorityDao authorityDao;
    private AdminRoleRelaDao adminRoleRelaDao;

    @Autowired
    public void setAdminRoleRelaDao(AdminRoleRelaDao adminRoleRelaDao) {
        this.adminRoleRelaDao = adminRoleRelaDao;
    }

    @Autowired
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Before
    public void setup() {
        String name = "lidutestrole";
        String description = "lidutestrole";
        List<Authority> listAuthority = authorityDao.findAll();
        listAuthorityId = new String[listAuthority.size()];
        for (int index = 0; index < listAuthority.size(); index++) {
            listAuthorityId[index] = listAuthority.get(index).getAuthorityId();
        }
        role = roleService.addRole(name, description, listAuthorityId);
    }

    @After
    public void tearDown() {
        if (null != role) {
            if (null != roleService.findByRoleId(role.getRoleId())) {
                roleService.deleteRole(role.getRoleId());
            }
        }
        role = null;
        role = null;
    }

    @Test
    public void testGetRoles() {
        List<Role> list = roleService.getAllRole();
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testFindRoleById() throws JsonProcessingException {
        Role newRole = roleService.findByRoleId(role.getRoleId());
        Assert.assertTrue(newRole.getDescription().equals(role.getDescription()));
    }

    @Test
    public void deleteRole() throws JsonProcessingException {
        if (null != role) {
            roleService.deleteRole(role.getRoleId());
        }
        Role newRole = roleService.findByRoleId(role.getRoleId());
        Assert.assertTrue(null == newRole);

        List<AdminRoleRela> listRoleAuthorityRela = adminRoleRelaDao.getAdminRoleRelaByRole(role);
        Assert.assertTrue(0 == listRoleAuthorityRela.size());
    }

    @Test
    public void updateRole() throws JsonProcessingException {
        roleService.updateRole(role.getRoleId(), "updatetest", "updatetest", null);
        Role newRole = roleService.findByRoleId(role.getRoleId());
        Assert.assertTrue((newRole.getName().equals("updatetest")) && (newRole.getDescription().equals("updatetest")));
    }
}
