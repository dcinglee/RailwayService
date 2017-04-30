package com.railwayservice.grabticket;

import com.railwayservice.application.util.RandomString;
import com.railwayservice.grabticket.dao.RailwayTicketDao;
import com.railwayservice.grabticket.entity.RailwayTicket;
import com.railwayservice.grabticket.service.RailwayTicketService;
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

/**
 * 车票服务测试类。
 *
 * @author Ewing
 * @date 2017/2/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestRailwayTicket {
    private Logger logger = LoggerFactory.getLogger(TestRailwayTicket.class);

    private RailwayTicketService railwayTicketService;
    private RailwayTicketDao railwayTicketDao;

    @Autowired
    public void setRailwayTicketService(RailwayTicketService railwayTicketService) {
        this.railwayTicketService = railwayTicketService;
    }

    @Autowired
    public void setRailwayTicketDao(RailwayTicketDao railwayTicketDao) {
        this.railwayTicketDao = railwayTicketDao;
    }

    public RailwayTicket createRailwayTicket() {
        RailwayTicket railwayTicket = new RailwayTicket();
        return railwayTicketService.addRailwayTicket(railwayTicket);
    }

    public void cleanData(RailwayTicket railwayTicket) {
        if (railwayTicket != null)
            railwayTicketDao.delete(railwayTicket);
    }

    @Test
    public void addRailwayTicketTest() {
        RailwayTicket railwayTicket = new RailwayTicket();
        railwayTicket = railwayTicketService.addRailwayTicket(railwayTicket);
        // 在断言之前清理以免留下测试数据。
        // cleanData(railwayTicket);
        Assert.assertTrue(StringUtils.hasText(railwayTicket.getRailwayTicketId()));
    }

    @Test
    public void updateRailwayTicketTest() {
        String name = RandomString.getChinese(4);
        RailwayTicket railwayTicket = createRailwayTicket();
        railwayTicket = railwayTicketService.updateRailwayTicket(railwayTicket);
        // 在断言之前清理以免留下测试数据。
        cleanData(railwayTicket);
        Assert.assertTrue(true);
    }

    @Test
    public void queryRailwayTicketTest() {
        RailwayTicket railwayTicket = createRailwayTicket();
        Pageable pageable = new PageRequest(0, 10);
        Page railwayTickets = railwayTicketService.queryRailwayTickets("", pageable);
        // 在断言之前清理以免留下测试数据。
        cleanData(railwayTicket);
        Assert.assertTrue(railwayTickets.getTotalElements() > 0);
    }

    @Test
    public void deleteRailwayTicketTest() {
        RailwayTicket railwayTicket = createRailwayTicket();
        railwayTicketService.deleteRailwayTicket(railwayTicket.getRailwayTicketId());
        RailwayTicket railwayTicketResult = railwayTicketDao.findOne(railwayTicket.getRailwayTicketId());
        Assert.assertNull(railwayTicketResult);
    }

}
