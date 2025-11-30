package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceProfileTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setDisplayName("Old Name");
        user.setAvatarUrl(null);
    }

    @Test
    void testUpdateProfileDisplayName() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateProfile(1L, "New Name", null);

        assertEquals("New Name", result.getDisplayName());
        assertNull(result.getAvatarUrl());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateProfileAvatarUrl() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateProfile(1L, null, "/files/avatar.jpg");

        assertEquals("Old Name", result.getDisplayName());
        assertEquals("/files/avatar.jpg", result.getAvatarUrl());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateProfileBothFields() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateProfile(1L, "New Name", "/files/avatar.jpg");

        assertEquals("New Name", result.getDisplayName());
        assertEquals("/files/avatar.jpg", result.getAvatarUrl());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateProfileUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> userService.updateProfile(1L, "New Name", null));

        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetOnlineUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        when(redisService.getOnlineUsers()).thenReturn(Set.of("1", "2"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        List<User> onlineUsers = userService.getOnlineUsers();

        assertEquals(2, onlineUsers.size());
    }

    @Test
    void testIsUserOnline() {
        when(redisService.isUserOnline(1L)).thenReturn(true);
        when(redisService.isUserOnline(2L)).thenReturn(false);

        assertTrue(userService.isUserOnline(1L));
        assertFalse(userService.isUserOnline(2L));
    }

    @Test
    void testUpdateLastSeen() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateLastSeen(1L);

        assertNotNull(user.getLastSeenAt());
        verify(userRepository).save(user);
    }
}
