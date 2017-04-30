package com.railwayservice.common;

import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.service.CommonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.InputStream;

/**
 * 商户管理服务测试类。
 *
 * @author Ewing
 * @date 2017/2/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestCommon {
    private Logger logger = LoggerFactory.getLogger(TestCommon.class);

    private CommonService commonService;

    private ImageInfo imageInfo;

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Test
    public void addImageTest() {
        InputStream inputStream = TestCommon.class.getResourceAsStream("/images/default.jpg");
        imageInfo = commonService.addImage(inputStream);
        Assert.assertTrue(StringUtils.hasText(imageInfo.getImageId()));
        // 清理测试数据
        commonService.deleteImage(imageInfo.getImageId());
    }

    @Test
    public void deleteImageTest() {
        InputStream inputStream = TestCommon.class.getResourceAsStream("/images/default.jpg");
        imageInfo = commonService.addImage(inputStream);
        commonService.deleteImage(imageInfo.getImageId());
        Assert.assertTrue(true);
    }

}
