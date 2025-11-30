package com.yen.ChatAppV2.config;

import com.yen.ChatAppV2.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final RedisService redisService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // Extract userId from headers if provided
        String userId = headerAccessor.getFirstNativeHeader("userId");
        if (userId != null) {
            try {
                Long userIdLong = Long.parseLong(userId);
                redisService.setUserOnline(userIdLong, sessionId);
                log.info("User {} connected with session: {}", userIdLong, sessionId);
            } catch (NumberFormatException e) {
                log.warn("Invalid userId in connection header: {}", userId);
            }
        }

        log.info("New WebSocket connection: {}", sessionId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // Extract userId from session attributes
        String userId = headerAccessor.getFirstNativeHeader("userId");
        if (userId != null) {
            try {
                Long userIdLong = Long.parseLong(userId);
                redisService.setUserOffline(userIdLong);
                log.info("User {} disconnected from session: {}", userIdLong, sessionId);
            } catch (NumberFormatException e) {
                log.warn("Invalid userId in disconnection: {}", userId);
            }
        }

        log.info("WebSocket connection closed: {}", sessionId);
    }
}
