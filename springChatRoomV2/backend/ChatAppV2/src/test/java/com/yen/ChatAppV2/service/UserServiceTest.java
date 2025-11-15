package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.exception.ConflictException;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser_Success() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setDisplayName("Test User");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User created = userService.createUser("testuser", "test@example.com", "Test User");

        // Then
        assertThat(created).isNotNull();
        assertThat(created.getUsername()).isEqualTo("testuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_UsernameExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> userService.createUser("testuser", "test@example.com", "Test"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void testCreateUser_EmailExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> userService.createUser("testuser", "test@example.com", "Test"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void testGetUserById_Success() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User found = userService.getUserById(1L);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
