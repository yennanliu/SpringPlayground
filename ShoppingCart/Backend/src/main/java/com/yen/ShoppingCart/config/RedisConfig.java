package com.yen.ShoppingCart.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // Cache names
    public static final String CACHE_TOKENS     = "tokens";
    public static final String CACHE_PRODUCTS   = "products";
    public static final String CACHE_CATEGORIES = "categories";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper mapper = new ObjectMapper();
        // Register standard modules (JavaTimeModule, etc.) so date/time types serialize correctly
        mapper.findAndRegisterModules();

        // Restrict deserialization to known safe packages — prevents RCE via gadget chains.
        // LaissezFaireSubTypeValidator (the previous value) accepts arbitrary types and is insecure.
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.yen.ShoppingCart")
                .allowIfSubType("java.util")
                .allowIfSubType("java.lang")
                .build();

        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(mapper);

        RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> perCacheTtl = new HashMap<>();
        // token→user mapping: 15 min (matches typical session length)
        perCacheTtl.put(CACHE_TOKENS,     defaults.entryTtl(Duration.ofMinutes(15)));
        // product list: 5 min (acceptable staleness for listings)
        perCacheTtl.put(CACHE_PRODUCTS,   defaults.entryTtl(Duration.ofMinutes(5)));
        // category list: 30 min (changes very rarely)
        perCacheTtl.put(CACHE_CATEGORIES, defaults.entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaults.entryTtl(Duration.ofMinutes(10)))
                .withInitialCacheConfigurations(perCacheTtl)
                .build();
    }
}
