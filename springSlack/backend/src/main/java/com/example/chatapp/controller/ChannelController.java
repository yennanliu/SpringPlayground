package com.example.chatapp.controller;

import com.example.chatapp.model.Channel;
import com.example.chatapp.model.User;
import com.example.chatapp.service.ChannelService;
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
import java.util.Set;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @PostMapping
    public ResponseEntity<Channel> createChannel(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        Long creatorId = Long.valueOf(request.get("creatorId").toString());
        
        Channel channel = channelService.createChannel(name, description, creatorId);
        return ResponseEntity.ok(channel);
    }
    
    @GetMapping("/{channelId}")
    public ResponseEntity<Channel> getChannel(@PathVariable Long channelId) {
        Channel channel = channelService.getChannel(channelId);
        return ResponseEntity.ok(channel);
    }
    
    @GetMapping
    public ResponseEntity<List<Channel>> getAllChannels() {
        List<Channel> channels = channelService.getAllChannels();
        return ResponseEntity.ok(channels);
    }
    
    @PostMapping("/{channelId}/join")
    public ResponseEntity<Channel> joinChannel(
            @PathVariable Long channelId,
            @RequestBody Map<String, Long> request) {
        
        Long userId = request.get("userId");
        Channel channel = channelService.joinChannel(channelId, userId);
        return ResponseEntity.ok(channel);
    }
    
    @PostMapping("/{channelId}/leave")
    public ResponseEntity<Channel> leaveChannel(
            @PathVariable Long channelId,
            @RequestBody Map<String, Long> request) {
        
        Long userId = request.get("userId");
        Channel channel = channelService.leaveChannel(channelId, userId);
        return ResponseEntity.ok(channel);
    }
    
    @GetMapping("/{channelId}/members")
    public ResponseEntity<Set<User>> getChannelMembers(@PathVariable Long channelId) {
        Set<User> members = channelService.getChannelMembers(channelId);
        return ResponseEntity.ok(members);
    }
} 