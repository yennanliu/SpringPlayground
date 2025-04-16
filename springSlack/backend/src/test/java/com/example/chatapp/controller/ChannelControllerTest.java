package com.example.chatapp.controller;

import com.example.chatapp.model.Channel;
import com.example.chatapp.model.User;
import com.example.chatapp.service.ChannelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChannelService channelService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Channel testChannel;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testChannel = new Channel();
        testChannel.setId(1L);
        testChannel.setName("general");
        testChannel.setDescription("General discussion");
        Set<User> members = new HashSet<>();
        members.add(testUser);
        testChannel.setMembers(members);
    }

    @Test
    void createChannel_Success() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("name", "general");
        request.put("description", "General discussion");
        request.put("creatorId", 1L);

        when(channelService.createChannel(anyString(), anyString(), anyLong())).thenReturn(testChannel);

        // Act & Assert
        mockMvc.perform(post("/api/channels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("general")))
                .andExpect(jsonPath("$.description", is("General discussion")));
    }

    @Test
    void getChannel_Success() throws Exception {
        // Arrange
        when(channelService.getChannel(1L)).thenReturn(testChannel);

        // Act & Assert
        mockMvc.perform(get("/api/channels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("general")));
    }

    @Test
    void getAllChannels_Success() throws Exception {
        // Arrange
        Channel channel1 = new Channel();
        channel1.setId(1L);
        channel1.setName("general");

        Channel channel2 = new Channel();
        channel2.setId(2L);
        channel2.setName("random");

        List<Channel> channels = Arrays.asList(channel1, channel2);
        when(channelService.getAllChannels()).thenReturn(channels);

        // Act & Assert
        mockMvc.perform(get("/api/channels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("general")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("random")));
    }

    @Test
    void joinChannel_Success() throws Exception {
        // Arrange
        Map<String, Long> request = new HashMap<>();
        request.put("userId", 2L);

        when(channelService.joinChannel(eq(1L), eq(2L))).thenReturn(testChannel);

        // Act & Assert
        mockMvc.perform(post("/api/channels/1/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("general")));
    }

    @Test
    void leaveChannel_Success() throws Exception {
        // Arrange
        Map<String, Long> request = new HashMap<>();
        request.put("userId", 1L);

        when(channelService.leaveChannel(eq(1L), eq(1L))).thenReturn(testChannel);

        // Act & Assert
        mockMvc.perform(post("/api/channels/1/leave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("general")));
    }

    @Test
    void getChannelMembers_Success() throws Exception {
        // Arrange
        Set<User> members = new HashSet<>();
        members.add(testUser);
        when(channelService.getChannelMembers(1L)).thenReturn(members);

        // Act & Assert
        mockMvc.perform(get("/api/channels/1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("testuser")));
    }
} 