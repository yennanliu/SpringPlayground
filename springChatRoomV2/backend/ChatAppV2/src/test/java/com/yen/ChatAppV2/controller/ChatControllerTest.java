package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.dto.MessageRequest;
import com.yen.ChatAppV2.model.MessageType;
import com.yen.ChatAppV2.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Test for ChatController
 *
 * Tests the WebSocket message handling logic in ChatController.
 * Verifies proper integration with ChatService.
 */
@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @InjectMocks
    private ChatController chatController;

    private MessageRequest testRequest;
    private ChatMessageDTO testResponse;

    @BeforeEach
    public void setup() {
        testRequest = new MessageRequest();
        testRequest.setChannelId("group:1");
        testRequest.setSenderId(1L);
        testRequest.setContent("Hello WebSocket!");
        testRequest.setMessageType(MessageType.TEXT);

        testResponse = new ChatMessageDTO();
        testResponse.setId(1L);
        testResponse.setChannelId("group:1");
        testResponse.setSenderId(1L);
        testResponse.setSenderName("Alice");
        testResponse.setContent("Hello WebSocket!");
        testResponse.setMessageType(MessageType.TEXT);
        testResponse.setTimestamp(LocalDateTime.now());
    }

    @Test
    public void testSendMessage_Success() {
        // Given: A valid message request
        when(chatService.processAndSendMessage(any(MessageRequest.class)))
                .thenReturn(testResponse);

        // When: Sending a message through WebSocket
        chatController.sendMessage("group:1", testRequest, headerAccessor);

        // Then: ChatService should be called with correct parameters
        ArgumentCaptor<MessageRequest> requestCaptor = ArgumentCaptor.forClass(MessageRequest.class);
        verify(chatService, times(1)).processAndSendMessage(requestCaptor.capture());

        MessageRequest captured = requestCaptor.getValue();
        assertEquals("group:1", captured.getChannelId());
        assertEquals(1L, captured.getSenderId());
        assertEquals("Hello WebSocket!", captured.getContent());
        assertEquals(MessageType.TEXT, captured.getMessageType());
    }

    @Test
    public void testSendMessage_SetsChannelIdFromPath() {
        // Given: A request with different channelId in body
        testRequest.setChannelId("wrong:channel");
        when(chatService.processAndSendMessage(any(MessageRequest.class)))
                .thenReturn(testResponse);

        // When: Sending through WebSocket with channelId in path
        chatController.sendMessage("group:1", testRequest, headerAccessor);

        // Then: ChannelId from path should override request body
        ArgumentCaptor<MessageRequest> requestCaptor = ArgumentCaptor.forClass(MessageRequest.class);
        verify(chatService).processAndSendMessage(requestCaptor.capture());

        assertEquals("group:1", requestCaptor.getValue().getChannelId());
    }

    @Test
    public void testSendMessage_DifferentMessageTypes() {
        // Test TEXT message
        testRequest.setMessageType(MessageType.TEXT);
        chatController.sendMessage("group:1", testRequest, headerAccessor);

        // Test IMAGE message
        testRequest.setMessageType(MessageType.IMAGE);
        testRequest.setContent("image.jpg");
        chatController.sendMessage("group:1", testRequest, headerAccessor);

        // Test FILE message
        testRequest.setMessageType(MessageType.FILE);
        testRequest.setContent("document.pdf");
        chatController.sendMessage("group:1", testRequest, headerAccessor);

        // Then: All three message types should be processed
        verify(chatService, times(3)).processAndSendMessage(any(MessageRequest.class));
    }

    @Test
    public void testSendMessage_GroupChannel() {
        // Given: A group channel
        String groupChannel = "group:123";
        testRequest.setChannelId(groupChannel);
        when(chatService.processAndSendMessage(any(MessageRequest.class)))
                .thenReturn(testResponse);

        // When: Sending to group channel
        chatController.sendMessage(groupChannel, testRequest, headerAccessor);

        // Then: Message should be processed
        ArgumentCaptor<MessageRequest> requestCaptor = ArgumentCaptor.forClass(MessageRequest.class);
        verify(chatService).processAndSendMessage(requestCaptor.capture());
        assertEquals(groupChannel, requestCaptor.getValue().getChannelId());
    }

    @Test
    public void testSendMessage_DirectMessageChannel() {
        // Given: A direct message channel
        String dmChannel = "dm:1:2";
        testRequest.setChannelId(dmChannel);
        when(chatService.processAndSendMessage(any(MessageRequest.class)))
                .thenReturn(testResponse);

        // When: Sending to DM channel
        chatController.sendMessage(dmChannel, testRequest, headerAccessor);

        // Then: Message should be processed
        ArgumentCaptor<MessageRequest> requestCaptor = ArgumentCaptor.forClass(MessageRequest.class);
        verify(chatService).processAndSendMessage(requestCaptor.capture());
        assertEquals(dmChannel, requestCaptor.getValue().getChannelId());
    }

    @Test
    public void testSendMessage_LongContent() {
        // Given: A message with long content
        String longContent = "A".repeat(4000);
        testRequest.setContent(longContent);
        when(chatService.processAndSendMessage(any(MessageRequest.class)))
                .thenReturn(testResponse);

        // When: Sending long message
        chatController.sendMessage("group:1", testRequest, headerAccessor);

        // Then: Message should be processed
        ArgumentCaptor<MessageRequest> requestCaptor = ArgumentCaptor.forClass(MessageRequest.class);
        verify(chatService).processAndSendMessage(requestCaptor.capture());
        assertEquals(longContent, requestCaptor.getValue().getContent());
    }
}
