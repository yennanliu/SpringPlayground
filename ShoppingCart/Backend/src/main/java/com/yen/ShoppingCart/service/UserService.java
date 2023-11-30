package com.yen.ShoppingCart.service;

// https://github.com/webtutsplus/ecommerce-backend/blob/master/src/main/java/com/webtutsplus/ecommerce/service/UserService.java

import com.yen.ShoppingCart.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import static com.webtutsplus.ecommerce.config.MessageStrings.USER_CREATED;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

//    public ResponseDto signUp(SignupDto signupDto)  throws CustomException{
//
//    }

}
