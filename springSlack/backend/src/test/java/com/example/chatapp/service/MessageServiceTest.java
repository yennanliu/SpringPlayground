package com.example.chatapp.service;

import com.example.chatapp.dto.MessageDto;
import com.example.chatapp.model.Channel;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.ChannelRepository;
import com.example.chatapp.repository.MessageRepository;
import com.example.chatapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @InjectMocks
    private MessageService messageService;

    private User sender;
    private User recipient;
    private Channel channel;
    private Message channelMessage;
    private Message directMessage;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);
        sender.setUsername("sender");

        recipient = new User();
        recipient.setId(2L);
        recipient.setUsername("recipient");

        channel = new Channel();
        channel.setId(1L);
        channel.setName("general");

        channelMessage = new Message();
        channelMessage.setId(1L);
        channelMessage.setContent("Hello channel");
        channelMessage.setTimestamp(LocalDateTime.now());
        channelMessage.setType(Message.MessageType.CHANNEL);
        channelMessage.setSender(sender);
        channelMessage.setChannel(channel);

        directMessage = new Message();
        directMessage.setId(2L);
        directMessage.setContent("Hello user");
        directMessage.setTimestamp(LocalDateTime.now());
        directMessage.setType(Message.MessageType.DIRECT);
        directMessage.setSender(sender);
        directMessage.setRecipient(recipient);

        when(redisTemplate.opsForList()).thenReturn(listOperations);
    }

    @Test
    void sendChannelMessage_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(channelRepository.findById(1L)).thenReturn(Optional.of(channel));
        when(messageRepository.save(any(Message.class))).thenReturn(channelMessage);

        // Act
        MessageDto result = messageService.sendChannelMessage(1L, 1L, "Hello channel");

        // Assert
        assertNotNull(result);
        assertEquals("Hello channel", result.getContent());
        assertEquals(Message.MessageType.CHANNEL, result.getType());
        assertEquals(1L, result.getSenderId());
        assertEquals(1L, result.getChannelId());

        // Verify Redis and WebSocket interactions
        verify(listOperations).rightPush(anyString(), any(Message.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/channel/1"), any(MessageDto.class));
    }

    @Test
    void sendDirectMessage_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(recipient));
        when(messageRepository.save(any(Message.class))).thenReturn(directMessage);

        // Act
        MessageDto result = messageService.sendDirectMessage(1L, 2L, "Hello user");

        // Assert
        assertNotNull(result);
        assertEquals("Hello user", result.getContent());
        assertEquals(Message.MessageType.DIRECT, result.getType());
        assertEquals(1L, result.getSenderId());
        assertEquals(2L, result.getRecipientId());

        // Verify Redis and WebSocket interactions
        verify(listOperations).rightPush(anyString(), any(Message.class));
        verify(messagingTemplate).convertAndSendToUser(
                eq("recipient"),
                eq("/queue/messages"),
                any(MessageDto.class)
        );
    }

    @Test
    void getChannelMessages_FromCache() {
        // Arrange
        List<Object> cachedMessages = Collections.singletonList(channelMessage);
        when(listOperations.range(eq("channel:1:messages"), eq(0L), eq(-1L)))
                .thenReturn(cachedMessages);

        // Act
        List<MessageDto> result = messageService.getChannelMessages(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello channel", result.get(0).getContent());
        
        // Verify that repository was not called as data was from cache
        verify(messageRepository, never()).findByChannelIdOrderByTimestampAsc(anyLong());
    }

    @Test
    void getChannelMessages_FromRepository() {
        // Arrange
        when(listOperations.range(eq("channel:1:messages"), eq(0L), eq(-1L)))
                .thenReturn(Collections.emptyList());
        when(messageRepository.findByChannelIdOrderByTimestampAsc(1L))
                .thenReturn(Collections.singletonList(channelMessage));

        // Act
        List<MessageDto> result = messageService.getChannelMessages(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello channel", result.get(0).getContent());
        
        // Verify repository was called and results were cached
        verify(messageRepository).findByChannelIdOrderByTimestampAsc(1L);
        verify(listOperations).rightPush(eq("channel:1:messages"), any(Message.class));
    }

    @Test
    void getDirectMessages_FromCache() {
        // Arrange
        List<Object> cachedMessages = Collections.singletonList(directMessage);
        when(listOperations.range(eq("dm:1:2"), eq(0L), eq(-1L)))
                .thenReturn(cachedMessages);

        // Act
        List<MessageDto> result = messageService.getDirectMessages(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello user", result.get(0).getContent());
        
        // Verify that repository was not called as data was from cache
        verify(messageRepository, never()).findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
                anyLong(), anyLong(), anyLong(), anyLong());
    }

    @Test
    void getDirectMessages_FromRepository() {
        // Arrange
        when(listOperations.range(eq("dm:1:2"), eq(0L), eq(-1L)))
                .thenReturn(Collections.emptyList());
        when(messageRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
                eq(1L), eq(2L), eq(2L), eq(1L)))
                .thenReturn(Collections.singletonList(directMessage));

        // Act
        List<MessageDto> result = messageService.getDirectMessages(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello user", result.get(0).getContent());
        
        // Verify repository was called and results were cached
        verify(messageRepository).findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
                eq(1L), eq(2L), eq(2L), eq(1L));
        verify(listOperations).rightPush(eq("dm:1:2"), any(Message.class));
    }

    @Test
    void getDirectMessages_EnsureCorrectKeyOrder() {
        // Arrange
        // Test the case where userId1 > userId2, should still get the same cache key
        when(listOperations.range(eq("dm:1:2"), eq(0L), eq(-1L)))
                .thenReturn(Collections.singletonList(directMessage));

        // Act
        List<MessageDto> result = messageService.getDirectMessages(2L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verify the key ordering in Redis
        verify(listOperations).range(eq("dm:1:2"), eq(0L), eq(-1L));
    }
} 