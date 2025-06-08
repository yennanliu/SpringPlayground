package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.controller.UserController;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.*;
import EmployeeSystem.service.AuthenticationService;
import EmployeeSystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<List<User>> responseEntity = userController.getUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        when(userService.getUserById(1)).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.getUserById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    // Add tests for other methods in the controller

    // Sample test for SignUp method
    @Test
    public void testSignUp() throws CustomException {
        SignupDto signupDto = new SignupDto();
        when(userService.signUp(signupDto)).thenReturn(new ResponseDto("success", "User created successfully"));

        ResponseDto responseDto = userController.signUp(signupDto);

        assertEquals("success", responseDto.getStatus());
    }

    // Photo Upload Controller Tests

    @Test
    public void testUpdateUserWithPhoto_Success() throws Exception {
        // Arrange
        Integer userId = 1;
        String userJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\"}";
        
        MultipartFile mockPhoto = mock(MultipartFile.class);
        when(mockPhoto.getOriginalFilename()).thenReturn("test.jpg");
        when(mockPhoto.getSize()).thenReturn(1024L);

        doNothing().when(userService).updateUserWithPhoto(any(UserCreateDto.class), eq(mockPhoto));

        // Act
        ResponseEntity<ApiResponse> response = userController.updateUserWithPhoto(userId, userJson, mockPhoto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("User has been updated successfully", response.getBody().getMessage());

        verify(userService).updateUserWithPhoto(any(UserCreateDto.class), eq(mockPhoto));
    }

    @Test
    public void testUpdateUserWithPhoto_NoPhoto() throws Exception {
        // Arrange
        Integer userId = 1;
        String userJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\"}";

        doNothing().when(userService).updateUserWithPhoto(any(UserCreateDto.class), eq(null));

        // Act
        ResponseEntity<ApiResponse> response = userController.updateUserWithPhoto(userId, userJson, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("User has been updated successfully", response.getBody().getMessage());

        verify(userService).updateUserWithPhoto(any(UserCreateDto.class), eq(null));
    }

    @Test
    public void testUpdateUserWithPhoto_InvalidJson() throws Exception {
        // Arrange
        Integer userId = 1;
        String invalidUserJson = "{invalid json}";
        
        MultipartFile mockPhoto = mock(MultipartFile.class);

        // Act
        ResponseEntity<ApiResponse> response = userController.updateUserWithPhoto(userId, invalidUserJson, mockPhoto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().isSuccess());

        verify(userService, never()).updateUserWithPhoto(any(UserCreateDto.class), any(MultipartFile.class));
    }

    @Test
    public void testUpdateUserWithPhoto_ServiceException() throws Exception {
        // Arrange
        Integer userId = 1;
        String userJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\"}";
        
        MultipartFile mockPhoto = mock(MultipartFile.class);
        when(mockPhoto.getOriginalFilename()).thenReturn("test.jpg");

        doThrow(new RuntimeException("User not found")).when(userService)
            .updateUserWithPhoto(any(UserCreateDto.class), eq(mockPhoto));

        // Act
        ResponseEntity<ApiResponse> response = userController.updateUserWithPhoto(userId, userJson, mockPhoto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().isSuccess());

        verify(userService).updateUserWithPhoto(any(UserCreateDto.class), eq(mockPhoto));
    }

    @Test
    public void testGetUserPhoto_Success() {
        // Arrange
        Integer userId = 1;
        byte[] photoBytes = "test-photo-data".getBytes();
        
        User user = new User();
        user.setId(userId);
        user.setPhoto(photoBytes);
        
        when(userService.getUserById(userId)).thenReturn(user);

        // Act
        ResponseEntity<byte[]> response = userController.getUserPhoto(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(photoBytes, response.getBody());
        
        HttpHeaders headers = response.getHeaders();
        assertEquals(MediaType.IMAGE_JPEG, headers.getContentType());

        verify(userService).getUserById(userId);
    }

    @Test
    public void testGetUserPhoto_UserNotFound() {
        // Arrange
        Integer userId = 999;
        when(userService.getUserById(userId)).thenReturn(null);

        // Act
        ResponseEntity<byte[]> response = userController.getUserPhoto(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).getUserById(userId);
    }

    @Test
    public void testGetUserPhoto_NoPhotoData() {
        // Arrange
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        user.setPhoto(null); // No photo data
        
        when(userService.getUserById(userId)).thenReturn(user);

        // Act
        ResponseEntity<byte[]> response = userController.getUserPhoto(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).getUserById(userId);
    }

    @Test
    public void testGetUserPhoto_ServiceException() {
        // Arrange
        Integer userId = 1;
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<byte[]> response = userController.getUserPhoto(userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(userService).getUserById(userId);
    }

    // Sample test for SignIn method
//    @Test
//    public void testSignIn() throws CustomException {
//        SignInDto signInDto = new SignInDto();
//        when(userService.signIn(signInDto)).thenReturn(new SignInResponseDto("success", "token"));
//
//        SignInResponseDto signInResponseDto = userController.signUp(signInDto);
//
//        assertEquals("success", signInResponseDto.getStatus());
//        assertEquals("token", signInResponseDto.getToken());
//    }

}
