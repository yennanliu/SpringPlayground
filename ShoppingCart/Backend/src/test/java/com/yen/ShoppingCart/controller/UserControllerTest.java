package com.yen.ShoppingCart.controller;

import static com.yen.ShoppingCart.config.MessageStrings.USER_CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.yen.ShoppingCart.enums.ResponseStatus;
import com.yen.ShoppingCart.exception.CustomException;
import com.yen.ShoppingCart.model.dto.ResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignupDto;
import com.yen.ShoppingCart.repository.UserRepository;
import com.yen.ShoppingCart.service.AuthenticationService;
import com.yen.ShoppingCart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSignup() throws CustomException {

        // Arrange
        SignupDto signupDto = new SignupDto("John", "Doe", "john.doe@example.com", "password");
        ResponseDto expectedResponse = new ResponseDto(ResponseStatus.SUCCESS.toString(), USER_CREATED);

        // mock
        when(userService.signUp(signupDto)).thenReturn(expectedResponse);

        // Act
        ResponseDto response = userController.Signup(signupDto);

        // Assert
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("user created successfully", response.getMessage());
    }

//    @Test
//    public void testFindAllUser() throws AuthenticationFailException {
//
//        // Mock authenticationService
//        String token = "validToken";
//        doNothing().when(authenticationService).authenticate(token);
//
//        // Call the method under test
//        List<User> userList = new ArrayList<>(); // Add sample user objects to the list
//        when(userController.findAllUser(token)).thenReturn(userList);
//
//        // Verify the results
//        List<User> response = userController.findAllUser(token);
//        //assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(userList, response);
//    }

//    @Test
//    public void testGetUserProfile() throws AuthenticationFailException {
//        // Arrange
//        String token = "token";
//        User user = new User("John", "Doe", "john.doe@example.com", "password");
//        when(userService.getUserProfile(token)).thenReturn(user);
//
//        // Act
//        ResponseEntity<User> response = userController.getUserProfile(token);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//    }
//
//    @Test
//    public void testUpdateUser() {
//        // Arrange
//        User user = new User("John", "Doe", "john.doe@example.com", "password");
//        ApiResponse expectedResponse = new ApiResponse(true, "user is update");
//        when(userService.updateUser(user)).thenReturn(expectedResponse);
//
//        // Act
//        ResponseEntity response = userController.updateUser(user);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.g());
//        assertEquals(expectedResponse, response.getBody());
//    }

}
