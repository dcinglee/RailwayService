package com.railwayservice.application.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登陆盐缓存实现类。
 *
 * @author Ewing
 * @date 2017/2/14
 */
@Service
public class LoginSaltCache {
    private final Logger logger = LoggerFactory.getLogger(MerchantSessionCache.class);

    private long timeout = 86400L; // 86400秒=1天。

    private RedisTemplate<String, String> stringRedisTemplate = StaticRedisConfig.getStringRedisTemplate();

    public void put(String key, String code) {
        stringRedisTemplate.opsForValue().set(key, code, timeout, TimeUnit.SECONDS);
    }

    public String get(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("登陆盐缓 key=" + key + " 读取异常：" + e.getMessage());
            return null;
        }
    }

    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }
}
