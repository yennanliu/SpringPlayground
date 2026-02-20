package com.yen.ChatAppV2.integration;

import com.yen.ChatAppV2.config.TestSecurityConfig;
import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.dto.MessageRequest;
import com.yen.ChatAppV2.model.*;
import com.yen.ChatAppV2.repository.*;
import com.yen.ChatAppV2.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test for Complete Message Flow
 *
 * Tests the full message lifecycle:
 * 1. WebSocket connection
 * 2. Message sent via WebSocket
 * 3. Service processing
 * 4. Database persistence
 * 5. Redis caching
 * 6. Broadcasting to subscribers
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.autoconfigure.exclude=com.yen.ChatAppV2.config.SecurityConfig"
        }
)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
public class MessageFlowIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Autowired
    private RedisService redisService;

    private WebSocketStompClient stompClient;
    private String webSocketUrl;

    private User testUser1;
    private User testUser2;
    private Channel testChannel;

    @BeforeEach
    public void setup() {
        webSocketUrl = "ws://localhost:" + port + "/ws";
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Clean database
        messageRepository.deleteAll();
        channelMemberRepository.deleteAll();
        channelRepository.deleteAll();
        userRepository.deleteAll();

        // Create test users
        testUser1 = new User();
        testUser1.setUsername("alice");
        testUser1.setEmail("alice@test.com");
        testUser1.setDisplayName("Alice");
        testUser1.setPassword("password123");
        testUser1 = userRepository.save(testUser1);

        testUser2 = new User();
        testUser2.setUsername("bob");
        testUser2.setEmail("bob@test.com");
        testUser2.setDisplayName("Bob");
        testUser2.setPassword("password456");
        testUser2 = userRepository.save(testUser2);

        // Create test channel
        testChannel = new Channel();
        testChannel.setName("Test Channel");
        testChannel.setChannelType(ChannelType.GROUP);
        testChannel.setCreatedBy(testUser1.getId());
        testChannel = channelRepository.save(testChannel);

        // Add users to channel
        ChannelMember member1 = new ChannelMember();
        member1.setChannelId("group:" + testChannel.getId());
        member1.setUserId(testUser1.getId());
        member1.setJoinedAt(LocalDateTime.now());
        channelMemberRepository.save(member1);

        ChannelMember member2 = new ChannelMember();
        member2.setChannelId("group:" + testChannel.getId());
        member2.setUserId(testUser2.getId());
        member2.setJoinedAt(LocalDateTime.now());
        channelMemberRepository.save(member2);
    }

    @Test
    @DisplayName("Full message flow: WebSocket -> Service -> Database -> Redis -> Broadcast")
    public void testCompleteMessageFlow() throws Exception {
        // Given: A connected client subscribed to channel
        String channelId = "group:" + testChannel.getId();
        BlockingQueue<ChatMessageDTO> receivedMessages = new LinkedBlockingQueue<>();

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

        Thread.sleep(500); // Wait for subscription

        // When: Sending a message
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                "Integration test message",
                MessageType.TEXT
        );

        session.send("/app/chat/" + channelId, request);

        // Then 1: Message should be broadcasted via WebSocket
        ChatMessageDTO receivedDto = receivedMessages.poll(5, TimeUnit.SECONDS);
        assertNotNull(receivedDto, "Should receive message via WebSocket");
        assertEquals("Integration test message", receivedDto.getContent());
        assertEquals(testUser1.getId(), receivedDto.getSenderId());
        assertEquals("Alice", receivedDto.getSenderName());
        assertEquals(MessageType.TEXT, receivedDto.getMessageType());
        assertNotNull(receivedDto.getTimestamp());
        assertNotNull(receivedDto.getId());

        // Then 2: Message should be persisted to database
        List<Message> dbMessages = messageRepository.findAll();
        assertEquals(1, dbMessages.size());
        Message dbMessage = dbMessages.get(0);
        assertEquals("Integration test message", dbMessage.getContent());
        assertEquals(testUser1.getId(), dbMessage.getSenderId());
        assertEquals(channelId, dbMessage.getChannelId());
        assertEquals(MessageType.TEXT, dbMessage.getMessageType());
        assertNotNull(dbMessage.getCreatedAt());

        // Then 3: Message should be cached in Redis
        List<Object> cachedMessages = redisService.getCachedMessages(channelId, 10);
        assertFalse(cachedMessages.isEmpty(), "Redis should have cached message");

        // Cleanup
        session.disconnect();
    }

    @Test
    @DisplayName("Message field validation: content, timestamp, sender")
    public void testMessageFieldValidation() throws Exception {
        // Given
        String channelId = "group:" + testChannel.getId();
        BlockingQueue<ChatMessageDTO> receivedMessages = new LinkedBlockingQueue<>();

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

        // When: Sending message with special content
        String specialContent = "Hello 👋 World! Special chars: @#$%^&*()";
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                specialContent,
                MessageType.TEXT
        );

        session.send("/app/chat/" + channelId, request);

        // Then: All fields should be correct
        ChatMessageDTO received = receivedMessages.poll(5, TimeUnit.SECONDS);
        assertNotNull(received);
        assertEquals(specialContent, received.getContent());
        assertNotNull(received.getId());
        assertEquals(testUser1.getId(), received.getSenderId());
        assertEquals("Alice", received.getSenderName());
        assertNotNull(received.getTimestamp());
        assertTrue(received.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));

        // Database verification
        Message dbMessage = messageRepository.findById(received.getId()).orElse(null);
        assertNotNull(dbMessage);
        assertEquals(specialContent, dbMessage.getContent());
        assertEquals(testUser1.getId(), dbMessage.getSenderId());

        session.disconnect();
    }

    @Test
    @DisplayName("Different message types: TEXT, IMAGE, FILE")
    public void testDifferentMessageTypes() throws Exception {
        // Given
        String channelId = "group:" + testChannel.getId();
        BlockingQueue<ChatMessageDTO> receivedMessages = new LinkedBlockingQueue<>();

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
        MessageRequest textMsg = new MessageRequest(channelId, testUser1.getId(), "Text message", MessageType.TEXT);
        MessageRequest imageMsg = new MessageRequest(channelId, testUser1.getId(), "image.jpg", MessageType.IMAGE);
        MessageRequest fileMsg = new MessageRequest(channelId, testUser1.getId(), "document.pdf", MessageType.FILE);

        session.send("/app/chat/" + channelId, textMsg);
        session.send("/app/chat/" + channelId, imageMsg);
        session.send("/app/chat/" + channelId, fileMsg);

        // Then: All message types should be received
        ChatMessageDTO msg1 = receivedMessages.poll(5, TimeUnit.SECONDS);
        ChatMessageDTO msg2 = receivedMessages.poll(5, TimeUnit.SECONDS);
        ChatMessageDTO msg3 = receivedMessages.poll(5, TimeUnit.SECONDS);

        assertNotNull(msg1);
        assertEquals(MessageType.TEXT, msg1.getMessageType());
        assertEquals("Text message", msg1.getContent());

        assertNotNull(msg2);
        assertEquals(MessageType.IMAGE, msg2.getMessageType());
        assertEquals("image.jpg", msg2.getContent());

        assertNotNull(msg3);
        assertEquals(MessageType.FILE, msg3.getMessageType());
        assertEquals("document.pdf", msg3.getContent());

        // Verify all persisted to database
        List<Message> dbMessages = messageRepository.findAll();
        assertEquals(3, dbMessages.size());

        session.disconnect();
    }

    @Test
    @DisplayName("Special characters and emoji handling")
    public void testSpecialCharactersAndEmoji() throws Exception {
        // Given
        String channelId = "group:" + testChannel.getId();
        BlockingQueue<ChatMessageDTO> receivedMessages = new LinkedBlockingQueue<>();

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

        // When: Sending message with emojis and special chars
        String complexContent = "Hello 😀 🎉 🚀 测试中文 Ñoño <script>alert('xss')</script>";
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                complexContent,
                MessageType.TEXT
        );

        session.send("/app/chat/" + channelId, request);

        // Then: Content should be preserved
        ChatMessageDTO received = receivedMessages.poll(5, TimeUnit.SECONDS);
        assertNotNull(received);
        assertEquals(complexContent, received.getContent());

        // Database verification
        Message dbMessage = messageRepository.findById(received.getId()).orElse(null);
        assertNotNull(dbMessage);
        assertEquals(complexContent, dbMessage.getContent());

        session.disconnect();
    }

    @Test
    @DisplayName("Message persistence with correct database fields")
    public void testDatabasePersistence() throws Exception {
        // Given
        String channelId = "group:" + testChannel.getId();
        StompSession session = connectAndWait();
        BlockingQueue<ChatMessageDTO> receivedMessages = new LinkedBlockingQueue<>();

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

        // When: Sending multiple messages
        for (int i = 1; i <= 5; i++) {
            MessageRequest request = new MessageRequest(
                    channelId,
                    testUser1.getId(),
                    "Message " + i,
                    MessageType.TEXT
            );
            session.send("/app/chat/" + channelId, request);
            Thread.sleep(100);
        }

        // Wait for all messages
        for (int i = 0; i < 5; i++) {
            receivedMessages.poll(5, TimeUnit.SECONDS);
        }

        // Then: All messages should be in database
        List<Message> dbMessages = messageRepository.findAll();
        assertEquals(5, dbMessages.size());

        // Verify each message has correct fields
        for (Message msg : dbMessages) {
            assertNotNull(msg.getId());
            assertEquals(channelId, msg.getChannelId());
            assertEquals(testUser1.getId(), msg.getSenderId());
            assertNotNull(msg.getContent());
            assertTrue(msg.getContent().startsWith("Message "));
            assertEquals(MessageType.TEXT, msg.getMessageType());
            assertNotNull(msg.getCreatedAt());
            assertNull(msg.getEditedAt());
            assertFalse(msg.isDeleted());
        }

        session.disconnect();
    }

    @Test
    @DisplayName("Redis caching of messages")
    public void testRedisCaching() throws Exception {
        // Given
        String channelId = "group:" + testChannel.getId();
        StompSession session = connectAndWait();
        BlockingQueue<ChatMessageDTO> receivedMessages = new LinkedBlockingQueue<>();

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

        // When: Sending 10 messages
        for (int i = 1; i <= 10; i++) {
            MessageRequest request = new MessageRequest(
                    channelId,
                    testUser1.getId(),
                    "Cached message " + i,
                    MessageType.TEXT
            );
            session.send("/app/chat/" + channelId, request);
            Thread.sleep(50);
        }

        // Wait for all messages
        for (int i = 0; i < 10; i++) {
            receivedMessages.poll(5, TimeUnit.SECONDS);
        }

        Thread.sleep(1000); // Give Redis time to cache

        // Then: Redis should have cached all messages (last 100)
        List<Object> cachedMessages = redisService.getCachedMessages(channelId, 100);
        assertEquals(10, cachedMessages.size(), "Redis should cache all 10 messages");

        session.disconnect();
    }

    private StompSession connectAndWait() throws Exception {
        return stompClient.connectAsync(webSocketUrl, new StompSessionHandlerAdapter() {})
                .get(5, TimeUnit.SECONDS);
    }
}
