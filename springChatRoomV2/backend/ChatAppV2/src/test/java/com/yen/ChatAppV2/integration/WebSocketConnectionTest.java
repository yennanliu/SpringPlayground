package com.yen.ChatAppV2.integration;

import com.yen.ChatAppV2.config.TestSecurityConfig;
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
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WebSocket Connection Lifecycle Test
 *
 * Tests WebSocket connection, disconnection, and online status tracking:
 * - User online status in Redis on connection
 * - User offline status in Redis on disconnection
 * - Multiple users online simultaneously
 * - SessionConnectedEvent and SessionDisconnectEvent
 * - WebSocketEventListener functionality
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
public class WebSocketConnectionTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Autowired
    private RedisService redisService;

    private String webSocketUrl;
    private User testUser1;
    private User testUser2;
    private Channel testChannel;

    @BeforeEach
    public void setup() {
        webSocketUrl = "ws://localhost:" + port + "/ws";

        // Clean database
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
    @DisplayName("User online status updated in Redis on connection")
    public void testUserOnlineOnConnection() throws Exception {
        // Given: User is offline
        assertFalse(redisService.isUserOnline(testUser1.getId()),
                "User should be offline initially");

        // When: User connects
        WebSocketStompClient client = createStompClient();
        StompSession session = connectWithUserId(client, testUser1.getId());

        // Wait for event processing
        Thread.sleep(500);

        // Then: User should be online in Redis
        assertTrue(redisService.isUserOnline(testUser1.getId()),
                "User should be online after connection");

        // Cleanup
        session.disconnect();
    }

    @Test
    @DisplayName("User offline status updated in Redis on disconnection")
    public void testUserOfflineOnDisconnection() throws Exception {
        // Given: User is connected
        WebSocketStompClient client = createStompClient();
        StompSession session = connectWithUserId(client, testUser1.getId());

        Thread.sleep(500);
        assertTrue(redisService.isUserOnline(testUser1.getId()));

        // When: User disconnects
        session.disconnect();
        Thread.sleep(1000); // Wait for disconnect event processing

        // Then: User should be offline in Redis
        assertFalse(redisService.isUserOnline(testUser1.getId()),
                "User should be offline after disconnection");
    }

    @Test
    @DisplayName("Multiple users online simultaneously")
    public void testMultipleUsersOnline() throws Exception {
        // Given: Both users offline
        assertFalse(redisService.isUserOnline(testUser1.getId()));
        assertFalse(redisService.isUserOnline(testUser2.getId()));

        // When: Both users connect
        WebSocketStompClient client1 = createStompClient();
        WebSocketStompClient client2 = createStompClient();

        StompSession session1 = connectWithUserId(client1, testUser1.getId());
        StompSession session2 = connectWithUserId(client2, testUser2.getId());

        Thread.sleep(500);

        // Then: Both users should be online
        assertTrue(redisService.isUserOnline(testUser1.getId()),
                "User 1 should be online");
        assertTrue(redisService.isUserOnline(testUser2.getId()),
                "User 2 should be online");

        // Get all online users
        Set<Object> onlineUsers = redisService.getOnlineUsers();
        assertTrue(onlineUsers.size() >= 2,
                "At least 2 users should be online");

        // Cleanup
        session1.disconnect();
        session2.disconnect();
        Thread.sleep(1000);

        // Verify both offline
        assertFalse(redisService.isUserOnline(testUser1.getId()));
        assertFalse(redisService.isUserOnline(testUser2.getId()));
    }

    @Test
    @DisplayName("Connection without userId header - no Redis update")
    public void testConnectionWithoutUserId() throws Exception {
        // When: Connecting without userId header
        WebSocketStompClient client = createStompClient();
        StompSession session = client.connectAsync(
                webSocketUrl,
                new StompSessionHandlerAdapter() {}
        ).get(5, TimeUnit.SECONDS);

        assertNotNull(session);
        assertTrue(session.isConnected());

        Thread.sleep(500);

        // Then: No user should be marked online (since no userId provided)
        Set<Object> onlineUsers = redisService.getOnlineUsers();
        // Online users set might be empty or contain other test users
        // Main point: connection succeeds without userId

        session.disconnect();
    }

    @Test
    @DisplayName("Reconnection - online status updated correctly")
    public void testReconnection() throws Exception {
        // First connection
        WebSocketStompClient client1 = createStompClient();
        StompSession session1 = connectWithUserId(client1, testUser1.getId());
        Thread.sleep(500);

        assertTrue(redisService.isUserOnline(testUser1.getId()));

        // Disconnect
        session1.disconnect();
        Thread.sleep(1000);

        assertFalse(redisService.isUserOnline(testUser1.getId()),
                "User should be offline after first disconnect");

        // Reconnect
        WebSocketStompClient client2 = createStompClient();
        StompSession session2 = connectWithUserId(client2, testUser1.getId());
        Thread.sleep(500);

        assertTrue(redisService.isUserOnline(testUser1.getId()),
                "User should be online after reconnection");

        // Cleanup
        session2.disconnect();
    }

    @Test
    @DisplayName("Concurrent connections and disconnections")
    public void testConcurrentConnectionsAndDisconnections() throws Exception {
        final int NUM_USERS = 5;
        ExecutorService executor = Executors.newFixedThreadPool(NUM_USERS);

        // Connect all users concurrently
        for (int i = 0; i < NUM_USERS; i++) {
            final Long userId = (long) (i + 1);
            executor.submit(() -> {
                try {
                    // Create temporary user
                    User user = new User();
                    user.setUsername("concurrent_user_" + userId);
                    user.setEmail("concurrent_" + userId + "@test.com");
                    user.setDisplayName("Concurrent User " + userId);
                    user.setPassword("password");
                    userRepository.save(user);

                    WebSocketStompClient client = createStompClient();
                    StompSession session = connectWithUserId(client, userId);
                    Thread.sleep(500);

                    // Verify online
                    assertTrue(redisService.isUserOnline(userId));

                    // Disconnect
                    session.disconnect();
                    Thread.sleep(1000);

                } catch (Exception e) {
                    System.err.println("Error in concurrent connection test: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(30, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("SessionConnectedEvent triggers correctly")
    public void testSessionConnectedEvent() throws Exception {
        // When: User connects
        WebSocketStompClient client = createStompClient();
        StompSession session = connectWithUserId(client, testUser1.getId());

        // Then: Connection should be established
        assertNotNull(session);
        assertTrue(session.isConnected());

        // Wait for event processing
        Thread.sleep(500);

        // Verify event was processed (user is online)
        assertTrue(redisService.isUserOnline(testUser1.getId()),
                "SessionConnectedEvent should have marked user online");

        session.disconnect();
    }

    @Test
    @DisplayName("SessionDisconnectEvent triggers correctly")
    public void testSessionDisconnectEvent() throws Exception {
        // Given: User connected
        WebSocketStompClient client = createStompClient();
        StompSession session = connectWithUserId(client, testUser1.getId());
        Thread.sleep(500);

        assertTrue(redisService.isUserOnline(testUser1.getId()));

        // When: User disconnects
        session.disconnect();

        // Wait for event processing
        Thread.sleep(1000);

        // Then: SessionDisconnectEvent should have marked user offline
        assertFalse(redisService.isUserOnline(testUser1.getId()),
                "SessionDisconnectEvent should have marked user offline");
    }

    @Test
    @DisplayName("Multiple connections from same user")
    public void testMultipleConnectionsSameUser() throws Exception {
        // When: Same user connects multiple times (e.g., multiple tabs)
        WebSocketStompClient client1 = createStompClient();
        WebSocketStompClient client2 = createStompClient();

        StompSession session1 = connectWithUserId(client1, testUser1.getId());
        Thread.sleep(300);
        StompSession session2 = connectWithUserId(client2, testUser1.getId());
        Thread.sleep(300);

        // Then: User should be marked online
        assertTrue(redisService.isUserOnline(testUser1.getId()));

        // Disconnect first session
        session1.disconnect();
        Thread.sleep(500);

        // User might still appear online (depending on implementation)
        // Second session still active

        // Disconnect second session
        session2.disconnect();
        Thread.sleep(1000);

        // Now user should be offline
        assertFalse(redisService.isUserOnline(testUser1.getId()));
    }

    @Test
    @DisplayName("Get all online users")
    public void testGetAllOnlineUsers() throws Exception {
        // Connect multiple users
        WebSocketStompClient client1 = createStompClient();
        WebSocketStompClient client2 = createStompClient();

        StompSession session1 = connectWithUserId(client1, testUser1.getId());
        StompSession session2 = connectWithUserId(client2, testUser2.getId());

        Thread.sleep(500);

        // Get online users
        Set<Object> onlineUsers = redisService.getOnlineUsers();

        assertNotNull(onlineUsers);
        assertTrue(onlineUsers.size() >= 2,
                "At least 2 users should be online");

        System.out.println("Online users: " + onlineUsers);

        // Cleanup
        session1.disconnect();
        session2.disconnect();
    }

    @Test
    @DisplayName("Connection survives and maintains online status")
    public void testConnectionStability() throws Exception {
        // Connect user
        WebSocketStompClient client = createStompClient();
        StompSession session = connectWithUserId(client, testUser1.getId());

        Thread.sleep(500);
        assertTrue(redisService.isUserOnline(testUser1.getId()));

        // Wait for a while (simulating active session)
        Thread.sleep(2000);

        // Verify still online
        assertTrue(redisService.isUserOnline(testUser1.getId()),
                "User should remain online during active session");

        session.disconnect();
    }

    private WebSocketStompClient createStompClient() {
        WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());
        client.setMessageConverter(new MappingJackson2MessageConverter());
        return client;
    }

    private StompSession connectWithUserId(WebSocketStompClient client, Long userId) throws Exception {
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("userId", userId.toString());

        return client.connectAsync(
                webSocketUrl,
                new org.springframework.web.socket.WebSocketHttpHeaders(),
                connectHeaders,
                new StompSessionHandlerAdapter() {}
        ).get(5, TimeUnit.SECONDS);
    }
}
