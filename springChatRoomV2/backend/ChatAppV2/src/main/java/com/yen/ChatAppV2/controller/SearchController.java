package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Search", description = "Message search functionality")
@SecurityRequirement(name = "bearerAuth")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search messages", description = "Search messages globally or within a specific channel")
    @GetMapping("/messages")
    public ResponseEntity<Page<ChatMessageDTO>> searchMessages(
            @Parameter(description = "Search query", required = true) @RequestParam String query,
            @Parameter(description = "Channel ID (optional, search all channels if not provided)") @RequestParam(required = false) Long channelId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessageDTO> results = searchService.searchMessages(query, channelId, pageable);
        return ResponseEntity.ok(results);
    }
}
