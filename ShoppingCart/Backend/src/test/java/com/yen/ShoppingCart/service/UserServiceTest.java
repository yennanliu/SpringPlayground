package com.yen.ShoppingCart.service;

import com.yen.ShoppingCart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AuthenticationService authenticationService;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void setUp(){

        System.out.println("setup ...");
    }

    @Test
    public void testHashPassword() throws NoSuchAlgorithmException {

        String password = "my_pwd";
        System.out.println(userService.hashPassword(password));
        assertEquals(userService.hashPassword(password), "ACB1A4CFD9326C9ED77D535BDB2F115D");
    }

    // TODO : find a way test it
//    @Test
//    public void shouldThrowNoSuchAlgorithmExceptionWithInvalidPassword() throws NoSuchAlgorithmException {
//
//        String password = null;
//        System.out.println(userService.hashPassword(password));
//        assertEquals(userService.hashPassword(password), "ACB1A4CFD9326C9ED77D535BDB2F115D");
//    }

}