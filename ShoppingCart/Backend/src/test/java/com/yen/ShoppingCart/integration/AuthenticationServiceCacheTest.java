package com.yen.ShoppingCart.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.yen.ShoppingCart.config.RedisConfig;
import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.model.AuthenticationToken;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.repository.TokenRepository;
import com.yen.ShoppingCart.service.AuthenticationService;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Integration test verifying that @Cacheable / @CacheEvict on AuthenticationService
 * work correctly. Uses an in-memory ConcurrentMapCacheManager — no Redis required.
 *
 * Key behaviour under test:
 *  - getUser(token)        → cached on first call; repo NOT hit on subsequent calls
 *  - getUser(null result)  → null is NOT cached (unless = "#result == null")
 *  - saveConfirmationToken → evicts the cached entry for that token
 */
@SpringJUnitConfig(AuthenticationServiceCacheTest.TestConfig.class)
class AuthenticationServiceCacheTest {

    @EnableCaching
    @Configuration
    static class TestConfig {

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(
                    RedisConfig.CACHE_TOKENS,
                    RedisConfig.CACHE_PRODUCTS,
                    RedisConfig.CACHE_CATEGORIES
            );
        }

        @Bean
        public TokenRepository tokenRepository() {
            return Mockito.mock(TokenRepository.class);
        }

        @Bean
        public AuthenticationService authenticationService() {
            // Spring auto-wires the TokenRepository bean into AuthenticationService.repository
            return new AuthenticationService();
        }
    }

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private CacheManager cacheManager;

    private User user;
    private AuthenticationToken token;

    @BeforeEach
    void setUp() {
        Mockito.reset(tokenRepository);
        // clear cache between tests
        cacheManager.getCache(RedisConfig.CACHE_TOKENS).clear();

        user = new User("fn", "ln", "test@example.com", Role.USER, "pwd");
        token = new AuthenticationToken(1, "valid-token", new Date(), user);
    }

    @Test
    void getUser_secondCall_shouldHitCacheNotRepo() {
        when(tokenRepository.findTokenByToken("valid-token")).thenReturn(token);

        User first  = authenticationService.getUser("valid-token");
        User second = authenticationService.getUser("valid-token");

        assertNotNull(first);
        assertEquals(first, second);
        // repo must have been called exactly once; second call served from cache
        verify(tokenRepository, times(1)).findTokenByToken("valid-token");
    }

    @Test
    void getUser_nullResult_shouldNotBeCached() {
        when(tokenRepository.findTokenByToken("bad-token")).thenReturn(null);

        User first  = authenticationService.getUser("bad-token");
        User second = authenticationService.getUser("bad-token");

        assertNull(first);
        assertNull(second);
        // null was not cached → repo hit on both calls
        verify(tokenRepository, times(2)).findTokenByToken("bad-token");
    }

    @Test
    void saveConfirmationToken_shouldEvictCachedEntry() {
        when(tokenRepository.findTokenByToken("valid-token")).thenReturn(token);

        // prime the cache
        authenticationService.getUser("valid-token");
        verify(tokenRepository, times(1)).findTokenByToken("valid-token");

        // evict via save
        authenticationService.saveConfirmationToken(token);
        verify(tokenRepository, times(1)).save(token);

        // next getUser must go to repo again (cache was evicted)
        authenticationService.getUser("valid-token");
        verify(tokenRepository, times(2)).findTokenByToken("valid-token");
    }

    @Test
    void getUser_differentTokens_shouldBeCachedIndependently() {
        User user2 = new User("fn2", "ln2", "other@example.com", Role.USER, "pwd2");
        AuthenticationToken token2 = new AuthenticationToken(2, "other-token", new Date(), user2);

        when(tokenRepository.findTokenByToken("valid-token")).thenReturn(token);
        when(tokenRepository.findTokenByToken("other-token")).thenReturn(token2);

        authenticationService.getUser("valid-token");
        authenticationService.getUser("other-token");

        // call each a second time — should be cache hits
        authenticationService.getUser("valid-token");
        authenticationService.getUser("other-token");

        verify(tokenRepository, times(1)).findTokenByToken("valid-token");
        verify(tokenRepository, times(1)).findTokenByToken("other-token");
    }
}
