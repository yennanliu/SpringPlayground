package com.example.chatapp.controller;

import com.example.chatapp.dto.MessageDto;
import com.example.chatapp.model.Message;
import com.example.chatapp.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @Mock
    private Principal principal;

    @InjectMocks
    private ChatController chatController;

    private MessageDto messageDto;

    @BeforeEach
    void setUp() {
        messageDto = new MessageDto();
        messageDto.setId(1L);
        messageDto.setContent("Hello channel");
        messageDto.setTimestamp(LocalDateTime.now());
        messageDto.setType(Message.MessageType.CHANNEL);
        messageDto.setSenderId(1L);
        messageDto.setSenderUsername("sender");
        messageDto.setChannelId(1L);
    }

    @Test
    void sendChannelMessage_Success() {
        // Arrange
        when(headerAccessor.getUser()).thenReturn(principal);
        when(principal.getName()).thenReturn("sender");
        when(messageService.sendChannelMessage(anyLong(), anyLong(), anyString())).thenReturn(messageDto);

        // Act
        chatController.sendChannelMessage(1L, messageDto, headerAccessor);

        // Assert
        verify(messageService, times(1)).sendChannelMessage(1L, 1L, "Hello channel");
    }

    @Test
    void sendPrivateMessage_Success() {
        // Arrange
        when(messageService.sendDirectMessage(anyLong(), anyLong(), anyString())).thenReturn(messageDto);

        // Act
        chatController.sendPrivateMessage(2L, messageDto);

        // Assert
        verify(messageService, times(1)).sendDirectMessage(1L, 2L, "Hello channel");
    }
} 