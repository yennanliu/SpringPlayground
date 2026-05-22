package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.UserRepository;
import com.yen.FlinkRestService.exception.ValidationException;
import com.yen.FlinkRestService.model.AppUser;
import com.yen.FlinkRestService.model.dto.auth.AuthResponse;
import com.yen.FlinkRestService.model.dto.auth.AuthResponse.UserDto;
import com.yen.FlinkRestService.model.dto.auth.SigninRequest;
import com.yen.FlinkRestService.model.dto.auth.SignupRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Transactional
    public AuthResponse signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ValidationException("Email address is already registered");
        }

        AppUser user = new AppUser();
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setPassword(PASSWORD_ENCODER.encode(req.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());

        AppUser saved = userRepository.save(user);
        log.info("New user registered: email={}", saved.getEmail());
        return toAuthResponse(saved);
    }

    @Transactional
    public AuthResponse signin(SigninRequest req) {
        AppUser user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ValidationException("Invalid email or password"));

        if (!PASSWORD_ENCODER.matches(req.getPassword(), user.getPassword())) {
            log.warn("Failed sign-in attempt for email={}", req.getEmail());
            throw new ValidationException("Invalid email or password");
        }

        // Refresh token on each successful sign-in
        user.setToken(UUID.randomUUID().toString());
        AppUser saved = userRepository.save(user);
        log.info("User signed in: email={}", saved.getEmail());
        return toAuthResponse(saved);
    }

    private AuthResponse toAuthResponse(AppUser user) {
        UserDto dto = new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());
        return new AuthResponse(user.getToken(), dto);
    }
}
