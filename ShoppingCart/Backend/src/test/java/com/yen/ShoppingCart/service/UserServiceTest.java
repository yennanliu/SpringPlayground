package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.config.MessageStrings;
import com.yen.ShoppingCart.enums.ResponseStatus;
import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.exception.CustomException;
import com.yen.ShoppingCart.model.AuthenticationToken;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.ResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignupDto;
import com.yen.ShoppingCart.repository.TokenRepository;
import com.yen.ShoppingCart.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserService userService;

    /**
     *
     * The main difference between MockitoAnnotations.initMocks(this);
     * and MockitoAnnotations.openMocks(this); lies in their behavior and usage:
     *
     * - MockitoAnnotations.initMocks(this);:
     * This method initializes objects annotated with Mockito annotations (such as @Mock, @InjectMocks, etc.) for the given test class (this).
     * It should be called in a setup method annotated with @BeforeEach or @Before to initialize the mocks before each test method is executed.
     * This method has been deprecated in recent versions of Mockito in favor of MockitoAnnotations.openMocks(this);.
     *
     *
     * - MockitoAnnotations.openMocks(this);:
     * This method also initializes objects annotated with Mockito annotations for the given test class (this), similar to initMocks(this).
     * It replaces initMocks(this) and is the recommended way to initialize mocks in newer versions of Mockito.
     * It not only initializes mocks but also validates the correct usage of Mockito annotations, providing better diagnostics for potential issues.
     *
     *
     * In summary, while both methods are used to initialize Mockito annotations, openMocks(this) is the newer and preferred method, offering improved validation and error reporting. If you are using a newer version of Mockito, it's recommended to use openMocks(this) instead of initMocks(this).
     *
     */

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, tokenRepository, authenticationService);
    }

    @Test
    public void testSignUp() throws CustomException {

        // Given
        SignupDto signupDto = new SignupDto("John", "Doe", "john.doe@example.com", "password");
        User user = new User("John", "Doe", "john.doe@example.com", Role.USER, "encryptedPassword");

        //AuthenticationToken authenticationToken = new AuthenticationToken(user);

        // mock
        when(userRepository.findByEmail(signupDto.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);
        //when(authenticationService.saveConfirmationToken(any(AuthenticationToken.class))).thenReturn(authenticationToken);

        ResponseDto response = userService.signUp(signupDto);

        // Then
        assertEquals(ResponseStatus.SUCCESS.toString(), response.getStatus());
        assertEquals(MessageStrings.USER_CREATED, response.getMessage());
    }

}
