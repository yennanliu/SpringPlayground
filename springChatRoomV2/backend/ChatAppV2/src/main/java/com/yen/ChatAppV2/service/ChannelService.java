package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.dto.ChannelDTO;
import com.yen.ChatAppV2.exception.BadRequestException;
import com.yen.ChatAppV2.exception.ConflictException;
import com.yen.ChatAppV2.exception.NotFoundException;
import com.yen.ChatAppV2.model.Channel;
import com.yen.ChatAppV2.model.ChannelMember;
import com.yen.ChatAppV2.model.ChannelType;
import com.yen.ChatAppV2.model.User;
import com.yen.ChatAppV2.repository.ChannelMemberRepository;
import com.yen.ChatAppV2.repository.ChannelRepository;
import com.yen.ChatAppV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;

    public Channel createGroupChannel(String name, Long creatorId, List<Long> memberIds) {
        // Validate creator exists (if provided), otherwise skip creator validation
        if (creatorId != null && !userRepository.existsById(creatorId)) {
            log.warn("Creator with ID {} not found, creating channel without creator reference", creatorId);
            creatorId = null; // Don't set creator if user doesn't exist
        }

        // Create channel
        Channel channel = new Channel();
        channel.setChannelType(ChannelType.GROUP);
        channel.setName(name);
        channel.setCreatedBy(creatorId);
        channel = channelRepository.save(channel);

        // Add creator and members
        Set<Long> allMembers = new HashSet<>(memberIds);
        if (creatorId != null) {
            allMembers.add(creatorId);
        }

        String channelKey = "group:" + channel.getId();
        for (Long memberId : allMembers) {
            // Only add members that exist in the database
            if (userRepository.existsById(memberId)) {
                ChannelMember cm = new ChannelMember();
                cm.setChannelId(channelKey);
                cm.setUserId(memberId);
                channelMemberRepository.save(cm);

                // Cache in Redis
                redisService.addUserToChannel(memberId, channelKey);
            } else {
                log.warn("Skipping non-existent user {} when creating channel", memberId);
            }
        }

        log.info("Created group channel {} with {} members", channel.getId(), allMembers.size());
        return channel;
    }

    public Channel getOrCreateDirectChannel(Long userId1, Long userId2) {
        // Validate users exist
        userRepository.findById(userId1)
                .orElseThrow(() -> new NotFoundException("User 1 not found"));
        userRepository.findById(userId2)
                .orElseThrow(() -> new NotFoundException("User 2 not found"));

        Long smallerId = Math.min(userId1, userId2);
        Long largerId = Math.max(userId1, userId2);

        // Check if channel exists
        Optional<Channel> existing = channelRepository.findDirectChannel(smallerId, largerId);
        if (existing.isPresent()) {
            log.info("Found existing direct channel between users {} and {}", smallerId, largerId);
            return existing.get();
        }

        // Create new DM channel
        Channel channel = new Channel();
        channel.setChannelType(ChannelType.DIRECT);
        channel.setCreatedBy(userId1);
        channel = channelRepository.save(channel);

        // Add both users
        String channelKey = "dm:" + smallerId + ":" + largerId;
        for (Long userId : Arrays.asList(smallerId, largerId)) {
            ChannelMember cm = new ChannelMember();
            cm.setChannelId(channelKey);
            cm.setUserId(userId);
            channelMemberRepository.save(cm);

            // Cache in Redis
            redisService.addUserToChannel(userId, channelKey);
        }

        log.info("Created direct channel {} between users {} and {}", channel.getId(), smallerId, largerId);
        return channel;
    }

    public List<ChannelDTO> getUserChannels(Long userId) {
        List<Channel> channels = channelRepository.findChannelsByUserId(userId);
        log.debug("Found {} channels for user {}", channels.size(), userId);
        return channels.stream()
                .map(channel -> convertToDTO(channel, userId))
                .collect(Collectors.toList());
    }

    public void addMemberToChannel(Long channelId, Long userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("Channel not found"));

        if (channel.getChannelType() == ChannelType.DIRECT) {
            throw new BadRequestException("Cannot add members to direct message channels");
        }

        String channelKey = "group:" + channelId;

        if (channelMemberRepository.existsByChannelIdAndUserId(channelKey, userId)) {
            throw new ConflictException("User is already a member");
        }

        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ChannelMember cm = new ChannelMember();
        cm.setChannelId(channelKey);
        cm.setUserId(userId);
        channelMemberRepository.save(cm);

        // Cache in Redis
        redisService.addUserToChannel(userId, channelKey);

        log.info("Added user {} to channel {}", userId, channelId);
    }

    private ChannelDTO convertToDTO(Channel channel, Long currentUserId) {
        String channelKey = channel.getChannelType() == ChannelType.GROUP
            ? "group:" + channel.getId()
            : "dm:" + channel.getId(); // Note: DM needs proper user IDs, simplified here
        List<ChannelMember> members = channelMemberRepository.findByChannelId(channelKey);

        ChannelDTO dto = new ChannelDTO();
        dto.setId(channel.getId());
        dto.setChannelType(channel.getChannelType());
        dto.setName(channel.getName());
        dto.setCreatedAt(channel.getCreatedAt());
        dto.setMemberCount(members.size());

        // For DM channels, set name as other user's display name
        if (channel.getChannelType() == ChannelType.DIRECT) {
            Long otherUserId = members.stream()
                    .map(ChannelMember::getUserId)
                    .filter(id -> !id.equals(currentUserId))
                    .findFirst()
                    .orElse(null);

            if (otherUserId != null) {
                User otherUser = userRepository.findById(otherUserId).orElse(null);
                if (otherUser != null) {
                    dto.setName(otherUser.getDisplayName() != null ?
                            otherUser.getDisplayName() : otherUser.getUsername());
                }
            }
        }

        return dto;
    }
}
