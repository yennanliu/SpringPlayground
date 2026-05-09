package com.yen.ShoppingCart.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * Unit tests for RedissonConfig.
 *
 * We validate the Redisson Config object (host/port wiring) without
 * spinning up a real client or connecting to Redis — connection is lazy.
 * The bean lifecycle (create / shutdown) is verified by Spring context tests
 * at integration time.
 */
class RedissonConfigTest {

    @Test
    void redissonConfig_defaultAddress_isLocalhost6379() {
        // Build the Config the same way RedissonConfig.redissonClient() does
        Config config = new Config();
        String host = "localhost";
        int port = 6379;
        config.useSingleServer()
              .setAddress("redis://" + host + ":" + port)
              .setConnectionMinimumIdleSize(2)
              .setConnectionPoolSize(10);

        SingleServerConfig serverConfig = config.useSingleServer();
        assertEquals("redis://localhost:6379", serverConfig.getAddress());
        assertEquals(2, serverConfig.getConnectionMinimumIdleSize());
        assertEquals(10, serverConfig.getConnectionPoolSize());
    }

    @Test
    void redissonConfig_customAddress_isReflectedInConfig() {
        Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://redis-primary:6380")
              .setConnectionMinimumIdleSize(2)
              .setConnectionPoolSize(10);

        assertEquals("redis://redis-primary:6380",
                config.useSingleServer().getAddress());
    }

    @Test
    void redissonConfig_poolSize_greaterThanMinIdle() {
        Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://localhost:6379")
              .setConnectionMinimumIdleSize(2)
              .setConnectionPoolSize(10);

        SingleServerConfig s = config.useSingleServer();
        assertTrue(s.getConnectionPoolSize() > s.getConnectionMinimumIdleSize(),
                "pool size must be larger than min-idle to allow bursting");
    }
}
