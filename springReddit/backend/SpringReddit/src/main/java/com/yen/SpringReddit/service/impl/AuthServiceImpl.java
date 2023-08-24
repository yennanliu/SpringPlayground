//package com.yen.SpringReddit.service.impl;
//
//import com.yen.SpringReddit.dao.UserDao;
//import com.yen.SpringReddit.dao.VerificationTokenDao;
//import com.yen.SpringReddit.dto.AuthenticationResponse;
//import com.yen.SpringReddit.dto.LoginRequest;
//import com.yen.SpringReddit.dto.RegisterRequest;
//import com.yen.SpringReddit.exceptions.SpringRedditException;
//import com.yen.SpringReddit.po.NotificationEmail;
//import com.yen.SpringReddit.po.User;
//import com.yen.SpringReddit.po.VerificationToken;
////import com.yen.SpringReddit.security.JwtProvider;
//import com.yen.SpringReddit.service.AuthService;
//import com.yen.SpringReddit.service.MailService;
//import com.yen.SpringReddit.service.RefreshTokenService;
////import io.jsonwebtoken.Jwt;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.security.authentication.AuthenticationManager;
//
//import java.time.Instant;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
////@AllArgsConstructor
//@Transactional
//@Slf4j
//public class AuthServiceImpl implements AuthService {
//
//
//    /**
//     *  Autowired injection VS Constructor injection
//     *      - https://youtu.be/kpKUMmAmcj0?t=373
//     *      - Field Injection is Evil - https://odrotbohm.de/2013/11/why-field-injection-is-evil/
//     *      - Field Injection Considered Harmful - https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/
//     */
////    @Autowired
////    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserDao userDao;
//
//    @Autowired
//    private VerificationTokenDao verificationTokenDao;
//
//    @Autowired
//    private MailService mailService;
//
////    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    JwtProvider jwtProvider;
//
//    @Autowired
//    RefreshTokenService refreshTokenService;
//
//    @Override
//    public void signUp(RegisterRequest registerRequest) {
//
//        log.info("registerRequest = " + registerRequest.toString());
//
//        User user = new User();
//        // copy attr from registerRequest to user
//        //BeanUtils.copyProperties(registerRequest, user);
//        user.setEmail(registerRequest.getEmail());
//        user.setUsername(registerRequest.getUsername());
//        user.setPassword(registerRequest.getPassword());
//        //user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//        user.setCreated(Instant.now());
//        user.setEnabled(false);
//
//        log.info("user = " + user.toString());
//
//        // save to DB
//        userDao.save(user);
//
//        String token = generateVerificationToken(user);
//
//        NotificationEmail email = new NotificationEmail(
//                "plz activate your email",
//                user.getEmail(),
//                "Thanks for register!, plz click below url to activate your account :" +
//               "http://localhost:8080/api/auth/accountVerification/" + token
//        );
//        log.info("email = " + email.toString());
//        mailService.sendMail(email);
//    }
//
//    @Override
//    public String generateVerificationToken(User user) {
//
//        String token =  UUID.randomUUID().toString();
//        VerificationToken verificationToken = new VerificationToken();
//        verificationToken.setToken(token);
//        verificationToken.setUser(user);
//        // save to DB
//        verificationTokenDao.save(verificationToken);
//        return token;
//    }
//
//    @Override
//    public void verifyAccount(String token) {
//
//        Optional<VerificationToken> verificationToken = verificationTokenDao.findByToken(token);
//        verificationToken.orElseThrow( () -> new SpringRedditException("Invalid token"));
//    }
//
//    // TODO : fix this (authenticationManager init error)
//    @Override
//    public AuthenticationResponse login(LoginRequest loginRequest) {
//
//        Authentication authenticate = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
//                loginRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String token = jwtProvider.generateToken(authenticate);
//        return AuthenticationResponse.builder()
//                .authenticationToken(token)
//                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
//                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
//                .username(loginRequest.getUsername())
//                .build();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public User getCurrentUser() {
//
//        // TODO : fix below
//        return new User();
////        Jwt principal = (Jwt) SecurityContextHolder.
////                getContext().getAuthentication().getPrincipal();
////        return userDao.findByUsername(principal.getSubject())
////                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
//    }
//
//    @Override
//    public boolean isLoggedIn() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
//    }
//
//    @Transactional
//    private void fetchUserAndEnable(VerificationToken verificationToken){
//
//        String username = verificationToken.getUser().getUsername();
//        User user = userDao.findByUsername(username).orElseThrow( () -> new SpringRedditException("User not found with name = " + username));
//        user.setEnabled(true);
//        userDao.save(user);
//    }
//
//}
