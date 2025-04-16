package com.example.chatapp.service;

import com.example.chatapp.dto.MessageDto;
import com.example.chatapp.model.Channel;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.ChannelRepository;
import com.example.chatapp.repository.MessageRepository;
import com.example.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public MessageDto sendChannelMessage(Long senderId, Long channelId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        
        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setType(Message.MessageType.CHANNEL);
        message.setSender(sender);
        message.setChannel(channel);
        
        message = messageRepository.save(message);
        
        // Cache the message in Redis
        String cacheKey = "channel:" + channelId + ":messages";
        redisTemplate.opsForList().rightPush(cacheKey, message);
        
        // Convert to DTO
        MessageDto messageDto = convertToDto(message);
        
        // Send to WebSocket subscribers
        messagingTemplate.convertAndSend("/topic/channel/" + channelId, messageDto);
        
        return messageDto;
    }
    
    public MessageDto sendDirectMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));
        
        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setType(Message.MessageType.DIRECT);
        message.setSender(sender);
        message.setRecipient(recipient);
        
        message = messageRepository.save(message);
        
        // Cache the message in Redis
        String cacheKey = "dm:" + Math.min(senderId, recipientId) + ":" + Math.max(senderId, recipientId);
        redisTemplate.opsForList().rightPush(cacheKey, message);
        
        // Convert to DTO
        MessageDto messageDto = convertToDto(message);
        
        // Send to recipient via WebSocket
        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/messages",
                messageDto
        );
        
        return messageDto;
    }
    
    public List<MessageDto> getChannelMessages(Long channelId) {
        // Try to get from Redis first
        String cacheKey = "channel:" + channelId + ":messages";
        List<Object> cachedMessages = redisTemplate.opsForList().range(cacheKey, 0, -1);
        
        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            return cachedMessages.stream()
                    .map(obj -> (Message) obj)
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        
        // If not in cache, get from DB and cache the results
        List<Message> messages = messageRepository.findByChannelIdOrderByTimestampAsc(channelId);
        messages.forEach(message -> redisTemplate.opsForList().rightPush(cacheKey, message));
        
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<MessageDto> getDirectMessages(Long userId1, Long userId2) {
        // Try to get from Redis first
        String cacheKey = "dm:" + Math.min(userId1, userId2) + ":" + Math.max(userId1, userId2);
        List<Object> cachedMessages = redisTemplate.opsForList().range(cacheKey, 0, -1);
        
        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            return cachedMessages.stream()
                    .map(obj -> (Message) obj)
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        
        // If not in cache, get from DB and cache the results
        List<Message> messages = messageRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
                userId1, userId2, userId2, userId1);
        
        messages.forEach(message -> redisTemplate.opsForList().rightPush(cacheKey, message));
        
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setType(message.getType());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderUsername(message.getSender().getUsername());
        
        if (message.getChannel() != null) {
            dto.setChannelId(message.getChannel().getId());
        }
        
        if (message.getRecipient() != null) {
            dto.setRecipientId(message.getRecipient().getId());
        }
        
        return dto;
    }
} 