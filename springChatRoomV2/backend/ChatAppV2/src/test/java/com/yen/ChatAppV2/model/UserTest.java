package com.yen.ChatAppV2.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testUserCreation() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setDisplayName("Test User");

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getDisplayName()).isEqualTo("Test User");
    }

    @Test
    void testUserPrePersist() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        // When
        user.onCreate();

        // Then
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getCreatedAt()).isBefore(LocalDateTime.now().plusSeconds(1));
    }

    @Test
    void testUserAllArgsConstructor() {
        // When
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "testuser", "test@example.com", "password", "Test User", null, now, null);

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getDisplayName()).isEqualTo("Test User");
        assertThat(user.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testUserNoArgsConstructor() {
        // When
        User user = new User();

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isNull();
    }
}
