package com.railwayservice.user;

import com.railwayservice.application.util.EncodeUtil;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.user.dao.UserDao;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 用户管理测试类。
 *
 * @author lixs
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestUser {

    User usera = null;
    User userb = null;
    User userc = null;
    private UserService userService;
    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Before
    public void setUp() throws Exception {
        usera = new User();
        usera.setCity("滁州");
        usera.setName("李乡盛");
        usera.setOpenid("1213");
        userDao.save(usera);

    }

    @After
    public void tearDown() throws Exception {
        if (usera != null && userDao != null)
            userDao.delete(usera);
        if (userb != null && userDao != null)
            userDao.delete(userb);
        if (userc != null && userDao != null)
            userDao.delete(userc);
        usera = null;
        userb = null;
        userc = null;
    }

    @Test
    public void testUser() {
        User user = userService.getUserByOpenid(usera.getOpenid());
        Assert.assertTrue(user.getCity().equals("滁州"));
    }

    @Test
    public void testUserPwd() {
        String pwd = EncodeUtil.encodePassword("123456", "lidx");
        System.out.println(pwd);

        pwd = EncodeUtil.encodePassword("123456", "test2");
        System.out.println(pwd);
        
        pwd = EncodeUtil.encodePassword("123456", "test1");
        System.out.println(pwd);
    }

    @Test
    public void testAddUser() {
        userc = new User();
        
        userc.setName("皮卡丘");
        userc.setGender(1);
        userc.setLanguage("日语");
        userc.setOpenid("123456789");
        userb = userService.addUser(userc);

        Assert.assertEquals(userc, userb);

    }

    @Test
    public void testUpdateUser() {
        userb = new User();
        userb = userDao.findUserByUserId(usera.getUserId());
        userb.setGender(1);
        userb.setLanguage("汉语");
        userc = userService.updateUser(userb);
        Assert.assertTrue(userc.getGender() == 1);
    }

    @Test
    public void testFindall() {
        List<User> user = userService.getAllUser();
        Assert.assertTrue(user.size() > 0);
    }

    @Test
    public void testQueryAllUser() {
        PageParam param = new PageParam();
        Page<User> users = userService.queryAllUser(usera.getNickName(),usera.getPhoneNo(),param.newPageable());
        Assert.assertTrue(users != null);
    }

}
