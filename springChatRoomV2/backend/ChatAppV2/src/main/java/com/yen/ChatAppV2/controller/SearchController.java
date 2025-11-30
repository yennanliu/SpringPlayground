package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.service.SearchService;
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
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/messages")
    public ResponseEntity<Page<ChatMessageDTO>> searchMessages(
            @RequestParam String query,
            @RequestParam(required = false) Long channelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessageDTO> results = searchService.searchMessages(query, channelId, pageable);
        return ResponseEntity.ok(results);
    }
}
