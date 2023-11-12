package com.yen.springChatRoom.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChatControllerTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testQueryRedis(){

        final String onlineUserKey = "websocket.onlineUsers";
        // https://ost.51cto.com/posts/2333
        Set<String> resultSet = redisTemplate.opsForSet().members(onlineUserKey);
        System.out.println("resultSet:" + resultSet);

    }

}