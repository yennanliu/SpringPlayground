package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.AddMemberRequest;
import com.yen.ChatAppV2.dto.ChannelDTO;
import com.yen.ChatAppV2.dto.CreateDirectChannelRequest;
import com.yen.ChatAppV2.dto.CreateGroupChannelRequest;
import com.yen.ChatAppV2.model.Channel;
import com.yen.ChatAppV2.service.ChannelService;
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
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping
    public ResponseEntity<List<ChannelDTO>> getUserChannels(@RequestParam Long userId) {
        List<ChannelDTO> channels = channelService.getUserChannels(userId);
        return ResponseEntity.ok(channels);
    }

    @PostMapping("/group")
    public ResponseEntity<Channel> createGroupChannel(@RequestBody @Valid CreateGroupChannelRequest request) {
        Channel channel = channelService.createGroupChannel(
                request.getName(),
                request.getCreatorId(),
                request.getMemberIds()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @PostMapping("/direct")
    public ResponseEntity<Channel> createDirectChannel(@RequestBody @Valid CreateDirectChannelRequest request) {
        Channel channel = channelService.getOrCreateDirectChannel(
                request.getUserId1(),
                request.getUserId2()
        );
        return ResponseEntity.ok(channel);
    }

    @PostMapping("/{channelId}/members")
    public ResponseEntity<Void> addMember(
            @PathVariable Long channelId,
            @RequestBody @Valid AddMemberRequest request) {
        channelService.addMemberToChannel(channelId, request.getUserId());
        return ResponseEntity.ok().build();
    }
}
