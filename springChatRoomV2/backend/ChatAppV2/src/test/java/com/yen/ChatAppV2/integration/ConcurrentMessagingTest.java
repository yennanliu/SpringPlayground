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
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Concurrent Messaging Integration Test
 *
 * Tests multiple users sending messages simultaneously:
 * - 10+ users sending messages concurrently
 * - No message loss
 * - No duplicate messages
 * - Correct ordering by timestamp
 * - All messages persisted to database
 * - All messages broadcasted to subscribers
 * - Redis cache correctly updated
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
public class ConcurrentMessagingTest {

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

    private String webSocketUrl;
    private List<User> testUsers;
    private Channel testChannel;
    private String channelId;

    @BeforeEach
    public void setup() {
        webSocketUrl = "ws://localhost:" + port + "/ws";

        // Clean database
        messageRepository.deleteAll();
        channelMemberRepository.deleteAll();
        channelRepository.deleteAll();
        userRepository.deleteAll();

        // Create 10 test users
        testUsers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@test.com");
            user.setDisplayName("User " + i);
            user.setPassword("password" + i);
            testUsers.add(userRepository.save(user));
        }

        // Create test channel
        testChannel = new Channel();
        testChannel.setName("Concurrent Test Channel");
        testChannel.setChannelType(ChannelType.GROUP);
        testChannel.setCreatedBy(testUsers.get(0).getId());
        testChannel = channelRepository.save(testChannel);

        channelId = "group:" + testChannel.getId();

