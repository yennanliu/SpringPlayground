package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.exception.ConflictException;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String username, String email, String displayName) {
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setDisplayName(displayName);

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
