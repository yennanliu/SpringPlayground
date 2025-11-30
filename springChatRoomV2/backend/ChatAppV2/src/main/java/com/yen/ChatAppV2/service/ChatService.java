package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.dto.MessageRequest;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.exception.UnauthorizedException;
import com.yen.ChatAppV2.model.Message;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.ChannelMemberRepository;
import com.yen.ChatAppV2.repository.MessageRepository;
import com.yen.ChatAppV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MessageRepository messageRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageDTO processAndSendMessage(MessageRequest request) {
        // 1. Validate user is member of channel
        if (!channelMemberRepository.existsByChannelIdAndUserId(
                request.getChannelId(), request.getSenderId())) {
            throw new UnauthorizedException("User is not a member of this channel");
        }

        // 2. Get sender info
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 3. Create message
        Message message = new Message();
        message.setChannelId(request.getChannelId());
        message.setSenderId(request.getSenderId());
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType());

        // 4. Save to database
        message = messageRepository.save(message);

        // 5. Cache in Redis
        redisService.cacheMessage(String.valueOf(request.getChannelId()), message);

        // 6. Create DTO
        ChatMessageDTO dto = new ChatMessageDTO(
                message.getId(),
                message.getChannelId(),
                message.getSenderId(),
                sender.getDisplayName() != null ? sender.getDisplayName() : sender.getUsername(),
                message.getContent(),
                message.getMessageType(),
                message.getCreatedAt()
        );

        // 7. Broadcast to channel
        messagingTemplate.convertAndSend(
                "/topic/channel/" + request.getChannelId(),
                dto
        );

        log.info("Message sent to channel {}: {}", request.getChannelId(), message.getId());
        return dto;
    }

    public Page<ChatMessageDTO> getChannelMessages(Long channelId, Long userId, Pageable pageable) {
        // Validate user is member
        if (!channelMemberRepository.existsByChannelIdAndUserId(channelId, userId)) {
            throw new UnauthorizedException("User is not a member of this channel");
        }

        // Fetch from database
        Page<Message> messages = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);

        // Convert to DTOs
        return messages.map(this::convertToDTO);
    }

    public ChatMessageDTO editMessage(Long messageId, Long userId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found"));

        if (!message.getSenderId().equals(userId)) {
            throw new UnauthorizedException("Can only edit own messages");
        }

        message.setContent(newContent);
        message.setEditedAt(LocalDateTime.now());
        message = messageRepository.save(message);

        ChatMessageDTO dto = convertToDTO(message);
        messagingTemplate.convertAndSend("/topic/channel/" + message.getChannelId() + "/edit", dto);

        log.info("Message {} edited by user {}", messageId, userId);
        return dto;
    }

    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found"));

        if (!message.getSenderId().equals(userId)) {
            throw new UnauthorizedException("Can only delete own messages");
        }

        message.setDeleted(true);
        message.setContent("[Message deleted]");
        messageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/topic/channel/" + message.getChannelId() + "/delete",
                messageId
        );

        log.info("Message {} deleted by user {}", messageId, userId);
    }

    private ChatMessageDTO convertToDTO(Message message) {
        User sender = userRepository.findById(message.getSenderId()).orElse(null);
        String senderName = sender != null ?
                (sender.getDisplayName() != null ? sender.getDisplayName() : sender.getUsername())
                : "Unknown";

        return new ChatMessageDTO(
                message.getId(),
                message.getChannelId(),
                message.getSenderId(),
                senderName,
                message.getContent(),
                message.getMessageType(),
                message.getCreatedAt()
        );
    }
}
