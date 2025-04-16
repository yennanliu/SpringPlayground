package com.example.chatapp.integration;

import com.example.chatapp.model.User;
import com.example.chatapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_Success() throws Exception {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("username", "integrationuser");
        request.put("email", "integration@example.com");
        request.put("password", "password");

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("integrationuser")))
                .andExpect(jsonPath("$.email", is("integration@example.com")));

        // Verify the user was saved to the repository
        User savedUser = userRepository.findByUsername("integrationuser").orElseThrow();
        assert savedUser.getEmail().equals("integration@example.com");
    }
} 