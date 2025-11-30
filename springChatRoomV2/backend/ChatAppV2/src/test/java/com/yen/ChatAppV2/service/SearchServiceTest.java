package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.model.Message;
import com.yen.ChatAppV2.model.MessageType;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.MessageRepository;
import com.yen.ChatAppV2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SearchService searchService;

    private Message message;
    private User user;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setDisplayName("Test User");

        message = new Message();
        message.setId(1L);
        message.setChannelId(10L);
        message.setSenderId(1L);
        message.setContent("Hello world");
        message.setMessageType(MessageType.TEXT);
        message.setCreatedAt(LocalDateTime.now());

        pageable = PageRequest.of(0, 20);
    }

    @Test
    void testSearchMessagesAcrossAllChannels() {
        Page<Message> messagePage = new PageImpl<>(Arrays.asList(message));
        when(messageRepository.searchMessages(eq("hello"), any(Pageable.class)))
                .thenReturn(messagePage);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Page<ChatMessageDTO> result = searchService.searchMessages("hello", null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Hello world", result.getContent().get(0).getContent());
        assertEquals("Test User", result.getContent().get(0).getSenderName());

        verify(messageRepository).searchMessages(eq("hello"), any(Pageable.class));
        verify(messageRepository, never()).searchMessagesByChannel(anyString(), anyLong(), any());
    }

    @Test
    void testSearchMessagesInSpecificChannel() {
        Page<Message> messagePage = new PageImpl<>(Arrays.asList(message));
        when(messageRepository.searchMessagesByChannel(eq("hello"), eq(10L), any(Pageable.class)))
                .thenReturn(messagePage);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Page<ChatMessageDTO> result = searchService.searchMessages("hello", 10L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Hello world", result.getContent().get(0).getContent());

        verify(messageRepository).searchMessagesByChannel(eq("hello"), eq(10L), any(Pageable.class));
        verify(messageRepository, never()).searchMessages(anyString(), any());
    }

    @Test
    void testSearchMessagesWithUnknownSender() {
        Page<Message> messagePage = new PageImpl<>(Arrays.asList(message));
        when(messageRepository.searchMessages(eq("hello"), any(Pageable.class)))
                .thenReturn(messagePage);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Page<ChatMessageDTO> result = searchService.searchMessages("hello", null, pageable);

        assertNotNull(result);
        assertEquals("Unknown", result.getContent().get(0).getSenderName());
    }
}
