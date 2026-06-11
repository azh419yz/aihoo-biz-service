package com.aihoo.api.doctor.common.utils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @program: aihoo-root
 * @description: 配置Redisson
 * @author: Mr.Li
 * @create: 2020-12-30 15:25
 **/
@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.port}")
    private String redisPort;

    @Value("${spring.data.redis.jedis.pool.max-idle}")
    private Integer maxPoolSize;

    @Value("${spring.data.redis.timeout}")
    private int timeout;

    @Value("${spring.data.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.data.redis.block-when-exhausted}")
    private boolean blockWhenExhausted;

    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer().
                setAddress("redis://" + redisHost + ":" + redisPort)
                .setPassword(redisPassword)
                .setConnectionPoolSize(maxPoolSize)
                .setConnectionMinimumIdleSize(0);
        return Redisson.create(config);
    }

    @Bean
    public JedisPool redisPoolFactory() {
        System.out.println("JedisPool注入成功!");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxPoolSize);
        poolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);
        poolConfig.setJmxEnabled(false);
        int parseInt = Integer.parseInt(redisPort);
        return new JedisPool(poolConfig, redisHost, parseInt, timeout, redisPassword);
    }
}
