package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.AddMemberRequest;
import com.yen.ChatAppV2.dto.ChannelDTO;
import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.dto.CreateDirectChannelRequest;
import com.yen.ChatAppV2.dto.CreateGroupChannelRequest;
import com.yen.ChatAppV2.model.Channel;
import com.yen.ChatAppV2.service.ChannelService;
import com.yen.ChatAppV2.service.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Channels", description = "Channel management endpoints (group and direct messages)")
@SecurityRequirement(name = "bearerAuth")
public class ChannelController {

    private final ChannelService channelService;
    private final ChatService chatService;

    @Operation(summary = "Get user's channels", description = "Retrieve all channels that the user is a member of")
    @GetMapping
    public ResponseEntity<List<ChannelDTO>> getUserChannels(
            @Parameter(description = "User ID", required = true) @RequestParam Long userId) {
        List<ChannelDTO> channels = channelService.getUserChannels(userId);
        return ResponseEntity.ok(channels);
    }

    @Operation(summary = "Create group channel", description = "Create a new group channel with multiple members")
    @PostMapping("/group")
    public ResponseEntity<Channel> createGroupChannel(@RequestBody @Valid CreateGroupChannelRequest request) {
        // Use default creatorId (1L) if not provided for development/testing
        Long creatorId = request.getCreatorId() != null ? request.getCreatorId() : 1L;

        Channel channel = channelService.createGroupChannel(
                request.getName(),
                creatorId,
                request.getMemberIds() != null ? request.getMemberIds() : java.util.Collections.emptyList()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @Operation(summary = "Create/get direct message channel", description = "Create a new DM channel or return existing one between two users")
    @PostMapping("/direct")
    public ResponseEntity<Channel> createDirectChannel(@RequestBody @Valid CreateDirectChannelRequest request) {
        Channel channel = channelService.getOrCreateDirectChannel(
                request.getUserId1(),
                request.getUserId2()
        );
        return ResponseEntity.ok(channel);
    }

    @Operation(summary = "Add member to channel", description = "Add a new member to an existing group channel")
    @PostMapping("/{channelId}/members")
    public ResponseEntity<Void> addMember(
            @Parameter(description = "Channel ID", required = true) @PathVariable Long channelId,
            @RequestBody @Valid AddMemberRequest request) {
        channelService.addMemberToChannel(channelId, request.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get channel messages", description = "Retrieve paginated message history for a channel")
    @GetMapping("/{channelId}/messages")
    public ResponseEntity<Page<ChatMessageDTO>> getChannelMessages(
            @Parameter(description = "Channel ID", required = true) @PathVariable Long channelId,
            @Parameter(description = "User ID", required = true) @RequestParam Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "50") int size) {

        // Convert numeric channelId to string format (group:id)
        String channelKey = "group:" + channelId;

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ChatMessageDTO> messages = chatService.getChannelMessages(channelKey, userId, pageable);
        return ResponseEntity.ok(messages);
    }
}
