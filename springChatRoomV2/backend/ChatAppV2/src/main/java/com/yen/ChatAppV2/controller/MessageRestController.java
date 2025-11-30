package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.service.ChatService;
import com.yen.ChatAppV2.service.ReadReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Messages", description = "Message operations and read receipts")
@SecurityRequirement(name = "bearerAuth")
public class MessageRestController {

    private final ChatService chatService;
    private final ReadReceiptService readReceiptService;

    @Operation(summary = "Get channel messages", description = "Retrieve paginated message history for a channel")
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<Page<ChatMessageDTO>> getChannelMessages(
            @Parameter(description = "Channel ID") @PathVariable Long channelId,
            @Parameter(description = "User ID", required = true) @RequestParam Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ChatMessageDTO> messages = chatService.getChannelMessages(channelId, userId, pageable);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Mark message as read", description = "Mark a specific message as read by the user")
    @PostMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "User ID", required = true) @RequestParam Long userId,
            @Parameter(description = "Channel ID", required = true) @RequestParam Long channelId) {
        readReceiptService.markAsRead(userId, channelId, messageId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get unread count", description = "Get the number of unread messages in a channel for a user")
    @GetMapping("/channel/{channelId}/unread")
    public ResponseEntity<Integer> getUnreadCount(
            @Parameter(description = "Channel ID") @PathVariable Long channelId,
            @Parameter(description = "User ID", required = true) @RequestParam Long userId) {
        int count = readReceiptService.getUnreadCount(userId, channelId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Edit message", description = "Edit an existing message (owner only)")
    @PutMapping("/{messageId}")
    public ResponseEntity<ChatMessageDTO> editMessage(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "User ID", required = true) @RequestParam Long userId,
            @Parameter(description = "New content", required = true) @RequestParam String content) {
        ChatMessageDTO message = chatService.editMessage(messageId, userId, content);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "Delete message", description = "Delete a message (owner only)")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Parameter(description = "User ID", required = true) @RequestParam Long userId) {
        chatService.deleteMessage(messageId, userId);
        return ResponseEntity.ok().build();
    }
}
