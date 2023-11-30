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

}
