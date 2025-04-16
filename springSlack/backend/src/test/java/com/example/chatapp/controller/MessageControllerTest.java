package com.example.chatapp.controller;

import com.example.chatapp.dto.MessageDto;
import com.example.chatapp.model.Message;
import com.example.chatapp.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private MessageDto channelMessageDto;
    private MessageDto directMessageDto;

    @BeforeEach
    void setUp() {
        channelMessageDto = new MessageDto();
        channelMessageDto.setId(1L);
        channelMessageDto.setContent("Hello channel");
        channelMessageDto.setTimestamp(LocalDateTime.now());
        channelMessageDto.setType(Message.MessageType.CHANNEL);
        channelMessageDto.setSenderId(1L);
        channelMessageDto.setSenderUsername("sender");
        channelMessageDto.setChannelId(1L);

        directMessageDto = new MessageDto();
        directMessageDto.setId(2L);
        directMessageDto.setContent("Hello user");
        directMessageDto.setTimestamp(LocalDateTime.now());
        directMessageDto.setType(Message.MessageType.DIRECT);
        directMessageDto.setSenderId(1L);
        directMessageDto.setSenderUsername("sender");
        directMessageDto.setRecipientId(2L);
    }

    @Test
    void sendChannelMessage_Success() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("senderId", 1L);
        request.put("content", "Hello channel");

        when(messageService.sendChannelMessage(anyLong(), anyLong(), anyString())).thenReturn(channelMessageDto);

        // Act & Assert
        mockMvc.perform(post("/api/messages/channel/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Hello channel")))
                .andExpect(jsonPath("$.type", is("CHANNEL")))
                .andExpect(jsonPath("$.senderId", is(1)))
                .andExpect(jsonPath("$.channelId", is(1)));
    }

    @Test
    void sendDirectMessage_Success() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("senderId", 1L);
        request.put("content", "Hello user");

        when(messageService.sendDirectMessage(anyLong(), anyLong(), anyString())).thenReturn(directMessageDto);

        // Act & Assert
        mockMvc.perform(post("/api/messages/direct/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.content", is("Hello user")))
                .andExpect(jsonPath("$.type", is("DIRECT")))
                .andExpect(jsonPath("$.senderId", is(1)))
                .andExpect(jsonPath("$.recipientId", is(2)));
    }

    @Test
    void getChannelMessages_Success() throws Exception {
        // Arrange
        List<MessageDto> messages = Arrays.asList(channelMessageDto);
        when(messageService.getChannelMessages(1L)).thenReturn(messages);

        // Act & Assert
        mockMvc.perform(get("/api/messages/channel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].content", is("Hello channel")))
                .andExpect(jsonPath("$[0].type", is("CHANNEL")));
    }

    @Test
    void getDirectMessages_Success() throws Exception {
        // Arrange
        List<MessageDto> messages = Arrays.asList(directMessageDto);
        when(messageService.getDirectMessages(1L, 2L)).thenReturn(messages);

        // Act & Assert
        mockMvc.perform(get("/api/messages/direct/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].content", is("Hello user")))
                .andExpect(jsonPath("$[0].type", is("DIRECT")));
    }
} 