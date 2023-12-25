package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.*;
import EmployeeSystem.service.AuthenticationService;
import EmployeeSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers(){

        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Integer userId){

        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/subordinates/{managerId}")
    public ResponseEntity<List<User>> getSubordinatesById(@PathVariable("managerId") Integer managerId){

        List<User> userList = userService.getSubordinatesById(managerId);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody UserCreateDto userCreateDto){

        userService.addUser(userCreateDto);
        return new ResponseEntity<>(new ApiResponse(true, "User has been added"), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserCreateDto userCreateDto) {

        //        Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());
//        if (!optionalCategory.isPresent()) {
//            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
//        }
//        Category category = optionalCategory.get();
        userService.updateUser(userCreateDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User has been updated"), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {

        return userService.signUp(signupDto);
    }

    //TODO token should be updated
    @PostMapping("/signIn")
    public SignInResponseDto Signup(@RequestBody SignInDto signInDto) throws CustomException {

        return userService.signIn(signInDto);
    }

    @GetMapping("/userProfile")
    public User getUserProfile(@RequestParam("token") String token){

        log.info("(getUserProfile) token = " + token);
        // get user from token
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        return user;
    }

}
