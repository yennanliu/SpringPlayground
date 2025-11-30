package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.UpdateProfileRequest;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.service.FileStorageService;
import com.yen.ChatAppV2.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getDisplayName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<java.util.List<User>> getAllUsers() {
        java.util.List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/online")
    public ResponseEntity<java.util.List<User>> getOnlineUsers() {
        java.util.List<User> users = userService.getOnlineUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileRequest request) {
        User user = userService.updateProfile(id, request.getDisplayName(), request.getAvatarUrl());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<String> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String avatarUrl = "/api/files/download/" + fileName;
        userService.updateProfile(id, null, avatarUrl);
        return ResponseEntity.ok(avatarUrl);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CreateUserRequest {
    private String username;
    private String email;
    private String displayName;
}
