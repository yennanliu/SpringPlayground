package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MESSAGE_CACHE_SIZE = 100;

    // Message caching
    public void cacheMessage(String channelId, Message message) {
        String key = "channel:" + channelId + ":messages";
        try {
            redisTemplate.opsForList().leftPush(key, message);
            redisTemplate.opsForList().trim(key, 0, MESSAGE_CACHE_SIZE - 1);
            log.debug("Cached message for channel {}: {}", channelId, message.getId());
        } catch (Exception e) {
            log.error("Error caching message for channel {}: {}", channelId, e.getMessage());
        }
    }

    public List<Object> getCachedMessages(String channelId, int limit) {
        String key = "channel:" + channelId + ":messages";
        try {
            return redisTemplate.opsForList().range(key, 0, limit - 1);
        } catch (Exception e) {
            log.error("Error getting cached messages for channel {}: {}", channelId, e.getMessage());
            return List.of();
        }
    }

    // Online user tracking
    public void setUserOnline(Long userId, String sessionId) {
        try {
            redisTemplate.opsForHash().put("users:online", userId.toString(), sessionId);
            log.debug("User {} is now online with session {}", userId, sessionId);
        } catch (Exception e) {
            log.error("Error setting user {} online: {}", userId, e.getMessage());
        }
    }

    public void setUserOffline(Long userId) {
        try {
            redisTemplate.opsForHash().delete("users:online", userId.toString());
            log.debug("User {} is now offline", userId);
        } catch (Exception e) {
            log.error("Error setting user {} offline: {}", userId, e.getMessage());
        }
    }

    public Set<Object> getOnlineUsers() {
        try {
            return redisTemplate.opsForHash().keys("users:online");
        } catch (Exception e) {
            log.error("Error getting online users: {}", e.getMessage());
            return Set.of();
        }
    }

    public boolean isUserOnline(Long userId) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey("users:online", userId.toString()));
        } catch (Exception e) {
            log.error("Error checking if user {} is online: {}", userId, e.getMessage());
            return false;
        }
    }

    // Channel membership
    public void addUserToChannel(Long userId, String channelId) {
        try {
            redisTemplate.opsForSet().add("channel:" + channelId + ":members", userId.toString());
            redisTemplate.opsForSet().add("user:" + userId + ":channels", channelId);
            log.debug("Added user {} to channel {}", userId, channelId);
        } catch (Exception e) {
            log.error("Error adding user {} to channel {}: {}", userId, channelId, e.getMessage());
        }
    }

    public void removeUserFromChannel(Long userId, String channelId) {
        try {
            redisTemplate.opsForSet().remove("channel:" + channelId + ":members", userId.toString());
            redisTemplate.opsForSet().remove("user:" + userId + ":channels", channelId);
            log.debug("Removed user {} from channel {}", userId, channelId);
        } catch (Exception e) {
            log.error("Error removing user {} from channel {}: {}", userId, channelId, e.getMessage());
        }
    }

    public Set<Object> getChannelMembers(String channelId) {
        try {
            return redisTemplate.opsForSet().members("channel:" + channelId + ":members");
        } catch (Exception e) {
            log.error("Error getting members for channel {}: {}", channelId, e.getMessage());
            return Set.of();
        }
    }

    public Set<Object> getUserChannels(Long userId) {
        try {
            return redisTemplate.opsForSet().members("user:" + userId + ":channels");
        } catch (Exception e) {
            log.error("Error getting channels for user {}: {}", userId, e.getMessage());
            return Set.of();
        }
    }

    // Typing indicators
    public void addTypingUser(String key, Long userId, long timeoutMs) {
        try {
            redisTemplate.opsForSet().add(key, userId.toString());
            redisTemplate.expire(key, timeoutMs, TimeUnit.MILLISECONDS);
            log.debug("User {} started typing in {}", userId, key);
        } catch (Exception e) {
            log.error("Error adding typing user {} for {}: {}", userId, key, e.getMessage());
        }
    }

    public void removeTypingUser(String key, Long userId) {
        try {
            redisTemplate.opsForSet().remove(key, userId.toString());
            log.debug("User {} stopped typing in {}", userId, key);
        } catch (Exception e) {
            log.error("Error removing typing user {} for {}: {}", userId, key, e.getMessage());
        }
    }
}