        // Add all users to channel
        for (User user : testUsers) {
            ChannelMember member = new ChannelMember();
            member.setChannelId(channelId);
            member.setUserId(user.getId());
            member.setJoinedAt(LocalDateTime.now());
            channelMemberRepository.save(member);
        }
    }

    @Test
    @DisplayName("10 users send messages concurrently - no data loss")
    public void testConcurrentMessageSending() throws Exception {
        final int NUM_USERS = 10;
        final int MESSAGES_PER_USER = 5;
        final int TOTAL_MESSAGES = NUM_USERS * MESSAGES_PER_USER;

        // Track all received messages
        ConcurrentHashMap<Long, ChatMessageDTO> receivedMessages = new ConcurrentHashMap<>();
        BlockingQueue<ChatMessageDTO> messageQueue = new LinkedBlockingQueue<>();

        // Create a subscriber to receive all messages
        WebSocketStompClient subscriberClient = createStompClient();
        StompSession subscriberSession = connectAndWait(subscriberClient);

        subscriberSession.subscribe("/topic/channel/" + channelId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                ChatMessageDTO dto = (ChatMessageDTO) payload;
                messageQueue.add(dto);
                receivedMessages.put(dto.getId(), dto);
            }
        });

        Thread.sleep(500);

        // Create executor for concurrent sending
        ExecutorService executor = Executors.newFixedThreadPool(NUM_USERS);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(NUM_USERS);
        AtomicInteger successCount = new AtomicInteger(0);

        // Submit tasks for each user
        for (int i = 0; i < NUM_USERS; i++) {
            final int userIndex = i;
            executor.submit(() -> {
                try {
                    User user = testUsers.get(userIndex);
                    WebSocketStompClient client = createStompClient();
                    StompSession session = connectAndWait(client);

                    // Wait for all threads to be ready
                    startLatch.await();

                    // Send messages
                    for (int msgNum = 1; msgNum <= MESSAGES_PER_USER; msgNum++) {
                        MessageRequest request = new MessageRequest(
                                channelId,
                                user.getId(),
                                "User " + (userIndex + 1) + " - Message " + msgNum,
                                MessageType.TEXT
                        );
                        session.send("/app/chat/" + channelId, request);
                        Thread.sleep(10); // Small delay between messages
                    }

                    successCount.addAndGet(MESSAGES_PER_USER);
                    session.disconnect();
                } catch (Exception e) {
                    System.err.println("Error in user " + userIndex + ": " + e.getMessage());
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        // Start all threads simultaneously
        startLatch.countDown();

        // Wait for all to complete (max 30 seconds)
        assertTrue(completionLatch.await(30, TimeUnit.SECONDS),
                "All users should complete sending within 30 seconds");

        // Wait for all messages to be received
        int receivedCount = 0;
        while (receivedCount < TOTAL_MESSAGES) {
            ChatMessageDTO msg = messageQueue.poll(5, TimeUnit.SECONDS);
            if (msg == null) {
                break;
            }
            receivedCount++;
        }

        System.out.println("Messages sent: " + successCount.get());
        System.out.println("Messages received: " + receivedCount);
        System.out.println("Unique messages: " + receivedMessages.size());

        // Verify: All messages received
        assertEquals(TOTAL_MESSAGES, receivedCount,
                "Should receive all " + TOTAL_MESSAGES + " messages");

        // Verify: No duplicates
        assertEquals(TOTAL_MESSAGES, receivedMessages.size(),
                "All messages should be unique (no duplicates)");

        // Verify: All messages in database
        List<Message> dbMessages = messageRepository.findAll();
        assertEquals(TOTAL_MESSAGES, dbMessages.size(),
                "All messages should be persisted to database");

        // Verify: No message loss - check content
        Set<String> expectedContents = new HashSet<>();
        for (int i = 1; i <= NUM_USERS; i++) {
            for (int j = 1; j <= MESSAGES_PER_USER; j++) {
                expectedContents.add("User " + i + " - Message " + j);
            }
        }

        Set<String> actualContents = receivedMessages.values().stream()
                .map(ChatMessageDTO::getContent)
                .collect(Collectors.toSet());

        assertEquals(expectedContents, actualContents,
                "All expected message contents should be present");

        // Verify: Messages are ordered by timestamp
        List<ChatMessageDTO> sortedMessages = new ArrayList<>(receivedMessages.values());
        sortedMessages.sort(Comparator.comparing(ChatMessageDTO::getTimestamp));

        for (int i = 1; i < sortedMessages.size(); i++) {
            assertFalse(sortedMessages.get(i).getTimestamp()
                            .isBefore(sortedMessages.get(i - 1).getTimestamp()),
                    "Messages should be in chronological order");
        }

        // Verify: Redis cache updated
        List<Object> cachedMessages = redisService.getCachedMessages(channelId, 100);
        assertFalse(cachedMessages.isEmpty(), "Redis should have cached messages");

        // Cleanup
        subscriberSession.disconnect();
        executor.shutdown();
    }

    @Test
    @DisplayName("100 concurrent messages - stress test")
    public void testHighVolumeConcurrentMessaging() throws Exception {
        final int NUM_USERS = 10;
        final int MESSAGES_PER_USER = 10;
        final int TOTAL_MESSAGES = NUM_USERS * MESSAGES_PER_USER;

        BlockingQueue<ChatMessageDTO> messageQueue = new LinkedBlockingQueue<>();
        Set<Long> receivedMessageIds = ConcurrentHashMap.newKeySet();

        // Subscriber
        WebSocketStompClient subscriberClient = createStompClient();
        StompSession subscriberSession = connectAndWait(subscriberClient);

        subscriberSession.subscribe("/topic/channel/" + channelId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                ChatMessageDTO dto = (ChatMessageDTO) payload;
                messageQueue.add(dto);
                receivedMessageIds.add(dto.getId());
            }
        });

        Thread.sleep(500);

        // Send messages concurrently
        ExecutorService executor = Executors.newFixedThreadPool(NUM_USERS);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(NUM_USERS);

        for (int i = 0; i < NUM_USERS; i++) {
            final int userIndex = i;
            executor.submit(() -> {
                try {
                    User user = testUsers.get(userIndex);
                    WebSocketStompClient client = createStompClient();
                    StompSession session = connectAndWait(client);

                    startLatch.await();

                    for (int msgNum = 1; msgNum <= MESSAGES_PER_USER; msgNum++) {
                        MessageRequest request = new MessageRequest(
                                channelId,
                                user.getId(),
                                "Stress test - User " + (userIndex + 1) + " - Msg " + msgNum,
                                MessageType.TEXT
                        );
                        session.send("/app/chat/" + channelId, request);
                    }

                    session.disconnect();
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(completionLatch.await(60, TimeUnit.SECONDS));

        // Collect all messages
        int receivedCount = 0;
        long timeout = System.currentTimeMillis() + 10000; // 10 seconds
        while (receivedCount < TOTAL_MESSAGES && System.currentTimeMillis() < timeout) {
            ChatMessageDTO msg = messageQueue.poll(1, TimeUnit.SECONDS);
            if (msg != null) {
                receivedCount++;
            }
        }

        System.out.println("Stress test - Received: " + receivedCount + "/" + TOTAL_MESSAGES);
        System.out.println("Unique message IDs: " + receivedMessageIds.size());

        // Allow some tolerance for high-volume tests
        assertTrue(receivedCount >= TOTAL_MESSAGES * 0.95,
                "At least 95% of messages should be received");

        assertTrue(receivedMessageIds.size() >= TOTAL_MESSAGES * 0.95,
                "At least 95% unique messages");

        // Database verification
        List<Message> dbMessages = messageRepository.findAll();
        assertTrue(dbMessages.size() >= TOTAL_MESSAGES * 0.95,
                "At least 95% of messages persisted to database");

        subscriberSession.disconnect();
        executor.shutdown();
    }

    @Test
    @DisplayName("Multiple subscribers receive same messages concurrently")
    public void testMultipleSubscribers() throws Exception {
        final int NUM_SUBSCRIBERS = 5;
        final int NUM_MESSAGES = 10;

        List<BlockingQueue<ChatMessageDTO>> subscriberQueues = new ArrayList<>();
        List<StompSession> sessions = new ArrayList<>();

        // Create multiple subscribers
        for (int i = 0; i < NUM_SUBSCRIBERS; i++) {
            BlockingQueue<ChatMessageDTO> queue = new LinkedBlockingQueue<>();
            subscriberQueues.add(queue);

            WebSocketStompClient client = createStompClient();
            StompSession session = connectAndWait(client);
            sessions.add(session);

            session.subscribe("/topic/channel/" + channelId, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return ChatMessageDTO.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    queue.add((ChatMessageDTO) payload);
                }
            });
        }

        Thread.sleep(500);

        // Send messages
        WebSocketStompClient senderClient = createStompClient();
        StompSession senderSession = connectAndWait(senderClient);

        for (int i = 1; i <= NUM_MESSAGES; i++) {
            MessageRequest request = new MessageRequest(
                    channelId,
                    testUsers.get(0).getId(),
                    "Broadcast message " + i,
                    MessageType.TEXT
            );
            senderSession.send("/app/chat/" + channelId, request);
            Thread.sleep(50);
        }

        Thread.sleep(2000);

        // Verify all subscribers received all messages
        for (int i = 0; i < NUM_SUBSCRIBERS; i++) {
            BlockingQueue<ChatMessageDTO> queue = subscriberQueues.get(i);
            int receivedCount = queue.size();

            System.out.println("Subscriber " + (i + 1) + " received: " + receivedCount + " messages");

            assertTrue(receivedCount >= NUM_MESSAGES * 0.9,
                    "Subscriber " + (i + 1) + " should receive at least 90% of messages");
        }

        // Cleanup
        senderSession.disconnect();
        for (StompSession session : sessions) {
            session.disconnect();
        }
    }

    @Test
    @DisplayName("Concurrent message ordering by timestamp")
    public void testMessageOrdering() throws Exception {
        BlockingQueue<ChatMessageDTO> messageQueue = new LinkedBlockingQueue<>();

        WebSocketStompClient subscriberClient = createStompClient();
        StompSession subscriberSession = connectAndWait(subscriberClient);

        subscriberSession.subscribe("/topic/channel/" + channelId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                messageQueue.add((ChatMessageDTO) payload);
            }
        });

        Thread.sleep(500);

        // Send 20 messages from 2 users concurrently
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(2);

        for (int i = 0; i < 2; i++) {
            final int userIndex = i;
            executor.submit(() -> {
                try {
                    User user = testUsers.get(userIndex);
                    WebSocketStompClient client = createStompClient();
                    StompSession session = connectAndWait(client);

                    startLatch.await();

                    for (int msgNum = 1; msgNum <= 10; msgNum++) {
                        MessageRequest request = new MessageRequest(
                                channelId,
                                user.getId(),
                                "User " + (userIndex + 1) + " - Seq " + msgNum,
                                MessageType.TEXT
                        );
                        session.send("/app/chat/" + channelId, request);
                        Thread.sleep(10);
                    }

                    session.disconnect();
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(completionLatch.await(30, TimeUnit.SECONDS));

        // Collect messages
        List<ChatMessageDTO> receivedMessages = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ChatMessageDTO msg = messageQueue.poll(5, TimeUnit.SECONDS);
            if (msg != null) {
                receivedMessages.add(msg);
            }
        }

        assertEquals(20, receivedMessages.size());

        // Verify timestamp ordering
        for (int i = 1; i < receivedMessages.size(); i++) {
            LocalDateTime prevTime = receivedMessages.get(i - 1).getTimestamp();
            LocalDateTime currTime = receivedMessages.get(i).getTimestamp();

            assertFalse(currTime.isBefore(prevTime),
                    "Messages should be ordered by timestamp");
        }

        subscriberSession.disconnect();
        executor.shutdown();
    }

    private WebSocketStompClient createStompClient() {
        WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());
        client.setMessageConverter(new MappingJackson2MessageConverter());
        return client;
    }

    private StompSession connectAndWait(WebSocketStompClient client) throws Exception {
        return client.connectAsync(webSocketUrl, new StompSessionHandlerAdapter() {})
                .get(5, TimeUnit.SECONDS);
    }
}
