package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.AuthResponse;
import com.yen.ChatAppV2.dto.LoginRequest;
import com.yen.ChatAppV2.dto.RegisterRequest;
import com.yen.ChatAppV2.exception.ConflictException;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName() != null ?
                request.getDisplayName() : request.getUsername());

        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities("USER")
                        .build()
        );

        log.info("User registered successfully: {}", user.getUsername());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getDisplayName());
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Fetch user details
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Generate JWT token
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities("USER")
                        .build()
        );

        log.info("User logged in successfully: {}", user.getUsername());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getDisplayName());
    }
}
