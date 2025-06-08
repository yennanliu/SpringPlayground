package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.*;
import EmployeeSystem.service.AuthenticationService;
import EmployeeSystem.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired UserService userService;

  @Autowired AuthenticationService authenticationService;

  @GetMapping("/")
  public ResponseEntity<List<User>> getUsers() {

    List<User> users = userService.getUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getUserById(@PathVariable("userId") Integer userId) {

    User user = userService.getUserById(userId);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/subordinates/{managerId}")
  public ResponseEntity<List<User>> getSubordinatesById(
      @PathVariable("managerId") Integer managerId) {

    List<User> userList = userService.getSubordinatesById(managerId);
    return new ResponseEntity<>(userList, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addUser(@RequestBody UserCreateDto userCreateDto) {

    userService.addUser(userCreateDto);
    return new ResponseEntity<>(new ApiResponse(true, "User has been added"), HttpStatus.CREATED);
  }

  @PostMapping("/update")
  public ResponseEntity<ApiResponse> updateUser(
      @RequestBody UserCreateDto userCreateDto,
      @RequestParam(value = "photo", required = false) MultipartFile photo) {

    //        Optional<Category> optionalCategory =
    // categoryService.readCategory(productDto.getCategoryId());
    //        if (!optionalCategory.isPresent()) {
    //            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is
    // invalid"), HttpStatus.CONFLICT);
    //        }
    //        Category category = optionalCategory.get();

    log.info(">>> photo = {}", photo);
    log.info(">>> photo size = {}", photo.getSize());

    userService.updateUser(userCreateDto);
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(true, "User has been updated"), HttpStatus.OK);
  }

  @PostMapping("/update/{userId}")
  public ResponseEntity<ApiResponse> updateUserWithPhoto(
      @PathVariable("userId") Integer userId,
      @RequestParam("user") String userJson,
      @RequestParam(value = "photo", required = false) MultipartFile photo) {

    try {
      // Parse the JSON string to UserCreateDto
      ObjectMapper objectMapper = new ObjectMapper();
      UserCreateDto userCreateDto = objectMapper.readValue(userJson, UserCreateDto.class);
      userCreateDto.setId(userId);

      log.info(">>> Updating user with ID: {}", userId);
      log.info(">>> User data: {}", userCreateDto);
      log.info(">>> Photo file: {}", photo != null ? photo.getOriginalFilename() : "null");

      if (photo != null) {
        log.info(">>> Photo size: {} bytes", photo.getSize());
      }

      userService.updateUserWithPhoto(userCreateDto, photo);
      return new ResponseEntity<>(
          new ApiResponse(true, "User has been updated successfully"), HttpStatus.OK);

    } catch (Exception e) {
      log.error("Error updating user: {}", e.getMessage());
      return new ResponseEntity<>(
          new ApiResponse(false, "Failed to update user: " + e.getMessage()), 
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/photo/{userId}")
  public ResponseEntity<byte[]> getUserPhoto(@PathVariable("userId") Integer userId) {
    
    try {
      User user = userService.getUserById(userId);
      if (user != null && user.getPhoto() != null) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Default to JPEG, could be enhanced to detect type
        return new ResponseEntity<>(user.getPhoto(), headers, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error retrieving user photo: {}", e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/signUp")
  public ResponseDto signUp(@RequestBody SignupDto signupDto) throws CustomException {

    return userService.signUp(signupDto);
  }

  // TODO token should be updated
  @PostMapping("/signIn")
  public SignInResponseDto signIn(@RequestBody SignInDto signInDto) throws CustomException {

    return userService.signIn(signInDto);
  }

  @GetMapping("/userProfile")
  public User getUserProfile(@RequestParam("token") String token) {

    log.info("(getUserProfile) token = " + token);
    // get user from token
    authenticationService.authenticate(token);
    User user = authenticationService.getUser(token);
    return user;
  }
}
