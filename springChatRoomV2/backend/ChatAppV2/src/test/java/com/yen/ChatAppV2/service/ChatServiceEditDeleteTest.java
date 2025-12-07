package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.exception.UnauthorizedException;
import com.yen.ChatAppV2.model.Message;
import com.yen.ChatAppV2.model.MessageType;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.ChannelMemberRepository;
import com.yen.ChatAppV2.repository.MessageRepository;
import com.yen.ChatAppV2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceEditDeleteTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelMemberRepository channelMemberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatService chatService;

    private Message message;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setDisplayName("Test User");

        message = new Message();
        message.setId(1L);
        message.setChannelId("group:10");
        message.setSenderId(1L);
        message.setContent("Original message");
        message.setMessageType(MessageType.TEXT);
        message.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testEditMessageSuccess() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ChatMessageDTO result = chatService.editMessage(1L, 1L, "Updated message");

        assertNotNull(result);
        assertEquals("Updated message", message.getContent());
        assertNotNull(message.getEditedAt());

        verify(messageRepository).save(message);
        verify(messagingTemplate).convertAndSend(
            eq("/topic/channel/group:10/edit"),
            any(ChatMessageDTO.class)
        );
    }

    @Test
    void testEditMessageNotFound() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> chatService.editMessage(1L, 1L, "Updated message"));
    }

    @Test
    void testEditMessageUnauthorized() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        assertThrows(UnauthorizedException.class,
            () -> chatService.editMessage(1L, 999L, "Updated message"));

        verify(messageRepository, never()).save(any());
    }

    @Test
    void testDeleteMessageSuccess() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        chatService.deleteMessage(1L, 1L);

        assertTrue(message.isDeleted());
        assertEquals("[Message deleted]", message.getContent());

        verify(messageRepository).save(message);
        verify(messagingTemplate).convertAndSend(
            eq("/topic/channel/group:10/delete"),
            eq(1L)
        );
    }

    @Test
    void testDeleteMessageNotFound() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> chatService.deleteMessage(1L, 1L));
    }

    @Test
    void testDeleteMessageUnauthorized() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        assertThrows(UnauthorizedException.class,
            () -> chatService.deleteMessage(1L, 999L));

        assertFalse(message.isDeleted());
        verify(messageRepository, never()).save(any());
    }
}
