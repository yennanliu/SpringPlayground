package com.yen.springChatRoom.controller;

import com.yen.springChatRoom.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return chatMessage;
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
