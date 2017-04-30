package com.railwayservice.lineStation;

import com.railwayservice.stationmanage.entity.LineStation;
import com.railwayservice.stationmanage.service.LineStationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 高铁线路测试类。
 *
 * @author Ewing
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestLineStation {

    private LineStationService lineStationService;

    @Autowired
    public void setLineStationService(LineStationService lineStationService) {
        this.lineStationService = lineStationService;
    }

    @Test
    public void getLineStation() {
        String lineCode = "K817";
        List<LineStation> listLineStation = lineStationService.getLineStationBylineNo(lineCode);
        Assert.assertTrue(0 < listLineStation.size());
    }
}	
