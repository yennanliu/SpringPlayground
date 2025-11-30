package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.ReadReceiptEvent;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.model.ChannelMember;
import com.yen.ChatAppV2.model.ChannelMemberId;
import com.yen.ChatAppV2.repository.ChannelMemberRepository;
import com.yen.ChatAppV2.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadReceiptServiceTest {

    @Mock
    private ChannelMemberRepository channelMemberRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ReadReceiptService readReceiptService;

    private ChannelMember channelMember;

    @BeforeEach
    void setUp() {
        channelMember = new ChannelMember();
        channelMember.setChannelId(1L);
        channelMember.setUserId(10L);
        channelMember.setJoinedAt(LocalDateTime.now().minusDays(1));
    }

    @Test
    void testMarkAsRead() {
        when(channelMemberRepository.findById(any(ChannelMemberId.class)))
                .thenReturn(Optional.of(channelMember));

        readReceiptService.markAsRead(10L, 1L, 5L);

        assertNotNull(channelMember.getLastReadAt());
        verify(channelMemberRepository).save(channelMember);

        ArgumentCaptor<ReadReceiptEvent> eventCaptor = ArgumentCaptor.forClass(ReadReceiptEvent.class);
        verify(messagingTemplate).convertAndSend(
            eq("/topic/channel/1/read"),
            eventCaptor.capture()
        );

        ReadReceiptEvent event = eventCaptor.getValue();
        assertEquals(10L, event.getUserId());
        assertEquals(1L, event.getChannelId());
        assertEquals(5L, event.getMessageId());
    }

    @Test
    void testMarkAsReadWithNonexistentMember() {
        when(channelMemberRepository.findById(any(ChannelMemberId.class)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> readReceiptService.markAsRead(10L, 1L, 5L));
    }

    @Test
    void testGetUnreadCountWithLastReadAt() {
        channelMember.setLastReadAt(LocalDateTime.now().minusHours(1));
        when(channelMemberRepository.findById(any(ChannelMemberId.class)))
                .thenReturn(Optional.of(channelMember));
        when(messageRepository.countByChannelIdAndCreatedAtAfter(eq(1L), any(LocalDateTime.class)))
                .thenReturn(5L);

        int count = readReceiptService.getUnreadCount(10L, 1L);

        assertEquals(5, count);
    }

    @Test
    void testGetUnreadCountWithoutLastReadAt() {
        channelMember.setLastReadAt(null);
        when(channelMemberRepository.findById(any(ChannelMemberId.class)))
                .thenReturn(Optional.of(channelMember));
        when(messageRepository.countByChannelIdAndCreatedAtAfter(eq(1L), any(LocalDateTime.class)))
                .thenReturn(3L);

        int count = readReceiptService.getUnreadCount(10L, 1L);

        assertEquals(3, count);
    }
}
