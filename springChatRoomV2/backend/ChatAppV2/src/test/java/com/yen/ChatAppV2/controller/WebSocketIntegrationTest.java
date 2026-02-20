package com.yen.ChatAppV2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.ChatAppV2.config.TestSecurityConfig;
import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.dto.MessageRequest;
import com.yen.ChatAppV2.model.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WebSocket Integration Test
 *
 * Tests the complete WebSocket flow:
 * 1. Connection establishment
 * 2. Message sending via /app/chat/{channelId}
 * 3. Message broadcasting to /topic/channel/{channelId}
 * 4. ChatService integration
 * 5. Multiple client subscriptions
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                com.yen.ChatAppV2.ChatAppV2Application.class
        },
        properties = {
                "spring.autoconfigure.exclude=com.yen.ChatAppV2.config.SecurityConfig"
        }
)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class WebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private WebSocketStompClient stompClient;
    private String webSocketUrl;
    private final BlockingQueue<ChatMessageDTO> receivedMessages = new LinkedBlockingQueue<>();

    @BeforeEach
    public void setup() {
        webSocketUrl = "ws://localhost:" + port + "/ws";
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void testWebSocketConnection() throws Exception {
        // Given: A WebSocket client
        StompSession session = connectAndWait();

        // Then: Connection should be established
        assertNotNull(session);
        assertTrue(session.isConnected());

        // Cleanup
        session.disconnect();
    }

    @Test
    public void testSendAndReceiveMessage() throws Exception {
        // Given: A connected client subscribed to a channel
        String channelId = "group:1";
        StompSession session = connectAndWait();

        session.subscribe("/topic/channel/" + channelId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                receivedMessages.add((ChatMessageDTO) payload);
            }
        });

        // Wait for subscription to be ready
        Thread.sleep(500);

        // When: Sending a message
        MessageRequest request = new MessageRequest(
                channelId,
                1L,  // senderId
                "Hello WebSocket!",
                MessageType.TEXT
        );

        session.send("/app/chat/" + channelId, request);

        // Then: Message should be received
        ChatMessageDTO received = receivedMessages.poll(5, TimeUnit.SECONDS);
        assertNotNull(received, "Should receive message within 5 seconds");
        assertEquals("Hello WebSocket!", received.getContent());
        assertEquals(channelId, received.getChannelId());
        assertEquals(1L, received.getSenderId());
        assertEquals(MessageType.TEXT, received.getMessageType());
        assertNotNull(received.getTimestamp());

        // Cleanup
        session.disconnect();
    }

    @Test
    public void testMultipleClientsReceiveMessage() throws Exception {
        // Given: Multiple clients connected to the same channel
        String channelId = "group:1";

        BlockingQueue<ChatMessageDTO> client1Messages = new LinkedBlockingQueue<>();
        BlockingQueue<ChatMessageDTO> client2Messages = new LinkedBlockingQueue<>();

        StompSession session1 = connectAndWait();
        StompSession session2 = connectAndWait();

        // Client 1 subscription
        session1.subscribe("/topic/channel/" + channelId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                client1Messages.add((ChatMessageDTO) payload);
            }
        });

        // Client 2 subscription
        session2.subscribe("/topic/channel/" + channelId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                client2Messages.add((ChatMessageDTO) payload);
            }
        });

        // Wait for subscriptions to be ready
        Thread.sleep(500);

        // When: Client 1 sends a message
        MessageRequest request = new MessageRequest(
                channelId,
                1L,
                "Broadcast test",
                MessageType.TEXT
        );

        session1.send("/app/chat/" + channelId, request);

        // Then: Both clients should receive the message
        ChatMessageDTO client1Received = client1Messages.poll(5, TimeUnit.SECONDS);
        ChatMessageDTO client2Received = client2Messages.poll(5, TimeUnit.SECONDS);

        assertNotNull(client1Received, "Client 1 should receive message");
        assertNotNull(client2Received, "Client 2 should receive message");

        assertEquals("Broadcast test", client1Received.getContent());
        assertEquals("Broadcast test", client2Received.getContent());

        // Cleanup
        session1.disconnect();
        session2.disconnect();
    }

    @Test
    public void testMessageTypes() throws Exception {
        // Given: A connected client
        String channelId = "group:1";
        StompSession session = connectAndWait();

        session.subscribe("/topic/channel/" + channelId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                receivedMessages.add((ChatMessageDTO) payload);
            }
        });

        Thread.sleep(500);

        // When: Sending different message types
        MessageRequest textMessage = new MessageRequest(channelId, 1L, "Text message", MessageType.TEXT);
        MessageRequest imageMessage = new MessageRequest(channelId, 1L, "image.jpg", MessageType.IMAGE);
        MessageRequest fileMessage = new MessageRequest(channelId, 1L, "document.pdf", MessageType.FILE);

        session.send("/app/chat/" + channelId, textMessage);
        session.send("/app/chat/" + channelId, imageMessage);
        session.send("/app/chat/" + channelId, fileMessage);

        // Then: All messages should be received with correct types
        ChatMessageDTO msg1 = receivedMessages.poll(5, TimeUnit.SECONDS);
        ChatMessageDTO msg2 = receivedMessages.poll(5, TimeUnit.SECONDS);
        ChatMessageDTO msg3 = receivedMessages.poll(5, TimeUnit.SECONDS);

        assertNotNull(msg1);
        assertEquals(MessageType.TEXT, msg1.getMessageType());

        assertNotNull(msg2);
        assertEquals(MessageType.IMAGE, msg2.getMessageType());

        assertNotNull(msg3);
        assertEquals(MessageType.FILE, msg3.getMessageType());

        // Cleanup
        session.disconnect();
    }

    @Test
    public void testTypingIndicators() throws Exception {
        // Given: A connected client subscribed to typing indicators
        String channelId = "group:1";
        BlockingQueue<Object> typingEvents = new LinkedBlockingQueue<>();

        StompSession session = connectAndWait();

        session.subscribe("/topic/channel/" + channelId + "/typing", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Object.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                typingEvents.add(payload);
            }
        });

        Thread.sleep(500);

        // When: Sending typing start event
        session.send("/app/typing/" + channelId + "/start",
                "{\"userId\":1,\"username\":\"Alice\"}");

        // Then: Should receive typing indicator
        Object typingEvent = typingEvents.poll(5, TimeUnit.SECONDS);
        assertNotNull(typingEvent, "Should receive typing indicator");

        // Cleanup
        session.disconnect();
    }

    @Test
    public void testDifferentChannels() throws Exception {
        // Given: Clients subscribed to different channels
        String channel1 = "group:1";
        String channel2 = "group:2";

        BlockingQueue<ChatMessageDTO> channel1Messages = new LinkedBlockingQueue<>();
        BlockingQueue<ChatMessageDTO> channel2Messages = new LinkedBlockingQueue<>();

        StompSession session1 = connectAndWait();
        StompSession session2 = connectAndWait();

        session1.subscribe("/topic/channel/" + channel1, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                channel1Messages.add((ChatMessageDTO) payload);
            }
        });

        session2.subscribe("/topic/channel/" + channel2, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                channel2Messages.add((ChatMessageDTO) payload);
            }
        });

        Thread.sleep(500);

        // When: Sending messages to different channels
        MessageRequest msg1 = new MessageRequest(channel1, 1L, "Message to channel 1", MessageType.TEXT);
        MessageRequest msg2 = new MessageRequest(channel2, 2L, "Message to channel 2", MessageType.TEXT);

        session1.send("/app/chat/" + channel1, msg1);
        session2.send("/app/chat/" + channel2, msg2);

        // Then: Each client should only receive messages from their subscribed channel
        ChatMessageDTO ch1Msg = channel1Messages.poll(5, TimeUnit.SECONDS);
        ChatMessageDTO ch2Msg = channel2Messages.poll(5, TimeUnit.SECONDS);

        assertNotNull(ch1Msg);
        assertEquals("Message to channel 1", ch1Msg.getContent());
        assertEquals(channel1, ch1Msg.getChannelId());

        assertNotNull(ch2Msg);
        assertEquals("Message to channel 2", ch2Msg.getContent());
        assertEquals(channel2, ch2Msg.getChannelId());

        // Verify no cross-channel messages
        assertNull(channel1Messages.poll(1, TimeUnit.SECONDS), "Channel 1 should not receive channel 2 messages");
        assertNull(channel2Messages.poll(1, TimeUnit.SECONDS), "Channel 2 should not receive channel 1 messages");

        // Cleanup
        session1.disconnect();
        session2.disconnect();
    }

    /**
     * Helper method to connect and wait for the connection to be established
     */
    private StompSession connectAndWait() throws Exception {
        return stompClient.connectAsync(webSocketUrl, new StompSessionHandlerAdapter() {})
                .get(5, TimeUnit.SECONDS);
    }
}
