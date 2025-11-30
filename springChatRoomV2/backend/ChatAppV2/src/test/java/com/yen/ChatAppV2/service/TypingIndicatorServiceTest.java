package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.TypingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypingIndicatorServiceTest {

    @Mock
    private RedisService redisService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private TypingIndicatorService typingIndicatorService;

    @Test
    void testUserStartedTyping() {
        Long userId = 1L;
        Long channelId = 10L;
        String username = "testuser";

        typingIndicatorService.userStartedTyping(userId, channelId, username);

        verify(redisService).addTypingUser(eq("channel:10:typing"), eq(userId), anyLong());

        ArgumentCaptor<TypingEvent> eventCaptor = ArgumentCaptor.forClass(TypingEvent.class);
        verify(messagingTemplate).convertAndSend(
            eq("/topic/channel/10/typing"),
            eventCaptor.capture()
        );

        TypingEvent event = eventCaptor.getValue();
        assertEquals(userId, event.getUserId());
        assertEquals(username, event.getUsername());
        assertTrue(event.isTyping());
    }

    @Test
    void testUserStoppedTyping() {
        Long userId = 1L;
        Long channelId = 10L;

        typingIndicatorService.userStoppedTyping(userId, channelId);

        verify(redisService).removeTypingUser(eq("channel:10:typing"), eq(userId));

        ArgumentCaptor<TypingEvent> eventCaptor = ArgumentCaptor.forClass(TypingEvent.class);
        verify(messagingTemplate).convertAndSend(
            eq("/topic/channel/10/typing"),
            eventCaptor.capture()
        );

        TypingEvent event = eventCaptor.getValue();
        assertEquals(userId, event.getUserId());
        assertNull(event.getUsername());
        assertFalse(event.isTyping());
    }
}
