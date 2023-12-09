package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.exception.AuthenticationFailException;
import com.yen.ShoppingCart.model.AuthenticationToken;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationServiceTest {

    @Mock
    TokenRepository tokenRepository;

    @InjectMocks
    AuthenticationService authenticationService;

    User user1;

    User user2;

    AuthenticationToken token1;

    @BeforeEach
    public void setUp(){

        System.out.println("setup ...");

        user1 = new User("f_name", "l_name", "email", Role.USER, "pwd");
        token1 = new AuthenticationToken(1, "my_token", now(), user1);

        user2 = new User();
    }

    @Test
    public void shouldGetUserFromTokenIfExist(){

        // mock
        Mockito.when(tokenRepository.findTokenByToken("my_token")).thenReturn(token1);

        assertEquals(authenticationService.getUser("my_token"), user1);
        assertEquals(authenticationService.getUser("my_token").getEmail(), "email");
        assertEquals(authenticationService.getUser("my_token").getLastName(), "l_name");
    }

    @Test
    public void shouldReturnNullFromTokenIfNotExist(){

        // mock
        Mockito.when(tokenRepository.findTokenByToken("some_token")).thenReturn(null);

        assertEquals(authenticationService.getUser("some_token"), null);
    }

    @Test
    public void shouldThrow_AUTH_TOEKN_NOT_PRESENT_WhenAuthenticateIfNullToken(){

        // mock
        Mockito.when(tokenRepository.findTokenByToken(null)).thenReturn(null);

        Exception exception = assertThrows(AuthenticationFailException.class, () -> {
            authenticationService.authenticate(null);
        } );

        assertEquals(exception.getMessage(),"authentication token not present");
    }

    @Test
    public void shouldThrow_AUTH_TOEKN_NOT_PRESENT_WhenAuthenticateIfNotValidToken(){

        // mock
        Mockito.when(tokenRepository.findTokenByToken("some_not_valid_token")).thenReturn(null);

        Exception exception = assertThrows(AuthenticationFailException.class, () -> {
            authenticationService.authenticate("some_not_valid_token");
        } );

        assertEquals(exception.getMessage(),"authentication token not valid");
    }

    @Test
    public void shouldReturnNotThrowExceptionIfAuthWithValidToken(){

        // mock
        Mockito.when(tokenRepository.findTokenByToken("my_token")).thenReturn(token1);

        // test Exception is NOT thrown
        // https://www.w3docs.com/snippets/java/how-to-test-that-no-exception-is-thrown.html
        assertDoesNotThrow(() -> {
            authenticationService.authenticate("my_token");
        });
    }

}