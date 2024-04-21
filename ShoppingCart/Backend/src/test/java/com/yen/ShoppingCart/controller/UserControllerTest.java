package com.yen.ShoppingCart.controller;

import com.yen.ShoppingCart.enums.ResponseStatus;
import com.yen.ShoppingCart.exception.CustomException;
import com.yen.ShoppingCart.model.dto.ResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignupDto;
import com.yen.ShoppingCart.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.yen.ShoppingCart.config.MessageStrings.USER_CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
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

}
