package com.railwayservice.common;

import com.railwayservice.application.cache.MerchantSessionCache;
import com.railwayservice.merchantmanage.entity.Merchant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 说明。
 *
 * @author Ewing
 * @date 2017/3/29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class RedisTest {

    private MerchantSessionCache merchantSessionCache;

    @Autowired
    public void setMerchantSessionCache(MerchantSessionCache merchantSessionCache) {
        this.merchantSessionCache = merchantSessionCache;
    }

    @Test
    public void merchantRedisTemplateTest() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setName("你好啊");
        merchantSessionCache.put("hello", merchant);

        Merchant res = merchantSessionCache.get("hello");
        System.out.println("获取到值：" + (res == null ? "null" : res.getName()));
    }

}
