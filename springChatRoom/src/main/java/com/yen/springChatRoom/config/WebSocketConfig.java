package com.yen.springChatRoom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     *  Stomp : spring implement WebSocket via Stomp
     *
     *      - https://blog.csdn.net/qq_21294185/article/details/130657375
     *      - https://blog.csdn.net/u013749113/article/details/131455579
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        //WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint("ws").withSockJS(); // if browser not support websocket, use SockJS instead
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        /**
         * 配置訊息代理
         *  enableSimpleBroker： server端推送給client端的路徑prefix (server -> client)
         *  setApplicationDestinationPrefixes： client端發送給server端的路徑prefix (client -> server)
         *
         *  https://hackmd.io/@mko123654/HkySHSe35
         */

        //WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

}
