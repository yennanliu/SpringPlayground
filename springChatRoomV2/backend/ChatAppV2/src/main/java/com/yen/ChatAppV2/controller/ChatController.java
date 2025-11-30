package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.MessageRequest;
import com.yen.ChatAppV2.dto.TypingRequest;
import com.yen.ChatAppV2.service.ChatService;
import com.yen.ChatAppV2.service.TypingIndicatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final TypingIndicatorService typingIndicatorService;

    @MessageMapping("/chat/{channelId}")
    public void sendMessage(@DestinationVariable Long channelId,
                          @Payload MessageRequest request,
                          SimpMessageHeaderAccessor headerAccessor) {
        log.info("Received message for channel {}: {}", channelId, request);

        // Ensure channelId matches
        request.setChannelId(channelId);

        // Process and broadcast
        chatService.processAndSendMessage(request);
    }

    @MessageMapping("/typing/{channelId}/start")
    public void startTyping(@DestinationVariable Long channelId, @Payload TypingRequest request) {
        log.debug("User {} started typing in channel {}", request.getUserId(), channelId);
        typingIndicatorService.userStartedTyping(request.getUserId(), channelId, request.getUsername());
    }

    @MessageMapping("/typing/{channelId}/stop")
    public void stopTyping(@DestinationVariable Long channelId, @Payload TypingRequest request) {
        log.debug("User {} stopped typing in channel {}", request.getUserId(), channelId);
        typingIndicatorService.userStoppedTyping(request.getUserId(), channelId);
    }
}
