package com.yen.SpringAssignmentSystem.controller;

// https://youtu.be/_L46CaEI490?si=Xk88tIpJnJxTiouz&t=410
// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/back-end/src/main/java/com/coderscampus/AssignmentSubmissionApp/web/AuthController.java

import com.yen.SpringAssignmentSystem.domain.User;
import com.yen.SpringAssignmentSystem.dto.AuthCredentialsRequest;
//import com.yen.SpringAssignmentSystem.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class AuthController {

    private Logger log = LoggerFactory.getLogger(OncePerRequestFilter.class);
//
//    @Autowired
//    private AuthenticationManager authenticationManager;

//    @Autowired
//    private JwtUtil jwtUtil;

    @Value("${cookies.domain}")
    private String domain;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthCredentialsRequest request){

        // TODO : fix why request not go through here
        System.out.println(">>> login");
        log.info(">>> login ...., request = " + request.toString());
        System.out.println(">>> login ...., request = " + request.toString());

//        try{
//            Authentication authenticate = authenticationManager
//                    .authenticate(
//                            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//                    );
//            User user = (User) authenticate.getPrincipal();
//            user.setPassword(null);
//            String token = jwtUtil.generateToken((UserDetails) user);
//            ResponseCookie cookie = ResponseCookie.from("jwt", token)
//                    .domain(domain)
//                    .path("/")
//                    .maxAge(Duration.ofDays(365L).getSeconds()) // check with .maxAge(Duration.buildByDays(365).getMilliseconds())
//                    .build();
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                    .body(token);
//        }catch (BadCredentialsException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, "cookie_string")
                    .body("token");
    }

    // https://youtu.be/EobHBIUuV5w?si=i4ZI9qGVWrDKa0Dd&t=541
    // localhost:8080/api/auth/validate?token=xxxxxx
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@CookieValue(name = "jwt") String token, @AuthenticationPrincipal User user){

        return null;
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout (){

        System.out.println(">>> logout");
        return null;
    }

}
