package com.yen.ShoppingCart.service;

// https://github.com/webtutsplus/ecommerce-backend/blob/master/src/main/java/com/webtutsplus/ecommerce/service/UserService.java

import com.yen.ShoppingCart.config.MessageStrings;
import com.yen.ShoppingCart.enums.ResponseStatus;
import com.yen.ShoppingCart.enums.Role;
import com.yen.ShoppingCart.exception.AuthenticationFailException;
import com.yen.ShoppingCart.exception.CustomException;
import com.yen.ShoppingCart.model.AuthenticationToken;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.ResponseDto;
import com.yen.ShoppingCart.model.dto.user.*;
import com.yen.ShoppingCart.repository.TokenRepository;
import com.yen.ShoppingCart.repository.UserRepository;
import com.yen.ShoppingCart.util.Helper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.yen.ShoppingCart.config.MessageStrings.USER_CREATED;


@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    AuthenticationService authenticationService;

    // constructor
    public UserService(){

    }

    public UserService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public UserService(UserRepository userRepository, TokenRepository tokenRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.authenticationService = authenticationService;
    }

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

    public SignInResponseDto signIn(SignInDto signInDto) throws CustomException{

        // first find User by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if(!Helper.notNull(user)){
            throw new AuthenticationFailException("user NOT existed");
        }
        try {
            // check if password is correct
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
                // password NOT match
                throw new AuthenticationFailException("password NOT match" + MessageStrings.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("hashing password failed {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if(!Helper.notNull(token)) {
            // token not present
            throw new CustomException("token NOT present");
        }

        return new SignInResponseDto ("success", token.getToken());
    }

    // local method

    // TODO !!! double check below logic
    String hashPassword(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public ResponseDto createUser(String token, UserCreateDto userCreateDto) throws CustomException, AuthenticationFailException {

        User creatingUser = authenticationService.getUser(token);
        // check if user has permission to create a new user
        if (!canCrudUser(creatingUser.getRole())) {
            // user can't create new user
            throw new AuthenticationFailException(MessageStrings.USER_NOT_PERMITTED);
        }

        String encryptedPassword = userCreateDto.getPassword();
        try {
            // encrypted password
            encryptedPassword = hashPassword(userCreateDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("hashing password failed {}", e.getMessage());
        }

        User user = new User(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getEmail(), userCreateDto.getRole(), encryptedPassword);
        User createdUser = null;

        try {
            createdUser = userRepository.save(user);
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            // save token to DB
            authenticationService.saveConfirmationToken(authenticationToken);
            log.info("createUser OK");
            return new ResponseDto(ResponseStatus.SUCCESS.toString(), USER_CREATED);
        } catch (Exception e) {
            // handle user creation fail error
            log.info("createUser failed");
            throw new CustomException(e.getMessage());
        }

    }

    boolean canCrudUser(Role role) {

        if (role == Role.ADMIN || role == Role.MANAGER) {
            return true;
        }
        return false;
    }

    boolean canCrudUser(User userUpdating, Integer userIdBeingUpdated) {

        Role role = userUpdating.getRole();
        // TODO : optimize below
        // admin and manager can crud any user
        if (role == Role.ADMIN || role == Role.MANAGER) {
            return true;
        }
        // user can update his own record, but not his role
        if (role == Role.USER && userUpdating.getId() == userIdBeingUpdated) {
            return true;
        }
        return false;
    }

    public void updateUser(User user) {

        //User user = getUserFromDto(userUpdateDto);
        //user.setId(userUpdateDto.getId());
        userRepository.save(user);
    }

    private User getUserFromDto(UserUpdateDto userUpdateDto){

        User user = new User();
        user.setId(userUpdateDto.getId());
        user.setEmail(userUpdateDto.getEmail());
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getFirstName());
        user.setRole(userUpdateDto.getRole());
        return user;
    }

}
