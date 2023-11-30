package com.yen.ShoppingCart.controller;

import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.exception.CustomException;
import com.yen.ShoppingCart.model.AuthenticationToken;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.ResponseDto;
import com.yen.ShoppingCart.model.dto.user.SignupDto;
import com.yen.ShoppingCart.repository.UserRepository;
import com.yen.ShoppingCart.service.AuthenticationService;
import com.yen.ShoppingCart.service.UserService;
import com.yen.ShoppingCart.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yen.ShoppingCart.enums.ResponseStatus;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.yen.ShoppingCart.config.MessageStrings.USER_CREATED;

/** Controller for signIn/singUp */
// https://github.com/webtutsplus/ecommerce-backend/blob/master/src/main/java/com/webtutsplus/ecommerce/controller/UserController.java

@Slf4j
@RequestMapping("user")
// TODO : check if we can do CORS setting in cofig
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    public ResponseDto signUp(SignupDto signupDto)  throws CustomException {

        // Check to see if the current email address has already been registered
        if (Helper.notNull(userRepository.findByEmail(signupDto.getEmail()))){
            // If the email address has been registered then throw an exception.
            throw new CustomException("User already exists");
        }

        String encryptedPassword = signupDto.getPassword();
        try{
            // Step 1) encrypt password
            encryptedPassword = hashPassword(signupDto.getPassword());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            log.error("hashing password failed {}", e.getMessage());
        }

        // Step 2) save to DB, generate token
        // prepare user instance
        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), Role.USER, encryptedPassword);
        User createdUser = null;
        try{

            // save the User
            createdUser = userRepository.save(user);

            /** generate token for user */
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);

            // save token to DB
            authenticationService.saveConfirmationToken(authenticationToken);

            // return success msg
            // success in creating
            return new ResponseDto(ResponseStatus.SUCCESS.toString(), USER_CREATED);

        }catch (Exception e){
            // handle signup error
            throw new CustomException(e.getMessage());
        }

    }

    // local method

    // TODO !!! double check below logic
    private String hashPassword(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return myHash;
    }

}
