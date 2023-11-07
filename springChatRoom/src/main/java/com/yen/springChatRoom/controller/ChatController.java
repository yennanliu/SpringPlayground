package com.yen.springChatRoom.controller;

import com.yen.springChatRoom.model.ChatMessage;
import com.yen.springChatRoom.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ChatController {

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    /**
     *  single mode : read msg from FE, and send to
     *                other users (@SendTo("/topic/public")) directly
     */
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
//
//        return chatMessage;
//    }

    /**
     *  cluster mode : read msg from FE, but NOT send to other users,
     *                 instead, send to Redis channel, so the other service
     *                 on cluster can read/digest the msg
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage){
        try{
            //redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage)));
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage));
        }catch (Exception e){
            LOGGER.error("send msg error : " + e.getMessage(), e);
        }
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        LOGGER.info("User added in Chatroom:" + chatMessage.getSender());
        // add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;

        // TODO : update with below
//        try {
//            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
////            redisTemplate.opsForSet().add(onlineUsers, chatMessage.getSender());
////            redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
    }

}