package com.railwayservice.application.cache;

import com.railwayservice.application.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * Redis静态配置类。
 *
 * @author Ewing
 * @date 2017/3/30
 */
public class StaticRedisConfig {
    private static Logger logger = LoggerFactory.getLogger(StaticRedisConfig.class);
    private static RedisTemplate<String, String> stringRedisTemplate;

    // 静态工具类不允许实例化
    private StaticRedisConfig() {
    }

    static {
        try {
            Properties property = new Properties();
            InputStream inputStream = StaticRedisConfig.class.getClassLoader().getResourceAsStream("common.properties");
            property.load(inputStream);

            // 加载Redis配置
            int maxTotal = Integer.valueOf(property.getProperty("redis.maxTotal"));
            int maxIdle = Integer.valueOf(property.getProperty("redis.maxIdle"));
            long maxWaitMillis = Long.valueOf(property.getProperty("redis.maxWaitMillis"));
            boolean testOnBorrow = Boolean.valueOf(property.getProperty("redis.testOnBorrow"));
            String hostName = property.getProperty("redis.hostName");
            int port = Integer.valueOf(property.getProperty("redis.port"));
            String password = property.getProperty("redis.password");
            int timeout = Integer.valueOf(property.getProperty("redis.timeout"));

            // Jedis连接池配置
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(maxTotal);
            poolConfig.setMaxIdle(maxIdle);
            poolConfig.setMaxWaitMillis(maxWaitMillis);
            poolConfig.setTestOnBorrow(testOnBorrow);

            // 初始化Jedis连接池
            JedisConnectionFactory factory = new JedisConnectionFactory();
            factory.setHostName(hostName);
            factory.setPort(port);
            factory.setPassword(password);
            factory.setTimeout(timeout);
            factory.setPoolConfig(poolConfig);
            factory.setDatabase(1);
            factory.setUsePool(true);
            factory.afterPropertiesSet();

            // 初始化RedisTemplate
            stringRedisTemplate = new RedisTemplate<>();
            stringRedisTemplate.setConnectionFactory(factory);
            stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
            stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
            stringRedisTemplate.afterPropertiesSet();

        } catch (Exception e) {
            logger.error("初始化Redis缓存失败：", e);
            throw new AppException("初始化Redis缓存失败！");
        }
    }

    public static RedisTemplate<String, String> getStringRedisTemplate() {
        return stringRedisTemplate;
    }

}
