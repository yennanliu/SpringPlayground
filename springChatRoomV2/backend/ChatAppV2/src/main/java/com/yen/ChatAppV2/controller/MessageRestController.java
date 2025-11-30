package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.service.ChatService;
import com.yen.ChatAppV2.service.ReadReceiptService;
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
public class MessageRestController {

    private final ChatService chatService;
    private final ReadReceiptService readReceiptService;

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<Page<ChatMessageDTO>> getChannelMessages(
            @PathVariable Long channelId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ChatMessageDTO> messages = chatService.getChannelMessages(channelId, userId, pageable);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long messageId,
            @RequestParam Long userId,
            @RequestParam Long channelId) {
        readReceiptService.markAsRead(userId, channelId, messageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/channel/{channelId}/unread")
    public ResponseEntity<Integer> getUnreadCount(
            @PathVariable Long channelId,
            @RequestParam Long userId) {
        int count = readReceiptService.getUnreadCount(userId, channelId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<ChatMessageDTO> editMessage(
            @PathVariable Long messageId,
            @RequestParam Long userId,
            @RequestParam String content) {
        ChatMessageDTO message = chatService.editMessage(messageId, userId, content);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long messageId,
            @RequestParam Long userId) {
        chatService.deleteMessage(messageId, userId);
        return ResponseEntity.ok().build();
    }
}
