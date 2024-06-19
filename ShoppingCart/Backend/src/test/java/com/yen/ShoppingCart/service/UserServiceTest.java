package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.config.MessageStrings;
import com.yen.ShoppingCart.enums.ResponseStatus;
import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.exception.AuthenticationFailException;
import com.yen.ShoppingCart.exception.CustomException;
import com.yen.ShoppingCart.model.AuthenticationToken;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.ResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignInDto;
import com.yen.ShoppingCart.model.dto.user.SignInResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignupDto;
import com.yen.ShoppingCart.model.dto.user.UserCreateDto;
import com.yen.ShoppingCart.repository.TokenRepository;
import com.yen.ShoppingCart.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    //@Spy // TODO : double check
    @InjectMocks
    private UserService userService;

    /**
     *
     * --> The main difference between
     *
     *  MockitoAnnotations.initMocks(this);
     *   VS
     *  MockitoAnnotations.openMocks(this)
     *
     * - 1) MockitoAnnotations.initMocks(this)
     *
     *  This method initializes objects annotated with Mockito annotations
     *  (such as @Mock, @InjectMocks, etc.) for the given test class (this).
     *  It should be called in a setup method annotated
     *  with @BeforeEach or @Before to initialize the mocks before each test method is executed.
     *  This method has been deprecated in recent versions
     *  of Mockito in favor of MockitoAnnotations.openMocks(this);.
     *
     *
     * - 2) MockitoAnnotations.openMocks(this)
     *
     *  This method also initializes objects annotated with Mockito
     *  annotations for the given test class (this), similar to initMocks(this).
     *  It replaces initMocks(this) and is the recommended way to
     *  initialize mocks in newer versions of Mockito.
     *  It not only initializes mocks but also validates
     *  the correct usage of Mockito annotations, providing
     *  better diagnostics for potential issues.
     *
     *
     * *** In summary, while both methods are used to initialize Mockito annotations,
     *  openMocks(this) is the newer and preferred method, offering improved validation and error reporting.
     *  If you are using a newer version of Mockito,
     *  it's recommended to use openMocks(this) instead of initMocks(this).
     *
     */
    @BeforeEach
    public void setUp() {
        //  MockitoAnnotations.initMocks(this);
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, tokenRepository, authenticationService);
    }

    @Test
    public void testSignUpOK() throws CustomException {

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

    @Test
    public void testSignUpUserAlreadyExists() {

        SignupDto signupDto = new SignupDto("John", "Doe", "test@example.com", "password");
        // mock
        when(userRepository.findByEmail(signupDto.getEmail())).thenReturn(new User());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.signUp(signupDto);
        });

        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    public void testSignUp_OtherException() {

        SignupDto signupDto = new SignupDto("John", "Doe", "test@example.com", "password");
        // mock
        when(userRepository.findByEmail(signupDto.getEmail())).thenReturn(null);
        //doReturn("hashedPassword").when(userService).hashPassword(signupDto.getPassword());

        //User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), Role.USER, "hashedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("some other error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.signUp(signupDto);
        });

        assertEquals("some other error", exception.getMessage());
    }

    @Test
    public void testSignInUserNotFound() {

        SignInDto signInDto = new SignInDto("test@example.com", "password");
        // mock
        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(null);

        Exception exception = assertThrows(AuthenticationFailException.class, () -> {
            userService.signIn(signInDto);
        });

        assertEquals("user NOT existed", exception.getMessage());
    }

    @Test
    public void testSignInWrongPassword() throws NoSuchAlgorithmException {

        User user = new User();
        user.setPassword("hashedPassword");

        SignInDto signInDto = new SignInDto("test@example.com", "wrongPassword");

        // mock
        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(user);
        // TODO : check if need to mock hashPassword
        //when(userService.hashPassword(anyString())).thenReturn("wrongHashedPassword");

        Exception exception = assertThrows(AuthenticationFailException.class, () -> {
            userService.signIn(signInDto);
        });

        assertEquals("password NOT match, please check the password", exception.getMessage());
    }

    @Test
    void testSignInNoToken() throws NoSuchAlgorithmException {
        User user = new User();
        user.setPassword("5F4DCC3B5AA765D61D8327DEB882CF99"); // 5F4DCC3B5AA765D61D8327DEB882CF99 is hash password

        SignInDto signInDto = new SignInDto("test@example.com", "password");
        // mock
        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(user);
        //doReturn("hashedPassword").when(userService).hashPassword(signInDto.getPassword());
        when(authenticationService.getToken(user)).thenReturn(null);

        Exception exception = assertThrows(CustomException.class, () -> {
            userService.signIn(signInDto);
        });

        assertEquals("token NOT present", exception.getMessage());
    }

    @Test
    public void testSignInSuccess() throws NoSuchAlgorithmException, CustomException {
        User user = new User();
        user.setPassword("5F4DCC3B5AA765D61D8327DEB882CF99");

        AuthenticationToken token = new AuthenticationToken();
        token.setToken("testToken");

        SignInDto signInDto = new SignInDto("test@example.com", "password");
        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(user);
        //when(userService.hashPassword(signInDto.getPassword())).thenReturn("hashedPassword");
        when(authenticationService.getToken(user)).thenReturn(token);

        SignInResponseDto response = userService.signIn(signInDto);

        assertEquals("success", response.getStatus());
        assertEquals("testToken", response.getToken());
    }

}
