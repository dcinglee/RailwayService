package com.railwayservice.application.cache;

import com.railwayservice.merchantmanage.entity.Merchant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 商户Redis缓存。
 *
 * @author Ewing
 * @date 2017/3/30
 */
@Service
public class MerchantSessionCache {
    private final Logger logger = LoggerFactory.getLogger(MerchantSessionCache.class);

    private long timeout = 2592000000L; // 2592000000毫秒=30天。
    private long timeidle = 604800L; // 604800秒=7天。

    private RedisTemplate<String, Merchant> merchantRedisTemplate;

    @Autowired
    public void setMerchantRedisTemplate(RedisTemplate<String, Merchant> merchantRedisTemplate) {
        this.merchantRedisTemplate = merchantRedisTemplate;
    }

    public void put(String key, Merchant merchant) {
        merchant.setLoginTime(System.currentTimeMillis());
        merchantRedisTemplate.opsForValue().set(key, merchant, timeidle, TimeUnit.SECONDS);
    }

    /**
     * 超出最大生存周期则移除，否则更新空闲时间。
     */
    public Merchant get(String key) {
        try {
            Merchant merchant = merchantRedisTemplate.opsForValue().get(key);
            if (merchant == null) return null;
            if (System.currentTimeMillis() - merchant.getLoginTime() > timeout) {
                merchantRedisTemplate.delete(key);
                return null;
            } else {
                merchantRedisTemplate.expire(key, timeidle, TimeUnit.SECONDS);
                return merchant;
            }
        } catch (Exception e) {
            logger.error("商户缓存 key=" + key + " 读取异常：" + e.getMessage());
            return null;
        }
    }

    public void remove(String key) {
        merchantRedisTemplate.delete(key);
    }

    public void removeAll() {
        merchantRedisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    public long getSize() {
        return merchantRedisTemplate.getConnectionFactory().getConnection().dbSize();
    }
}
