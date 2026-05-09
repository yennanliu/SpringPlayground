package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.config.MessageStrings;
import com.yen.ShoppingCart.config.RedisConfig;
import com.yen.ShoppingCart.exception.AuthenticationFailException;
import com.yen.ShoppingCart.model.AuthenticationToken;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.repository.TokenRepository;
import com.yen.ShoppingCart.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

// https://github.com/webtutsplus/ecommerce-backend/blob/master/src/main/java/com/webtutsplus/ecommerce/service/AuthenticationService.java

@Service
public class AuthenticationService {

    @Autowired
    TokenRepository repository;

    // Evict the cached entry for this token when a new token is saved (e.g. after re-login)
    @CacheEvict(value = RedisConfig.CACHE_TOKENS, key = "#authenticationToken.token")
    public void saveConfirmationToken(AuthenticationToken authenticationToken) {

        repository.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {

        return repository.findTokenByUser(user);
    }

    // Approach 3: cache token→User lookups (called on every authenticated request)
    @Cacheable(value = RedisConfig.CACHE_TOKENS, key = "#token", unless = "#result == null")
    public User getUser(String token) {
        AuthenticationToken authenticationToken = repository.findTokenByToken(token);
        if (Helper.notNull(authenticationToken)) {
            if (Helper.notNull(authenticationToken.getUser())) {
                return authenticationToken.getUser();
            }
        }
        return null;
    }

    public void authenticate(String token) throws AuthenticationFailException {

        if (!Helper.notNull(token)) {
            throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_PRESENT);
        }
        if (!Helper.notNull(getUser(token))) {
            throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_VALID);
        }
    }

}
