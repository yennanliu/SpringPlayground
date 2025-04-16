package com.example.chatapp.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RedisConfigTest {

    @Mock
    private RedisConnectionFactory connectionFactory;

    @InjectMocks
    private RedisConfig redisConfig;

    @Test
    void redisTemplate_Success() {
        // Act
        RedisTemplate<String, Object> template = redisConfig.redisTemplate(connectionFactory);

        // Assert
        assertNotNull(template);
    }

    @Test
    void redisMessageListenerContainer_Success() {
        // Act
        RedisMessageListenerContainer container = redisConfig.redisMessageListenerContainer(connectionFactory);

        // Assert
        assertNotNull(container);
    }
} 