package com.example.chatapp.controller;

import com.example.chatapp.dto.MessageDto;
import com.example.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/channel/{channelId}")
    public ResponseEntity<MessageDto> sendChannelMessage(
            @PathVariable Long channelId,
            @RequestBody Map<String, Object> request) {
        
        Long senderId = Long.valueOf(request.get("senderId").toString());
        String content = (String) request.get("content");
        
        MessageDto message = messageService.sendChannelMessage(senderId, channelId, content);
        return ResponseEntity.ok(message);
    }
    
    @PostMapping("/direct/{recipientId}")
    public ResponseEntity<MessageDto> sendDirectMessage(
            @PathVariable Long recipientId,
            @RequestBody Map<String, Object> request) {
        
        Long senderId = Long.valueOf(request.get("senderId").toString());
        String content = (String) request.get("content");
        
        MessageDto message = messageService.sendDirectMessage(senderId, recipientId, content);
        return ResponseEntity.ok(message);
    }
    
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageDto>> getChannelMessages(@PathVariable Long channelId) {
        List<MessageDto> messages = messageService.getChannelMessages(channelId);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/direct/{userId1}/{userId2}")
    public ResponseEntity<List<MessageDto>> getDirectMessages(
            @PathVariable Long userId1,
            @PathVariable Long userId2) {
        
        List<MessageDto> messages = messageService.getDirectMessages(userId1, userId2);
        return ResponseEntity.ok(messages);
    }
} 