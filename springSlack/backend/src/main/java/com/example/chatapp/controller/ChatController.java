package com.example.chatapp.controller;

import com.example.chatapp.dto.MessageDto;
import com.example.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage/{channelId}")
    public void sendChannelMessage(
            @DestinationVariable Long channelId,
            @Payload MessageDto messageDto,
            SimpMessageHeaderAccessor headerAccessor) {
        
        Principal principal = headerAccessor.getUser();
        Long senderId = messageDto.getSenderId();
        
        messageService.sendChannelMessage(senderId, channelId, messageDto.getContent());
    }
    
    @MessageMapping("/chat.sendPrivateMessage/{recipientId}")
    public void sendPrivateMessage(
            @DestinationVariable Long recipientId,
            @Payload MessageDto messageDto) {
        
        Long senderId = messageDto.getSenderId();
        messageService.sendDirectMessage(senderId, recipientId, messageDto.getContent());
    }
} 