package com.railwayservice.application.cache;

import com.railwayservice.serviceprovider.entity.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 服务人员Redis缓存。
 *
 * @author Ewing
 * @date 2017/3/30
 */
@Service
public class ServiceProviderSessionCache {
    private final Logger logger = LoggerFactory.getLogger(ServiceProviderSessionCache.class);

    private long timeout = 2592000000L; // 2592000000毫秒=30天。
    private long timeidle = 604800L; // 604800秒=7天。

    private RedisTemplate<String, ServiceProvider> serviceProviderRedisTemplate;

    @Autowired
    public void setServiceProviderRedisTemplate(RedisTemplate<String, ServiceProvider> serviceProviderRedisTemplate) {
        this.serviceProviderRedisTemplate = serviceProviderRedisTemplate;
    }

    public void put(String key, ServiceProvider serviceProvider) {
        serviceProvider.setLoginTime(System.currentTimeMillis());
        serviceProviderRedisTemplate.opsForValue().set(key, serviceProvider, timeidle, TimeUnit.SECONDS);
    }

    /**
     * 超出最大生存周期则移除，否则更新空闲时间。
     */
    public ServiceProvider get(String key) {
        try {
            ServiceProvider serviceProvider = serviceProviderRedisTemplate.opsForValue().get(key);
            if (serviceProvider == null) return null;
            if (System.currentTimeMillis() - serviceProvider.getLoginTime() > timeout) {
                serviceProviderRedisTemplate.delete(key);
                return null;
            } else {
                serviceProviderRedisTemplate.expire(key, timeidle, TimeUnit.SECONDS);
                return serviceProvider;
            }
        } catch (Exception e) {
            logger.error("服务员缓存 key=" + key + " 读取异常：" + e.getMessage());
            return null;
        }
    }

    public void remove(String key) {
        serviceProviderRedisTemplate.delete(key);
    }

    public void removeAll() {
        serviceProviderRedisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    public long getSize() {
        return serviceProviderRedisTemplate.getConnectionFactory().getConnection().dbSize();
    }
}
