package com.yen.springChatRoom.service;

import com.yen.springChatRoom.bean.Channel;
import com.yen.springChatRoom.bean.User;
import com.yen.springChatRoom.controller.ChatController;
import com.yen.springChatRoom.bean.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    private List<Channel> channels = new ArrayList<>();

    public List<Channel> getAllChannels() {

        return channels;
    }

    public Channel createChannel(String channelId, List<User> users) {

        Channel channel = new Channel(channelId, users);
        channels.add(channel);
        return channel;
    }

    // TODO : implement user - user send private msg ?

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
