package com.yen.ShoppingCart.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for RedisConfig constants and cache name correctness.
 * No Spring context needed — just validates the values our cache annotations depend on.
 */
class RedisConfigTest {

    @Test
    void cacheNames_shouldMatchExpectedValues() {
        assertEquals("tokens",     RedisConfig.CACHE_TOKENS);
        assertEquals("products",   RedisConfig.CACHE_PRODUCTS);
        assertEquals("categories", RedisConfig.CACHE_CATEGORIES);
    }

    @Test
    void cacheNames_shouldBeDistinct() {
        assertNotEquals(RedisConfig.CACHE_TOKENS,   RedisConfig.CACHE_PRODUCTS);
        assertNotEquals(RedisConfig.CACHE_TOKENS,   RedisConfig.CACHE_CATEGORIES);
        assertNotEquals(RedisConfig.CACHE_PRODUCTS, RedisConfig.CACHE_CATEGORIES);
    }

    @Test
    void cacheNames_shouldNotBeNullOrBlank() {
        assertNotNull(RedisConfig.CACHE_TOKENS);
        assertNotNull(RedisConfig.CACHE_PRODUCTS);
        assertNotNull(RedisConfig.CACHE_CATEGORIES);
        assertFalse(RedisConfig.CACHE_TOKENS.isBlank());
        assertFalse(RedisConfig.CACHE_PRODUCTS.isBlank());
        assertFalse(RedisConfig.CACHE_CATEGORIES.isBlank());
    }
}
