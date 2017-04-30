package com.railwayservice.travelroute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.railwayservice.user.dao.UserDao;
import com.railwayservice.user.entity.TravelRoute;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.TravelRouteService;
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
 * 行程测试类。
 *
 * @author lid
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestTravelRoute {

    TravelRoute travelRoute = null;
    User user = null;
    private TravelRouteService travelRouteService;
    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setTravelRouteService(TravelRouteService travelRouteService) {
        this.travelRouteService = travelRouteService;
    }

    @Before
    public void setup() {
        List<User> listUser = userDao.findAll();
        user = listUser.get(0);

        String customerName = "lidu";
        String customerPhone = "13316483222";
        String lineNo = "K817";
        String carriageNumber = "20";
        String seatNumber = "20";
        //travelRoute = travelRouteService.addTravelRoute(user.getUserId(), customerName, customerPhone, lineNo, carriageNumber, seatNumber);

    }

    @After
    public void tearDown() {

        travelRouteService.deleteTravelRoute(travelRoute.getRouteId());
        user = null;
    }

    /**
     * 根据用户信息查找行程
     */
    @Test
    public void testGetByUserId() throws JsonProcessingException {
        TravelRoute newTravelRoute = travelRouteService.getTravelRouteByUserId(user.getUserId());
        Assert.assertTrue(newTravelRoute.getLineNo().equals(travelRoute.getLineNo()));
    }

    /**
     * 更新行程
     */
    @Test
    public void testUpdateRoute() throws JsonProcessingException {
        travelRoute.setLineNo("K818");
        travelRoute.setCustomerName("lidutest");
        TravelRoute newTravelRoute = travelRouteService.updateTravelRoute(travelRoute);
        Assert.assertTrue((newTravelRoute.getLineNo().equals("K818")) && (newTravelRoute.getCustomerName().equals(travelRoute.getCustomerName())));

        Assert.assertTrue(!newTravelRoute.getAboardStation().equals(travelRoute.getAboardStation()));
    }

}
