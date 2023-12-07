package com.yen.springChatRoom.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import org.springframework.stereotype.Component;


/** Class for Redis channel conn */
@Component
public class RedisListenerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisListenerBean.class);

    // read setting from config
    @Value("${server.port}")
    private String serverPort;

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Value("${redis.channel.private}")
    private String privateChannel;

    /** Redis channel bean
     *
     *   1. listen Redis channel via binding (for example : container.addMessageListener)
     *   2. can do further biz logic in method
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // listen msgToAll (Redis channel)
        container.addMessageListener(listenerAdapter, new PatternTopic(msgToAll));

        LOGGER.info("Subscribe Redis channel : " + msgToAll);
        container.addMessageListener(listenerAdapter, new PatternTopic(userStatus));

        LOGGER.info("Subscribe Redis channel : " + userStatus);

        container.addMessageListener(listenerAdapter, new PatternTopic(privateChannel));
        LOGGER.info("Subscribe Redis channel : " + privateChannel);

        return container;
    }

}
