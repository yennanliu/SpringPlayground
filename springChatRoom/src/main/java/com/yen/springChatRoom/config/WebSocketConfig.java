package com.yen.springChatRoom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final String APP_PRIFIX = "/app";
    private final String TOPIC_PRIFIX = "/topic";
    private final String PRIVATE_PRIFIX = "/private";

    private final String WS_PATH = "ws";

    /**
     * Stomp : spring implement WebSocket via Stomp
     * <p>
     * - https://blog.csdn.net/qq_21294185/article/details/130657375
     * - https://blog.csdn.net/u013749113/article/details/131455579
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        //WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint(WS_PATH).withSockJS(); // if browser not support websocket, use SockJS instead
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        //WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.setApplicationDestinationPrefixes(APP_PRIFIX); // (client -> server)
        registry.enableSimpleBroker(TOPIC_PRIFIX, PRIVATE_PRIFIX); // (server -> client)
    }

    //
//    @Bean
//    public SimpMessagingTemplate messagingTemplate() {
//
//        //return new SimpMessagingTemplate(/* your message broker relay, e.g., "/topic" */);
//        return new SimpMessagingTemplate("/private/");
//    }

}
