package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.UserCreateDto;
import EmployeeSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

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

}
