package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.service.ChatService;
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
}
