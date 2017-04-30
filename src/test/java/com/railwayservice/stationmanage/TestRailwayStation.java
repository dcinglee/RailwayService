package com.railwayservice.stationmanage;

import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.stationmanage.dao.RailwayStationDao;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.entity.StationForImageRela;
import com.railwayservice.stationmanage.service.RailwayStationService;
import com.railwayservice.stationmanage.service.StationForImageRelaService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 车站测试类。
 *
 * @author Ewing
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestRailwayStation {

    List list = new ArrayList();
    private RailwayStationDao railwayStationDao;
    private RailwayStationService railwayStationService;
    private RailwayStation railwayStation;

    @Autowired
    public void setRailwayStationDao(RailwayStationDao railwayStationDao) {
        this.railwayStationDao = railwayStationDao;
    }

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    @Before
    public void setUp() {
        railwayStation = new RailwayStation();
        railwayStation.setStationName("原形科技");
        railwayStation.setStationNameAbbr("YXKJ");
        railwayStation.setProvince("长沙");
        railwayStation.setCity("长沙市");
        railwayStation.setStatus(1000);
        railwayStation.setLongitude(66.6);
        railwayStation.setLatitude(88.8);
        railwayStation.setCreateDate(new Date());
        railwayStation = railwayStationDao.save(railwayStation);
        list.add(railwayStation);
    }

    @After
    public void tearDown() {
        railwayStationDao.deleteInBatch(list);
    }


    /**
     * 添加车站
     */
    @Test
    public void testAddStation() {
        RailwayStation railwayStation = new RailwayStation();
        ImageInfo imageInfo = new ImageInfo();
        railwayStation.setStationName("原形");
        railwayStation.setStationNameAbbr("YX");
        railwayStation.setProvince("湖南省");
        railwayStation.setCity("长沙市");
        railwayStation.setStatus(1000);

        imageInfo.setImageId("1111");
        railwayStation = railwayStationService.addStation(railwayStation, imageInfo.getImageId());
        list.add(railwayStation);
        Assert.assertTrue(railwayStation != null && StringUtils.hasText(railwayStation.getStationId()));
    }

//    /**
//     * 删除车站
//     */
//    @Test
//    public void testDeleteStation() {
//        String satationId = railwayStationDao.findByStationName(railwayStation.getStationName()).getStationId();
//        railwayStationService.deleteStation(satationId);
//        Assert.assertTrue(satationId != null);
//    }

    /**
     * 更新车站
     */
    @Test
    public void testUpdateStation() {
        RailwayStation railwayStation = railwayStationDao.findFirstByOrderByCreateDateDesc();
        ImageInfo imageInfo = new ImageInfo();
        railwayStation.setStationName("Credit");
        railwayStation.setStationNameAbbr("YX");
        railwayStation.setProvince("湖南省");
        railwayStation.setCity("长沙市");
        railwayStation.setStatus(1000);

        imageInfo.setImageId("222");
        railwayStation = railwayStationService.updateStation(railwayStation, imageInfo.getImageId());
        list.add(railwayStation);
        Assert.assertTrue(railwayStation != null && StringUtils.hasText(railwayStation.getStationId()));
    }

    /**
     * 查询所有车站（不分页）
     */
    @Test
    public void testQueryAllStations() {
        List<RailwayStation> railwayStationList = railwayStationService.queryAllStations();
        Assert.assertTrue(railwayStationList.size() > 0);
    }

    /**
     * 通过经纬度查询车站信息
     */
    @Test
    public void testQueryStations() {
        railwayStation = railwayStationService.findRailwayStation(railwayStation.getLongitude(), railwayStation.getLatitude());
        Assert.assertTrue(railwayStation != null);
    }

    @Test
    public void testFindByStationId() {
        railwayStation = railwayStationService.findByStationId("station_changshanan");
        Assert.assertTrue(railwayStation != null);
    }

    private StationForImageRelaService stationForImageRelaService;

    @Autowired
    public void setStationForImageRelaService(StationForImageRelaService stationForImageRelaService) {
        this.stationForImageRelaService = stationForImageRelaService;
    }

    @Test
    public void testFindStationId() {
        List<String> stationForImageRelaList = stationForImageRelaService.findByStationId("CWQ");
        Assert.assertTrue(stationForImageRelaList.size() > 0);
    }
}