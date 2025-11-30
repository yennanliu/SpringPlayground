package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.UpdateProfileRequest;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.service.FileStorageService;
import com.yen.ChatAppV2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "User management and profile operations")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @Operation(summary = "Create user", description = "Create a new user (deprecated - use /api/auth/register instead)")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getDisplayName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user information by user ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    @GetMapping
    public ResponseEntity<java.util.List<User>> getAllUsers() {
        java.util.List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get online users", description = "Retrieve a list of currently online users")
    @GetMapping("/online")
    public ResponseEntity<java.util.List<User>> getOnlineUsers() {
        java.util.List<User> users = userService.getOnlineUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Update user profile", description = "Update user's display name and/or avatar URL")
    @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateProfile(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @RequestBody UpdateProfileRequest request) {
        User user = userService.updateProfile(id, request.getDisplayName(), request.getAvatarUrl());
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Upload avatar", description = "Upload a user avatar image and update profile")
    @PostMapping("/{id}/avatar")
    public ResponseEntity<String> uploadAvatar(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Parameter(description = "Avatar image file", required = true) @RequestParam("file") MultipartFile file) {
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
