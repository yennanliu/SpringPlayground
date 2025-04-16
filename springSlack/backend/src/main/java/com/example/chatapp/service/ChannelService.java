package com.example.chatapp.service;

import com.example.chatapp.model.Channel;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.ChannelRepository;
import com.example.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Channel createChannel(String name, String description, Long creatorId) {
        if (channelRepository.existsByName(name)) {
            throw new RuntimeException("Channel name already exists");
        }
        
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Channel channel = new Channel();
        channel.setName(name);
        channel.setDescription(description);
        channel.getMembers().add(creator);
        
        return channelRepository.save(channel);
    }
    
    public Channel getChannel(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
    }
    
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }
    
    public Channel joinChannel(Long channelId, Long userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        channel.getMembers().add(user);
        return channelRepository.save(channel);
    }
    
    public Channel leaveChannel(Long channelId, Long userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        channel.getMembers().remove(user);
        return channelRepository.save(channel);
    }
    
    public Set<User> getChannelMembers(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        
        return channel.getMembers();
    }
} 