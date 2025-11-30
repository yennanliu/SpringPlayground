package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.exception.ConflictException;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;

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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getOnlineUsers() {
        Set<Object> onlineUserIds = redisService.getOnlineUsers();
        return onlineUserIds.stream()
                .map(id -> Long.parseLong(id.toString()))
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public void updateLastSeen(Long userId) {
        User user = getUserById(userId);
        user.setLastSeenAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public boolean isUserOnline(Long userId) {
        return redisService.isUserOnline(userId);
    }

    public User updateProfile(Long userId, String displayName, String avatarUrl) {
        User user = getUserById(userId);

        if (displayName != null) {
            user.setDisplayName(displayName);
        }
        if (avatarUrl != null) {
            user.setAvatarUrl(avatarUrl);
        }

        return userRepository.save(user);
    }
}
