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
  public void testSignIn() throws CustomException, NoSuchAlgorithmException {

    // Create a properly hashed password for the mock user
    String plainPassword = "password";
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(plainPassword.getBytes());
    byte[] digest = md.digest();
    String hashedPassword = DatatypeConverter.printHexBinary(digest).toUpperCase();

    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword(hashedPassword); // Use hashed password

    AuthenticationToken mockToken = new AuthenticationToken(user);
    
    when(userRepository.findByEmail("test@example.com")).thenReturn(user);
    when(authenticationService.getToken(user)).thenReturn(mockToken);

    SignInDto signInDto = new SignInDto();
    signInDto.setEmail("test@example.com");
    signInDto.setPassword(plainPassword); // Use plain password in request

    SignInResponseDto signInResponseDto = userService.signIn(signInDto);

    assertEquals("success", signInResponseDto.getStatus());
    assertNotNull(signInResponseDto.getToken());
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
  public void testSignIn_WrongPassword() throws NoSuchAlgorithmException {

    // Create a hashed password for a different password
    String correctPassword = "password";
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(correctPassword.getBytes());
    byte[] digest = md.digest();
    String hashedPassword = DatatypeConverter.printHexBinary(digest).toUpperCase();

    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword(hashedPassword); // Use hashed password

    when(userRepository.findByEmail("test@example.com")).thenReturn(user);

    SignInDto signInDto = new SignInDto();
    signInDto.setEmail("test@example.com");
    signInDto.setPassword("wrongpassword"); // Different password

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
