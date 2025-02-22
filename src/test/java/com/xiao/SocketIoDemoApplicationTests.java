package com.xiao;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Date;

@MapperScan("com.xiao.mapper")
@SpringBootTest
class SocketIoDemoApplicationTests {
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    ZSetOperations<String, Object> zSetOperations;

    @Test
    public void test() {
        System.out.println(new Date(0));
//        for (int i = 1; i <= 20; i++)
//            zSetOperations.add("K", String.valueOf(i), i);
//        ZSetOperations.TypedTuple<Object> k = zSetOperations.popMax("K");
//        ZSetOperations.TypedTuple<Object> k = redisTemplate.opsForZSet().popMax("K");
//        System.out.println(k.getValue());
    }
}
