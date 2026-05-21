package com.yen.ShoppingCart.e2e;

import com.yen.ShoppingCart.config.RedisConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Replaces the Redis-backed CacheManager with an in-memory one for all e2e tests.
 * Imported explicitly with @Import(TestCacheConfig.class) on each test class.
 */
@TestConfiguration
public class TestCacheConfig {

    @Bean
    @Primary
    public CacheManager testCacheManager() {
        return new ConcurrentMapCacheManager(
                RedisConfig.CACHE_TOKENS,
                RedisConfig.CACHE_PRODUCTS,
                RedisConfig.CACHE_CATEGORIES);
    }
}
