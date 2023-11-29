package com.yen.springChatRoom.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Slf4j
public class HistoryController {

    @Value("${redis.channel.private}")
    private String privateChannel;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    //private RedisTemplate redisTemplate;

    //@MessageMapping("/private/chat_history/{username}")
    @GetMapping("/private/chat_history/{username}")
    public void getChatHistory(@PathVariable String username){

        log.info("getChatHistory : username = " + username);

        String key = "websocket.privateMsg." + username; // websocket.privateMsg.zzz
        log.info("key = " + key);
        Set<String> resultSet = redisTemplate.opsForSet().members(key);

        log.info("--> start");
        resultSet.forEach(System.out::println);
        log.info("--> end");
    }

}
