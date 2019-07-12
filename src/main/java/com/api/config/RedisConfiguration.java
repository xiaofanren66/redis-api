package com.api.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置
 * @author zhaoxingwu
 *
 */
@Configuration
public class RedisConfiguration {
    @Bean(name = "jedis.pool")
    @Autowired
    public JedisPool jedisPool(@Qualifier("jedis.config") JedisPoolConfig config,
                               @Value("${jedis.host}") String host,
                               @Value("${jedis.port}") int port,
                               @Value("${jedis.timeout}") int timeout,
                               @Value("${jedis.password}") String password) {
        if (StringUtils.isEmpty(password)) {
            return new JedisPool(config, host, port, timeout);
        } else {
            return new JedisPool(config, host, port, timeout, password);
        }
    }

    @Bean(name = "jedis.config")
    public JedisPoolConfig jedisPoolConfig(@Value("${jedis.pool.maxTotal}") int maxTotal,
                                           @Value("${jedis.pool.maxIdle}") int maxIdle,
                                           @Value("${jedis.pool.maxWaitMillis}") int maxWaitMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        return config;
    }
}
