package com.xiao.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

@Configuration
public class RedisConfig {
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    @Bean
    public ZSetOperations<String, Object> zSetOperations() {
        return redisTemplate.opsForZSet();
    }
}
