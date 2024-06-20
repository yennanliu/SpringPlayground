package com.yen.ShoppingCart.service;

import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.yen.ShoppingCart.config.MessageStrings;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationServiceTest {

    @Mock
    TokenRepository tokenRepository;

    @InjectMocks
    AuthenticationService authenticationService;

    private User user1;

    private User user2;

    private AuthenticationToken token1;

    private AuthenticationToken token;

    @BeforeEach
    public void setUp(){

        System.out.println("setup ...");

        user1 = new User("f_name", "l_name", "email", Role.USER, "pwd");
        token1 = new AuthenticationToken(1, "my_token", now(), user1);

        user2 = new User();

        token = new AuthenticationToken();
        token.setToken("valid-token");
        token.setUser(user1);
    }

    @Test
    public void saveConfirmationToken() {

        authenticationService.saveConfirmationToken(token);
        verify(tokenRepository, times(1)).save(token);
    }

    @Test
    public void getToken() {

        // mock
        when(tokenRepository.findTokenByUser(user1)).thenReturn(token);

        AuthenticationToken result = authenticationService.getToken(user1);

        assertNotNull(result);
        assertEquals(token, result);
    }

    @Test
    public void getUser() {

        // mock
        when(tokenRepository.findTokenByToken(any())).thenReturn(token);
        User result = authenticationService.getUser("valid-token");

        assertNotNull(result);
        assertEquals(user1, result);
    }

    @Test
    public void getUser_NotFound() {

        // mock
        when(tokenRepository.findTokenByToken("invalid-token")).thenReturn(null);
        User result = authenticationService.getUser("invalid-token");
        assertNull(result);
    }

    @Test
    public void authenticate_ValidToken() {

        // mock
        when(tokenRepository.findTokenByToken("valid-token")).thenReturn(token);
        assertDoesNotThrow(() -> authenticationService.authenticate("valid-token"));
    }

    @Test
    public void authenticate_TokenNotPresent() {

        Exception exception = assertThrows(AuthenticationFailException.class, () -> {
            authenticationService.authenticate(null);
        });
        assertEquals(MessageStrings.AUTH_TOEKN_NOT_PRESENT, exception.getMessage());
    }

    @Test
    public void authenticate_TokenNotValid() {

        // mock
        when(tokenRepository.findTokenByToken("invalid-token")).thenReturn(null);
        Exception exception = assertThrows(AuthenticationFailException.class, () -> {
            authenticationService.authenticate("invalid-token");
        });
        assertEquals(MessageStrings.AUTH_TOEKN_NOT_VALID, exception.getMessage());
    }

    @Test
    public void shouldGetUserFromTokenIfExist(){

        // mock
        when(tokenRepository.findTokenByToken("my_token")).thenReturn(token1);

        assertEquals(authenticationService.getUser("my_token"), user1);
        assertEquals(authenticationService.getUser("my_token").getEmail(), "email");
        assertEquals(authenticationService.getUser("my_token").getLastName(), "l_name");
    }

    @Test
    public void shouldReturnNullFromTokenIfNotExist(){

        // mock
        when(tokenRepository.findTokenByToken("some_token")).thenReturn(null);

        assertEquals(authenticationService.getUser("some_token"), null);
    }

    @Test
    public void shouldThrow_AUTH_TOEKN_NOT_PRESENT_WhenAuthenticateIfNullToken(){

        // mock
        when(tokenRepository.findTokenByToken(null)).thenReturn(null);

        Exception exception = assertThrows(AuthenticationFailException.class, () -> {
            authenticationService.authenticate(null);
        } );

        assertEquals(exception.getMessage(),"authentication token not present");
    }

    @Test
    public void shouldThrow_AUTH_TOEKN_NOT_PRESENT_WhenAuthenticateIfNotValidToken(){

        // mock
        when(tokenRepository.findTokenByToken("some_not_valid_token")).thenReturn(null);

        Exception exception = assertThrows(AuthenticationFailException.class, () -> {
            authenticationService.authenticate("some_not_valid_token");
        } );

        assertEquals(exception.getMessage(),"authentication token not valid");
    }

    @Test
    public void shouldReturnNotThrowExceptionIfAuthWithValidToken(){

        // mock
        when(tokenRepository.findTokenByToken("my_token")).thenReturn(token1);

        // test Exception is NOT thrown
        // https://www.w3docs.com/snippets/java/how-to-test-that-no-exception-is-thrown.html
        assertDoesNotThrow(() -> {
            authenticationService.authenticate("my_token");
        });
    }

}