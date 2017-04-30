package com.railwayservice.application.cache;

import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.stationmanage.entity.RailwayStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis配置类，使用注解配置。
 *
 * @author Ewing
 * @date 2017/3/30
 */
@Configuration
@PropertySource("classpath:common.properties")
public class SpringRedisConfig {

    @Value("${redis.maxTotal}")
    private int maxTotal;

    @Value("${redis.maxIdle}")
    private int maxIdle;

    @Value("${redis.maxWaitMillis}")
    private long maxWaitMillis;

    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${redis.hostName}")
    private String hostName;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.timeout}")
    private int timeout;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        return poolConfig;
    }

    /**
     * 商户缓存，使用第2个Redis库。
     */
    @Bean
    @Autowired
    public RedisTemplate<String, Merchant> merchantRedisTemplate(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(hostName);
        factory.setPort(port);
        factory.setPassword(password);
        factory.setTimeout(timeout);
        factory.setPoolConfig(jedisPoolConfig);
        factory.setDatabase(2);
        factory.setUsePool(true);
        factory.afterPropertiesSet();
        RedisTemplate<String, Merchant> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new ProtostuffSerializer<>(Merchant.class));
        return redisTemplate;
    }

    /**
     * 服务员对象缓存，使用第3个Redis库。
     */
    @Bean
    @Autowired
    public RedisTemplate<String, ServiceProvider> serviceProviderRedisTemplate(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(hostName);
        factory.setPort(port);
        factory.setPassword(password);
        factory.setTimeout(timeout);
        factory.setPoolConfig(jedisPoolConfig);
        factory.setDatabase(3);
        factory.setUsePool(true);
        factory.afterPropertiesSet();
        RedisTemplate<String, ServiceProvider> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new ProtostuffSerializer<>(ServiceProvider.class));
        return redisTemplate;
    }

}
