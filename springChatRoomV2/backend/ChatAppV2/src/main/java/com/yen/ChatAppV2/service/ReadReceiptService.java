package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.ReadReceiptEvent;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.model.ChannelMember;
import com.yen.ChatAppV2.model.ChannelMemberId;
import com.yen.ChatAppV2.repository.ChannelMemberRepository;
import com.yen.ChatAppV2.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadReceiptService {

    private final ChannelMemberRepository channelMemberRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void markAsRead(Long userId, Long channelId, Long messageId) {
        ChannelMemberId memberId = new ChannelMemberId();
        memberId.setChannelId(channelId);
        memberId.setUserId(userId);

        ChannelMember member = channelMemberRepository
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException("Channel member not found"));

        member.setLastReadAt(LocalDateTime.now());
        channelMemberRepository.save(member);

        // Broadcast read receipt
        ReadReceiptEvent event = new ReadReceiptEvent(userId, channelId, messageId);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId + "/read", event);

        log.debug("User {} marked message {} as read in channel {}", userId, messageId, channelId);
    }

    public int getUnreadCount(Long userId, Long channelId) {
        ChannelMemberId memberId = new ChannelMemberId();
        memberId.setChannelId(channelId);
        memberId.setUserId(userId);

        ChannelMember member = channelMemberRepository
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException("Channel member not found"));

        LocalDateTime lastRead = member.getLastReadAt();
        if (lastRead == null) {
            lastRead = member.getJoinedAt();
        }

        Long count = messageRepository.countByChannelIdAndCreatedAtAfter(channelId, lastRead);
        return count != null ? count.intValue() : 0;
    }
}
