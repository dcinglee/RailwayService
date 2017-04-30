package com.railwayserviceWX.cache;

import com.railwayservice.application.cache.StaticRedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class JsApiTicketCache {
    private static Logger logger = LoggerFactory.getLogger(JsApiTicketCache.class);

    // 静态工具类不允许实例化
    private JsApiTicketCache(){
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
            logger.error("JsApiTicket缓存 key=" + key + " 读取异常：" + e.getMessage());
            return null;
        }
    }

    public static void remove(String key) {
        stringRedisTemplate.delete(key);
    }
}
