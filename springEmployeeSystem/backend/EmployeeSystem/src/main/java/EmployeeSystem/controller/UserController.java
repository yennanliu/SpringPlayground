package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.exception.CustomException;
import EmployeeSystem.model.User;
import EmployeeSystem.model.dto.*;
import EmployeeSystem.service.AuthenticationService;
import EmployeeSystem.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired UserService userService;

  @Autowired AuthenticationService authenticationService;

  @GetMapping("/")
  public ResponseEntity<Flux<User>> getUsers() {

    // TODO : optimize below
    Flux<User> usersFlux = userService.getUsers();
    //List<User> users = usersFlux.toStream().collect(Collectors.toList());
    return new ResponseEntity<>(usersFlux, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Mono<User>> getUserById(@PathVariable("userId") Integer userId) {

    Mono<User> user = userService.getUserById(userId);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  // TODO : check Stream<User> VS Flux<User> return type
  @GetMapping("/subordinates/{managerId}")
  public ResponseEntity<Flux<User>> getSubordinatesById(
      @PathVariable("managerId") Integer managerId) {

    Flux<User> userList = userService.getSubordinatesById(managerId);
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

  @PostMapping("/signup")
  public ResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {

    return userService.signUp(signupDto);
  }

  // TODO token should be updated
  @PostMapping("/signIn")
  public SignInResponseDto Signup(@RequestBody SignInDto signInDto) throws CustomException {

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
