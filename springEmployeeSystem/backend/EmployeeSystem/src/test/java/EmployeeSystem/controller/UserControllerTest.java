package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.controller.UserController;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.*;
import EmployeeSystem.service.AuthenticationService;
import EmployeeSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        ResponseDto responseDto = userController.Signup(signupDto);

        assertEquals("success", responseDto.getStatus());
    }

    // Sample test for SignIn method
    @Test
    public void testSignIn() throws CustomException {
        SignInDto signInDto = new SignInDto();
        when(userService.signIn(signInDto)).thenReturn(new SignInResponseDto("success", "token"));

        SignInResponseDto signInResponseDto = userController.Signup(signInDto);

        assertEquals("success", signInResponseDto.getStatus());
        assertEquals("token", signInResponseDto.getToken());
    }

}
