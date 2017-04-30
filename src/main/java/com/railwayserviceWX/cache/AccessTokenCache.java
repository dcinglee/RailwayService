package com.railwayserviceWX.cache;

import com.railwayservice.application.cache.StaticRedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 微信accesstoken缓存实现类，每次需要token时从缓存中获取
 *
 * @author lid
 * @date 2017.2.24
 */
public class AccessTokenCache {
    private static Logger logger = LoggerFactory.getLogger(AccessTokenCache.class);

    // 静态工具类不允许实例化
    private AccessTokenCache(){
    }

    private static RedisTemplate<String, String> stringRedisTemplate = StaticRedisConfig.getStringRedisTemplate();

    private static long timeout = 7000; // AccessToken缓存最长7200秒，系统设置为7000秒

    public static void put(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public static String get(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("AccessToken缓存 key=" + key + " 读取异常：" + e.getMessage());
            return null;
        }
    }

    public static void remove(String key) {
        stringRedisTemplate.delete(key);
    }

}
