package com.example.chatapp.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebSocketConfigTest {

    @Mock
    private MessageBrokerRegistry registry;

    @Mock
    private StompEndpointRegistry endpointRegistry;

    @Mock
    private StompEndpointRegistry.StompEndpointRegistration registration;

    @InjectMocks
    private WebSocketConfig webSocketConfig;

    @Test
    void configureMessageBroker_Success() {
        // Act
        webSocketConfig.configureMessageBroker(registry);

        // Assert
        verify(registry).enableSimpleBroker("/topic", "/queue");
        verify(registry).setApplicationDestinationPrefixes("/app");
        verify(registry).setUserDestinationPrefix("/user");
    }

    @Test
    void registerStompEndpoints_Success() {
        // Arrange
        when(endpointRegistry.addEndpoint("/ws")).thenReturn(registration);
        when(registration.setAllowedOriginPatterns("*")).thenReturn(registration);

        // Act
        webSocketConfig.registerStompEndpoints(endpointRegistry);

        // Assert
        verify(endpointRegistry).addEndpoint("/ws");
        verify(registration).setAllowedOriginPatterns("*");
        verify(registration).withSockJS();
    }
} 