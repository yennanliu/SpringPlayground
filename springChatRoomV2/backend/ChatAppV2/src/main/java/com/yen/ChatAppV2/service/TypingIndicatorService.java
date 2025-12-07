package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.TypingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypingIndicatorService {

    private final RedisService redisService;
    private final SimpMessagingTemplate messagingTemplate;
    private static final long TYPING_TIMEOUT = 5000; // 5 seconds

    public void userStartedTyping(Long userId, String channelId, String username) {
        String key = "channel:" + channelId + ":typing";
        redisService.addTypingUser(key, userId, TYPING_TIMEOUT);

        // Broadcast typing event
        TypingEvent event = new TypingEvent(userId, username, true);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId + "/typing", event);

        log.debug("User {} started typing in channel {}", userId, channelId);
    }

    public void userStoppedTyping(Long userId, String channelId) {
        String key = "channel:" + channelId + ":typing";
        redisService.removeTypingUser(key, userId);

        // Broadcast stopped typing event
        TypingEvent event = new TypingEvent(userId, null, false);
        messagingTemplate.convertAndSend("/topic/channel/" + channelId + "/typing", event);

        log.debug("User {} stopped typing in channel {}", userId, channelId);
    }
}
