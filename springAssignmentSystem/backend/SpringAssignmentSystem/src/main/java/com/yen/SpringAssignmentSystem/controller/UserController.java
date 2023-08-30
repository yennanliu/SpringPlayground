package com.yen.SpringAssignmentSystem.controller;

import com.yen.SpringAssignmentSystem.domain.User;
import com.yen.SpringAssignmentSystem.dto.UserKeyDto;
import com.yen.SpringAssignmentSystem.service.CommentService;
import com.yen.SpringAssignmentSystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private JwtUtil jwtUtil;


    @GetMapping("{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {

        Optional<User> userByUsername = userService.findUserByUsername(email);
        return ResponseEntity.ok(userByUsername);
    }

    @PutMapping("{email}")
    //@Secured({"ROLE_INSTRUCTOR"})
    public ResponseEntity<?> saveUser(@RequestBody UserKeyDto user) {

        log.info("Got user: " + user);
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    // TODO : fix this once fix JWT, spring security
//    @PostMapping("/register")
//    private ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
//        userService.createUser(userDto);
//
//        try {
//            Authentication authenticate = authenticationManager
//                    .authenticate(
//                            new UsernamePasswordAuthenticationToken(
//                                    userDto.getUsername(), userDto.getPassword()
//                            )
//                    );
//
//            User user = (User) authenticate.getPrincipal();
//            user.setPassword(null);
//            return ResponseEntity.ok()
//                    .header(
//                            HttpHeaders.AUTHORIZATION,
//                            jwtUtil.generateToken(user)
//                    )
//                    .body(user);
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @GetMapping("/non-configured")
    public ResponseEntity<?> nonConfiguredStudents() {

        // TODO : fix below
        //List<User> nonConfiguredStudents = userService.findNonConfiguredStudents();
        List<User> nonConfiguredStudents = null;

        // TODO : fix below
//        List<UserKeyDto> result = nonConfiguredStudents.stream()
//                .map(u -> new UserKeyDto(u.getUsername(), u.getName(), u.getCohortStartDate(), u.getBootcampDurationInWeeks()))
//                .collect(Collectors.toList());

        List<UserKeyDto> result = nonConfiguredStudents.stream()
                .map(u -> new UserKeyDto(u.getUsername(), u.getName(), u.getCohortStartDate(), null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/bootcamp-students")
    public ResponseEntity<?> getBootcampStudents() {

        List<UserKeyDto> bootcampStudents = userService.findBootcampStudents();
        return ResponseEntity.ok(bootcampStudents);
    }

}
