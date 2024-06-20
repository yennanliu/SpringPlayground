package EmployeeSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import EmployeeSystem.enums.ResponseStatus;
import EmployeeSystem.exception.AuthenticationFailException;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.AuthenticationToken;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.ResponseDto;
import EmployeeSystem.model.dto.SignInDto;
import EmployeeSystem.model.dto.SignInResponseDto;
import EmployeeSystem.model.dto.SignupDto;
import EmployeeSystem.repository.UserRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private AuthenticationService authenticationService;

  @InjectMocks private UserService userService;

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

  @Test
  public void testSignIn() throws CustomException {

    User user = new User();
    user.setEmail("test@example.com");
    // user.setPassword("password");
    user.setPassword("5F4DCC3B5AA765D61D8327DEB882CF99"); // hashed password

    when(userRepository.findByEmail("test@example.com")).thenReturn(user);
    when(authenticationService.getToken(user)).thenReturn(new AuthenticationToken(user));

    SignInDto signInDto = new SignInDto();
    signInDto.setEmail("test@example.com");
    signInDto.setPassword("password");

    SignInResponseDto signInResponseDto = userService.signIn(signInDto);

    assertEquals("success", signInResponseDto.getStatus());
    // assertEquals("bfe0f0b4-388c-40e6-a5e9-f4a4008e3f73", signInResponseDto.getToken());
  }

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
