package EmployeeSystem.service;

import static org.junit.jupiter.api.Assertions.*;

import EmployeeSystem.exception.AuthenticationFailException;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.AuthenticationToken;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.SignInDto;
import EmployeeSystem.model.dto.SignInResponseDto;
import EmployeeSystem.model.dto.SignupDto;
import EmployeeSystem.model.dto.ResponseDto;
import EmployeeSystem.enums.ResponseStatus;
import EmployeeSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSignUp() throws CustomException {
        SignupDto signupDto = new SignupDto();
        signupDto.setEmail("test@example.com");
        signupDto.setPassword("password");

        ResponseDto responseDto = userService.signUp(signupDto);

        assertEquals(ResponseStatus.SUCCESS.toString(), responseDto.getStatus());
        assertEquals("user created successfully", responseDto.getMessage());
    }

//    @Test
//    public void testSignIn() throws CustomException {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//
//        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
//        when(authenticationService.getToken(user)).thenReturn(new AuthenticationToken(user));
//
//        SignInDto signInDto = new SignInDto();
//        signInDto.setEmail("test@example.com");
//        signInDto.setPassword("password");
//
//        SignInResponseDto signInResponseDto = userService.signIn(signInDto);
//
//        assertEquals("success", signInResponseDto.getStatus());
//        assertEquals("token", signInResponseDto.getToken());
//    }

    @Test
    public void testSignIn_UserNotFound() {

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        SignInDto signInDto = new SignInDto();
        signInDto.setEmail("test@example.com");
        signInDto.setPassword("password");

        assertThrows(AuthenticationFailException.class, () -> userService.signIn(signInDto));
    }

    @Test
    public void testSignIn_WrongPassword() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        SignInDto signInDto = new SignInDto();
        signInDto.setEmail("test@example.com");
        signInDto.setPassword("wrongpassword");

        assertThrows(AuthenticationFailException.class, () -> userService.signIn(signInDto));
    }

    @Test
    public void testHashPassword() throws NoSuchAlgorithmException {

        String pwd = "password";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pwd.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

        String hashedPassword = userService.hashPassword(pwd);

        assertNotNull(hashedPassword);
        assertEquals(myHash, hashedPassword);
    }

}