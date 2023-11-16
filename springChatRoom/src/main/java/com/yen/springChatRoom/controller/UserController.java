package com.yen.springChatRoom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.set.onlineUsers}")
    private String onlineUsers;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    final String onlineUserKey = "websocket.onlineUsers";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @GetMapping("/online_user")
    public List<String> getOnlineUser(){

        Set<String> resultSet = redisTemplate.opsForSet().members(onlineUserKey);
        System.out.println("(getOnlineUser) resultSet = " + resultSet);
        // TODO : optimize below
        List<String> users = new ArrayList<>();
        resultSet.forEach(x -> {
            users.add(x);
        });
        return users;
    }

}
