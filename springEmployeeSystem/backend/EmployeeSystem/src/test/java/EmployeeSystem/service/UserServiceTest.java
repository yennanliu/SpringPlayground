package EmployeeSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import EmployeeSystem.enums.ResponseStatus;
import EmployeeSystem.enums.Role;
import EmployeeSystem.exception.AuthenticationFailException;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.AuthenticationToken;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.ResponseDto;
import EmployeeSystem.model.dto.SignInDto;
import EmployeeSystem.model.dto.SignInResponseDto;
import EmployeeSystem.model.dto.SignupDto;
import EmployeeSystem.model.dto.UserCreateDto;
import EmployeeSystem.repository.UserRepository;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

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

  // Photo Upload Tests

  @Test
  public void testUpdateUserWithPhoto_Success() throws IOException {
    // Arrange
    Integer userId = 1;
    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setId(userId);
    userCreateDto.setFirstName("John");
    userCreateDto.setLastName("Doe");
    userCreateDto.setEmail("john.doe@example.com");

    User existingUser = new User();
    existingUser.setId(userId);
    existingUser.setFirstName("OldFirstName");
    existingUser.setLastName("OldLastName");

    MultipartFile mockPhoto = mock(MultipartFile.class);
    byte[] photoBytes = "test-photo-bytes".getBytes();
    
    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(mockPhoto.isEmpty()).thenReturn(false);
    when(mockPhoto.getBytes()).thenReturn(photoBytes);
    when(userRepository.save(any(User.class))).thenReturn(existingUser);

    // Act
    userService.updateUserWithPhoto(userCreateDto, mockPhoto);

    // Assert
    verify(userRepository).findById(userId);
    verify(mockPhoto).getBytes();
    verify(userRepository).save(existingUser);
    
    assertEquals("John", existingUser.getFirstName());
    assertEquals("Doe", existingUser.getLastName());
    assertEquals("john.doe@example.com", existingUser.getEmail());
    assertArrayEquals(photoBytes, existingUser.getPhoto());
  }

  @Test
  public void testUpdateUserWithPhoto_NoPhoto() {
    // Arrange
    Integer userId = 1;
    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setId(userId);
    userCreateDto.setFirstName("John");
    userCreateDto.setLastName("Doe");

    User existingUser = new User();
    existingUser.setId(userId);
    existingUser.setFirstName("OldFirstName");

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenReturn(existingUser);

    // Act
    userService.updateUserWithPhoto(userCreateDto, null);

    // Assert
    verify(userRepository).findById(userId);
    verify(userRepository).save(existingUser);
    
    assertEquals("John", existingUser.getFirstName());
    assertEquals("Doe", existingUser.getLastName());
    assertNull(existingUser.getPhoto()); // Photo should remain null
  }

  @Test
  public void testUpdateUserWithPhoto_EmptyPhoto() {
    // Arrange
    Integer userId = 1;
    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setId(userId);
    userCreateDto.setFirstName("John");

    User existingUser = new User();
    existingUser.setId(userId);

    MultipartFile mockPhoto = mock(MultipartFile.class);
    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(mockPhoto.isEmpty()).thenReturn(true);
    when(userRepository.save(any(User.class))).thenReturn(existingUser);

    // Act
    userService.updateUserWithPhoto(userCreateDto, mockPhoto);

    // Assert
    verify(userRepository).findById(userId);
    verify(mockPhoto).isEmpty();
    //verify(mockPhoto, never()).getBytes();
    verify(userRepository).save(existingUser);
    
    assertEquals("John", existingUser.getFirstName());
    assertNull(existingUser.getPhoto()); // Photo should remain null
  }

  @Test
  public void testUpdateUserWithPhoto_UserNotFound() {
    // Arrange
    Integer userId = 999;
    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setId(userId);

    MultipartFile mockPhoto = mock(MultipartFile.class);
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      userService.updateUserWithPhoto(userCreateDto, mockPhoto);
    });

    assertEquals("User not found with ID: " + userId, exception.getMessage());
    verify(userRepository).findById(userId);
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void testUpdateUserWithPhoto_PhotoProcessingError() throws Exception {
    // Arrange
    Integer userId = 1;
    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setId(userId);

    User existingUser = new User();
    existingUser.setId(userId);

    MultipartFile mockPhoto = mock(MultipartFile.class);
    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(mockPhoto.isEmpty()).thenReturn(false);
    when(mockPhoto.getBytes()).thenThrow(new IOException("File processing error"));

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      userService.updateUserWithPhoto(userCreateDto, mockPhoto);
    });

    assertEquals("Failed to process photo: File processing error", exception.getMessage());
    verify(userRepository).findById(userId);
    verify(mockPhoto).getBytes();
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void testUpdateUser_Success() {
    // Arrange
    Integer userId = 1;
    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setId(userId);
    userCreateDto.setFirstName("John");
    userCreateDto.setLastName("Doe");
    userCreateDto.setEmail("john.doe@example.com");
    userCreateDto.setRole(Role.USER);
    userCreateDto.setDepartementId(5);
    userCreateDto.setManagerId(10);

    User existingUser = new User();
    existingUser.setId(userId);
    existingUser.setFirstName("OldFirstName");
    existingUser.setLastName("OldLastName");
    existingUser.setEmail("old@example.com");

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenReturn(existingUser);

    // Act
    userService.updateUser(userCreateDto);

    // Assert
    verify(userRepository).findById(userId);
    verify(userRepository).save(existingUser);
    
    assertEquals("John", existingUser.getFirstName());
    assertEquals("Doe", existingUser.getLastName());
    assertEquals("john.doe@example.com", existingUser.getEmail());
    assertEquals(Role.USER, existingUser.getRole());
    assertEquals(Integer.valueOf(5), existingUser.getDepartementId());
    assertEquals(Integer.valueOf(10), existingUser.getManagerId());
  }

  @Test
  public void testUpdateUser_PartialUpdate() {
    // Arrange
    Integer userId = 1;
    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setId(userId);
    userCreateDto.setFirstName("John");
    // Other fields are null - should preserve existing values

    User existingUser = new User();
    existingUser.setId(userId);
    existingUser.setFirstName("OldFirstName");
    existingUser.setLastName("ExistingLastName");
    existingUser.setEmail("existing@example.com");
    existingUser.setRole(Role.ADMIN);

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenReturn(existingUser);

    // Act
    userService.updateUser(userCreateDto);

    // Assert
    verify(userRepository).findById(userId);
    verify(userRepository).save(existingUser);
    
    assertEquals("John", existingUser.getFirstName()); // Updated
    assertEquals("ExistingLastName", existingUser.getLastName()); // Preserved
    assertEquals("existing@example.com", existingUser.getEmail()); // Preserved
    assertEquals(Role.ADMIN, existingUser.getRole()); // Preserved
  }

  @Test
  public void testUpdateUser_UserNotFound() {
    // Arrange
    Integer userId = 999;
    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      userService.updateUser(userCreateDto);
    });

    assertEquals("User not found with ID: " + userId, exception.getMessage());
    verify(userRepository).findById(userId);
    verify(userRepository, never()).save(any(User.class));
  }
}
