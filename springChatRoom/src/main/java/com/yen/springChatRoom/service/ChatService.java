package com.yen.springChatRoom.service;

import com.yen.springChatRoom.controller.ChatController;
import com.yen.springChatRoom.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    public void sendMsg(@Payload ChatMessage chatMessage){

        LOGGER.info("Send msg by simpMessageSendingOperations:" + chatMessage.toString());
        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    }

    public void alertUserStatus(@Payload ChatMessage chatMessage) {

        LOGGER.info("Alert user online by simpMessageSendingOperations:" + chatMessage.toString());
        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    }

}
