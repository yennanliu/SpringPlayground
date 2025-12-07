package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.model.Message;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.MessageRepository;
import com.yen.ChatAppV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Page<ChatMessageDTO> searchMessages(String query, String channelId, Pageable pageable) {
        Page<Message> messages;

        if (channelId != null) {
            // Search within specific channel
            messages = messageRepository.searchMessagesByChannel(query, channelId, pageable);
        } else {
            // Search across all channels
            messages = messageRepository.searchMessages(query, pageable);
        }

        log.debug("Found {} messages matching query: {}", messages.getTotalElements(), query);
        return messages.map(this::convertToDTO);
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
