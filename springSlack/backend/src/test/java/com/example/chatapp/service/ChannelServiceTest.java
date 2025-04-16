package com.example.chatapp.service;

import com.example.chatapp.model.Channel;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.ChannelRepository;
import com.example.chatapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChannelService channelService;

    private User testUser;
    private Channel testChannel;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testChannel = new Channel();
        testChannel.setId(1L);
        testChannel.setName("general");
        testChannel.setDescription("General discussion");
        testChannel.setMembers(new HashSet<>(Collections.singletonList(testUser)));
    }

    @Test
    void createChannel_Success() {
        // Arrange
        when(channelRepository.existsByName(anyString())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(channelRepository.save(any(Channel.class))).thenReturn(testChannel);

        // Act
        Channel result = channelService.createChannel("general", "General discussion", 1L);

        // Assert
        assertNotNull(result);
        assertEquals("general", result.getName());
        assertEquals("General discussion", result.getDescription());
        assertTrue(result.getMembers().contains(testUser));
    }

    @Test
    void createChannel_NameExists_ThrowsException() {
        // Arrange
        when(channelRepository.existsByName(anyString())).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            channelService.createChannel("general", "General discussion", 1L);
        });

        assertEquals("Channel name already exists", exception.getMessage());
    }

    @Test
    void createChannel_UserNotFound_ThrowsException() {
        // Arrange
        when(channelRepository.existsByName(anyString())).thenReturn(false);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            channelService.createChannel("general", "General discussion", 999L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getChannel_Success() {
        // Arrange
        when(channelRepository.findById(1L)).thenReturn(Optional.of(testChannel));

        // Act
        Channel result = channelService.getChannel(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("general", result.getName());
    }

    @Test
    void getChannel_NotFound_ThrowsException() {
        // Arrange
        when(channelRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            channelService.getChannel(999L);
        });

        assertEquals("Channel not found", exception.getMessage());
    }

    @Test
    void getAllChannels_Success() {
        // Arrange
        Channel channel1 = new Channel();
        channel1.setId(1L);
        channel1.setName("general");

        Channel channel2 = new Channel();
        channel2.setId(2L);
        channel2.setName("random");

        when(channelRepository.findAll()).thenReturn(Arrays.asList(channel1, channel2));

        // Act
        List<Channel> channels = channelService.getAllChannels();

        // Assert
        assertNotNull(channels);
        assertEquals(2, channels.size());
        assertEquals("general", channels.get(0).getName());
        assertEquals("random", channels.get(1).getName());
    }

    @Test
    void joinChannel_Success() {
        // Arrange
        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("newuser");

        Channel updatedChannel = new Channel();
        updatedChannel.setId(1L);
        updatedChannel.setName("general");
        updatedChannel.setMembers(new HashSet<>(Arrays.asList(testUser, newUser)));

        when(channelRepository.findById(1L)).thenReturn(Optional.of(testChannel));
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));
        when(channelRepository.save(any(Channel.class))).thenReturn(updatedChannel);

        // Act
        Channel result = channelService.joinChannel(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getMembers().size());
        assertTrue(result.getMembers().contains(newUser));
    }

    @Test
    void leaveChannel_Success() {
        // Arrange
        // Add another user to test channel
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        testChannel.getMembers().add(user2);

        // Channel after user leaves
        Channel updatedChannel = new Channel();
        updatedChannel.setId(1L);
        updatedChannel.setName("general");
        updatedChannel.setMembers(new HashSet<>(Collections.singletonList(testUser)));

        when(channelRepository.findById(1L)).thenReturn(Optional.of(testChannel));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(channelRepository.save(any(Channel.class))).thenReturn(updatedChannel);

        // Act
        Channel result = channelService.leaveChannel(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getMembers().size());
        assertFalse(result.getMembers().contains(user2));
    }

    @Test
    void getChannelMembers_Success() {
        // Arrange
        when(channelRepository.findById(1L)).thenReturn(Optional.of(testChannel));

        // Act
        Set<User> members = channelService.getChannelMembers(1L);

        // Assert
        assertNotNull(members);
        assertEquals(1, members.size());
        assertTrue(members.contains(testUser));
    }
} 