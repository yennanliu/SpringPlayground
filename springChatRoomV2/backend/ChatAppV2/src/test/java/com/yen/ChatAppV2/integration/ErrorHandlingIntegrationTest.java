package com.yen.ChatAppV2.integration;

import com.yen.ChatAppV2.config.TestSecurityConfig;
import com.yen.ChatAppV2.dto.MessageRequest;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.exception.UnauthorizedException;
import com.yen.ChatAppV2.model.*;
import com.yen.ChatAppV2.repository.*;
import com.yen.ChatAppV2.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Error Handling Integration Test
 *
 * Tests error scenarios and exception handling:
 * - Unauthorized channel access
 * - Editing other users' messages
 * - Deleting non-existent messages
 * - Empty message content
 * - Message size limits
 * - Invalid user IDs
 * - Invalid channel IDs
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
public class ErrorHandlingIntegrationTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    private User testUser1;
    private User testUser2;
    private Channel testChannel;
    private String channelId;

    @BeforeEach
    public void setup() {
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

        channelId = "group:" + testChannel.getId();

        // Only user1 is member
        ChannelMember member1 = new ChannelMember();
        member1.setChannelId(channelId);
        member1.setUserId(testUser1.getId());
        member1.setJoinedAt(LocalDateTime.now());
        channelMemberRepository.save(member1);
    }

    @Test
    @DisplayName("Unauthorized: User sends message to channel they are not a member of")
    public void testUnauthorizedChannelAccess() {
        // Given: User2 is NOT a member of the channel
        MessageRequest request = new MessageRequest(
                channelId,
                testUser2.getId(),
                "Unauthorized message",
                MessageType.TEXT
        );

        // When/Then: Should throw UnauthorizedException
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> chatService.processAndSendMessage(request),
                "Should throw UnauthorizedException for non-members"
        );

        assertTrue(exception.getMessage().contains("not a member"),
                "Exception message should mention membership");

        // Verify message not saved
        assertEquals(0, messageRepository.count(),
                "No message should be saved");
    }

    @Test
    @DisplayName("Unauthorized: User edits another user's message")
    public void testUnauthorizedMessageEdit() {
        // Given: User1 sends a message
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                "Original message",
                MessageType.TEXT
        );

        var sentMessage = chatService.processAndSendMessage(request);

        // Add user2 to channel
        ChannelMember member2 = new ChannelMember();
        member2.setChannelId(channelId);
        member2.setUserId(testUser2.getId());
        member2.setJoinedAt(LocalDateTime.now());
        channelMemberRepository.save(member2);

        // When/Then: User2 tries to edit user1's message
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> chatService.editMessage(sentMessage.getId(), testUser2.getId(), "Hacked message"),
                "Should throw UnauthorizedException when editing others' messages"
        );

        assertTrue(exception.getMessage().contains("own messages"),
                "Exception should mention ownership");

        // Verify message unchanged
        Message dbMessage = messageRepository.findById(sentMessage.getId()).orElseThrow();
        assertEquals("Original message", dbMessage.getContent(),
                "Message content should remain unchanged");
    }

    @Test
    @DisplayName("Unauthorized: User deletes another user's message")
    public void testUnauthorizedMessageDelete() {
        // Given: User1 sends a message
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                "Message to be protected",
                MessageType.TEXT
        );

        var sentMessage = chatService.processAndSendMessage(request);

        // Add user2 to channel
        ChannelMember member2 = new ChannelMember();
        member2.setChannelId(channelId);
        member2.setUserId(testUser2.getId());
        member2.setJoinedAt(LocalDateTime.now());
        channelMemberRepository.save(member2);

        // When/Then: User2 tries to delete user1's message
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> chatService.deleteMessage(sentMessage.getId(), testUser2.getId()),
                "Should throw UnauthorizedException when deleting others' messages"
        );

        assertTrue(exception.getMessage().contains("own messages"));

        // Verify message not deleted
        Message dbMessage = messageRepository.findById(sentMessage.getId()).orElseThrow();
        assertFalse(dbMessage.isDeleted(),
                "Message should not be marked as deleted");
        assertEquals("Message to be protected", dbMessage.getContent());
    }

    @Test
    @DisplayName("NotFound: Edit non-existent message")
    public void testEditNonExistentMessage() {
        // When/Then: Try to edit message that doesn't exist
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> chatService.editMessage(99999L, testUser1.getId(), "New content"),
                "Should throw NotFoundException for non-existent message"
        );

        assertTrue(exception.getMessage().contains("not found"),
                "Exception should mention message not found");
    }

    @Test
    @DisplayName("NotFound: Delete non-existent message")
    public void testDeleteNonExistentMessage() {
        // When/Then: Try to delete message that doesn't exist
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> chatService.deleteMessage(99999L, testUser1.getId()),
                "Should throw NotFoundException for non-existent message"
        );

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("NotFound: Get messages from non-existent channel")
    public void testGetMessagesFromNonExistentChannel() {
        // When/Then: Try to get messages from channel user is not in
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> chatService.getChannelMessages("group:99999", testUser1.getId(), null),
                "Should throw UnauthorizedException for non-member access"
        );

        assertTrue(exception.getMessage().contains("not a member"));
    }

    @Test
    @DisplayName("NotFound: Send message with non-existent user")
    public void testSendMessageWithNonExistentUser() {
        // Given: Invalid user ID
        MessageRequest request = new MessageRequest(
                channelId,
                99999L,
                "Ghost message",
                MessageType.TEXT
        );

        // Add phantom user to channel members (to pass membership check)
        ChannelMember phantomMember = new ChannelMember();
        phantomMember.setChannelId(channelId);
        phantomMember.setUserId(99999L);
        phantomMember.setJoinedAt(LocalDateTime.now());
        channelMemberRepository.save(phantomMember);

        // When/Then: Should throw NotFoundException
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> chatService.processAndSendMessage(request),
                "Should throw NotFoundException for non-existent user"
        );

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    @DisplayName("Empty message content handling")
    public void testEmptyMessageContent() {
        // Note: Validation should happen at controller level (@Valid)
        // But test service behavior if it gets through
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                "",
                MessageType.TEXT
        );

        // This might succeed at service level but fail validation at controller
        // Service level just processes what it receives
        var result = chatService.processAndSendMessage(request);
        assertNotNull(result);
        assertEquals("", result.getContent());
    }

    @Test
    @DisplayName("Very long message content (max size test)")
    public void testVeryLongMessageContent() {
        // Create message with 5000 characters (at limit)
        String longContent = "A".repeat(5000);

        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                longContent,
                MessageType.TEXT
        );

        // Should succeed (within limit)
        var result = chatService.processAndSendMessage(request);
        assertNotNull(result);
        assertEquals(5000, result.getContent().length());

        // Verify persisted
        Message dbMessage = messageRepository.findById(result.getId()).orElseThrow();
        assertEquals(5000, dbMessage.getContent().length());
    }

    @Test
    @DisplayName("Extremely long message content (over 5000 chars)")
    public void testExtremelyLongMessageContent() {
        // Create message over the typical limit
        String veryLongContent = "B".repeat(6000);

        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                veryLongContent,
                MessageType.TEXT
        );

        // This should either:
        // 1. Be rejected by validation (@Size annotation)
        // 2. Or succeed if no validation (database column limit would apply)

        // For now, test that service handles it (validation is at controller level)
        try {
            var result = chatService.processAndSendMessage(request);
            assertNotNull(result);
            // If it succeeds, content should be saved
        } catch (Exception e) {
            // If it fails, that's also acceptable behavior
            assertTrue(e.getMessage().contains("size") ||
                      e.getMessage().contains("length") ||
                      e.getMessage().contains("too long"),
                      "Exception should mention size/length issue");
        }
    }

    @Test
    @DisplayName("Invalid channel ID format")
    public void testInvalidChannelIdFormat() {
        // Try with malformed channel ID
        MessageRequest request = new MessageRequest(
                "invalid_channel_format",
                testUser1.getId(),
                "Test message",
                MessageType.TEXT
        );

        // Should throw exception (not a member of non-existent channel)
        assertThrows(
                UnauthorizedException.class,
                () -> chatService.processAndSendMessage(request)
        );
    }

    @Test
    @DisplayName("Null message type defaults to TEXT")
    public void testNullMessageType() {
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                "Message without type",
                null
        );

        var result = chatService.processAndSendMessage(request);
        assertNotNull(result);
        // Should default to TEXT or handle gracefully
        assertNotNull(result.getMessageType());
    }

    @Test
    @DisplayName("Edit message multiple times")
    public void testEditMessageMultipleTimes() {
        // Given: A message
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                "Original",
                MessageType.TEXT
        );

        var sentMessage = chatService.processAndSendMessage(request);

        // Edit 1
        var edited1 = chatService.editMessage(sentMessage.getId(), testUser1.getId(), "Edit 1");
        assertEquals("Edit 1", edited1.getContent());

        // Edit 2
        var edited2 = chatService.editMessage(sentMessage.getId(), testUser1.getId(), "Edit 2");
        assertEquals("Edit 2", edited2.getContent());

        // Edit 3
        var edited3 = chatService.editMessage(sentMessage.getId(), testUser1.getId(), "Final");
        assertEquals("Final", edited3.getContent());

        // Verify editedAt is set
        Message dbMessage = messageRepository.findById(sentMessage.getId()).orElseThrow();
        assertNotNull(dbMessage.getEditedAt(),
                "EditedAt timestamp should be set");
        assertEquals("Final", dbMessage.getContent());
    }

    @Test
    @DisplayName("Delete already deleted message")
    public void testDeleteAlreadyDeletedMessage() {
        // Given: A deleted message
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                "To be deleted",
                MessageType.TEXT
        );

        var sentMessage = chatService.processAndSendMessage(request);
        chatService.deleteMessage(sentMessage.getId(), testUser1.getId());

        // Verify deleted
        Message dbMessage = messageRepository.findById(sentMessage.getId()).orElseThrow();
        assertTrue(dbMessage.isDeleted());

        // When: Try to delete again
        // Should either succeed (idempotent) or throw error
        assertDoesNotThrow(() ->
                chatService.deleteMessage(sentMessage.getId(), testUser1.getId()),
                "Deleting already deleted message should be idempotent"
        );
    }

    @Test
    @DisplayName("Edit deleted message")
    public void testEditDeletedMessage() {
        // Given: A deleted message
        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                "To be deleted and edited",
                MessageType.TEXT
        );

        var sentMessage = chatService.processAndSendMessage(request);
        chatService.deleteMessage(sentMessage.getId(), testUser1.getId());

        // When: Try to edit deleted message
        // Behavior depends on business logic - might allow or disallow
        assertDoesNotThrow(() -> {
            var edited = chatService.editMessage(sentMessage.getId(), testUser1.getId(), "Edited after delete");
            // If allowed, content should be updated
            assertNotNull(edited);
        });
    }

    @Test
    @DisplayName("Special SQL characters in message content")
    public void testSqlInjectionAttempt() {
        // Try SQL injection in content
        String maliciousContent = "'; DROP TABLE messages; --";

        MessageRequest request = new MessageRequest(
                channelId,
                testUser1.getId(),
                maliciousContent,
                MessageType.TEXT
        );

        // Should be safely escaped by JPA
        var result = chatService.processAndSendMessage(request);
        assertNotNull(result);
        assertEquals(maliciousContent, result.getContent());

        // Verify table still exists (message saved)
        Message dbMessage = messageRepository.findById(result.getId()).orElseThrow();
        assertEquals(maliciousContent, dbMessage.getContent());

        // Verify repository still works (table not dropped)
        assertEquals(1, messageRepository.count());
    }
}
