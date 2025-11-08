# Refactor Design: Group Chat & User-to-User Messaging with History

## Current Issues Analysis

### 1. **Hardcoded Logic & Magic Strings**
- Channel names hardcoded in multiple places (`ChatController.java:24`, `RedisListenerHandle.java:19-26`)
- Redis key patterns scattered across code (`websocket.onlineUsers`, `websocket.msgToAll.{user}.{sender}`)
- Topic paths hardcoded (`/topic/public`, `/topic/private`)

### 2. **Queue Management Issues**
- Using Redis Sets (`opsForSet()`) for message storage - **Sets don't preserve order or allow duplicates**
- Private messages stored at `ChatController.java:99-100` but only partially integrated with Redis pub/sub
- History retrieval (`ChatController.java:124-164`) uses Sets which lack:
  - Message ordering (timestamps ignored)
  - Pagination support
  - Message expiration/cleanup

### 3. **Missing History Functionality**
- Public chat (`/topic/public`) messages stored in Set but no retrieval endpoint
- Group chat not implemented
- Private chat history exists but uses unordered Sets

---

## Proposed Architecture

### **1. Data Structure Changes**

#### **Replace Redis Sets with Sorted Sets (ZSets) or Lists**

**Option A: Sorted Sets (Recommended)**
```java
// Store with timestamp as score
redisTemplate.opsForZSet().add(
    key,
    JsonUtil.parseObjToJson(message),
    System.currentTimeMillis()
);

// Retrieve with ordering and pagination
Set<String> messages = redisTemplate.opsForZSet().range(key, start, end);

// Get recent N messages
Set<String> recent = redisTemplate.opsForZSet().reverseRange(key, 0, N-1);
```

**Option B: Redis Lists**
```java
// Store (append to right)
redisTemplate.opsForList().rightPush(key, JsonUtil.parseObjToJson(message));

// Retrieve with pagination
List<String> messages = redisTemplate.opsForList().range(key, start, end);
```

**Recommendation: Use Sorted Sets (ZSets)** - provides:
- Chronological ordering via timestamp scores
- Efficient range queries
- Easy cleanup of old messages by score
- Deduplication by score+content if needed

---

### **2. Unified Message Routing System**

#### **New Enum: MessageDestinationType**
```java
public enum MessageDestinationType {
    PUBLIC,           // Broadcast to all users
    PRIVATE,          // User-to-user
    GROUP,            // Group chat
    SYSTEM            // System notifications
}
```

#### **Enhanced ChatMessage Model**
```java
@Data
@Builder
public class ChatMessage {
    private String messageId;           // UUID for deduplication
    private MessageType type;            // CHAT, JOIN, LEAVE, etc.
    private MessageDestinationType destination;
    private String content;
    private String sender;
    private String recipient;            // For PRIVATE
    private String groupId;              // For GROUP
    private Long timestamp;              // For ordering
    private Map<String, Object> metadata; // Extensible
}
```

#### **New Group Model**
```java
@Data
public class ChatGroup {
    private String groupId;
    private String groupName;
    private String createdBy;
    private Long createdAt;
    private Set<String> members;
    private GroupType type;  // PUBLIC, PRIVATE, DIRECT
}
```

---

### **3. Centralized Redis Key Management**

#### **New Class: RedisKeyManager**
```java
@Component
public class RedisKeyManager {

    // Namespaces
    private static final String NAMESPACE = "websocket";

    // Key templates
    private static final String ONLINE_USERS = NAMESPACE + ":onlineUsers";
    private static final String USER_SESSIONS = NAMESPACE + ":sessions:{username}";
    private static final String PUBLIC_HISTORY = NAMESPACE + ":history:public";
    private static final String PRIVATE_HISTORY = NAMESPACE + ":history:private:{user1}:{user2}";
    private static final String GROUP_HISTORY = NAMESPACE + ":history:group:{groupId}";
    private static final String GROUP_META = NAMESPACE + ":group:{groupId}:meta";
    private static final String GROUP_MEMBERS = NAMESPACE + ":group:{groupId}:members";
    private static final String USER_GROUPS = NAMESPACE + ":user:{username}:groups";

    // Channels
    private static final String CHANNEL_PUBLIC = NAMESPACE + ":channel:public";
    private static final String CHANNEL_PRIVATE = NAMESPACE + ":channel:private";
    private static final String CHANNEL_GROUP = NAMESPACE + ":channel:group:{groupId}";
    private static final String CHANNEL_USER_STATUS = NAMESPACE + ":channel:userStatus";

    public String getPrivateHistoryKey(String user1, String user2) {
        // Always use consistent ordering to avoid duplicate keys
        String[] users = new String[]{user1, user2};
        Arrays.sort(users);
        return PRIVATE_HISTORY
            .replace("{user1}", users[0])
            .replace("{user2}", users[1]);
    }

    public String getGroupHistoryKey(String groupId) {
        return GROUP_HISTORY.replace("{groupId}", groupId);
    }

    public String getGroupChannel(String groupId) {
        return CHANNEL_GROUP.replace("{groupId}", groupId);
    }

    // ... more methods
}
```

---

### **4. Message Storage Service**

#### **New Class: MessageStorageService**
```java
@Service
@Slf4j
public class MessageStorageService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisKeyManager keyManager;

    @Value("${chat.history.max-messages:1000}")
    private int maxHistoryMessages;

    @Value("${chat.history.ttl-days:30}")
    private long historyTtlDays;

    /**
     * Store message with automatic TTL and size limit
     */
    public void storeMessage(ChatMessage message) {
        String key = getStorageKey(message);
        double score = message.getTimestamp();
        String value = JsonUtil.parseObjToJson(message);

        // Store in sorted set
        redisTemplate.opsForZSet().add(key, value, score);

        // Trim to max size (keep most recent)
        long size = redisTemplate.opsForZSet().size(key);
        if (size > maxHistoryMessages) {
            redisTemplate.opsForZSet().removeRange(key, 0, size - maxHistoryMessages - 1);
        }

        // Set TTL if not exists
        if (redisTemplate.getExpire(key) == -1) {
            redisTemplate.expire(key, historyTtlDays, TimeUnit.DAYS);
        }

        log.debug("Stored message in key: {}, score: {}", key, score);
    }

    /**
     * Retrieve message history with pagination
     */
    public List<ChatMessage> getHistory(
            MessageDestinationType type,
            String identifier,
            int page,
            int size) {

        String key = getHistoryKey(type, identifier);
        long start = page * size;
        long end = start + size - 1;

        // Get in reverse order (newest first)
        Set<String> messages = redisTemplate.opsForZSet()
            .reverseRange(key, start, end);

        if (messages == null || messages.isEmpty()) {
            return Collections.emptyList();
        }

        return messages.stream()
            .map(msg -> JsonUtil.parseJsonToObj(msg, ChatMessage.class))
            .collect(Collectors.toList());
    }

    /**
     * Get recent N messages
     */
    public List<ChatMessage> getRecentMessages(
            MessageDestinationType type,
            String identifier,
            int count) {
        return getHistory(type, identifier, 0, count);
    }

    private String getStorageKey(ChatMessage message) {
        switch (message.getDestination()) {
            case PUBLIC:
                return keyManager.getPublicHistoryKey();
            case PRIVATE:
                return keyManager.getPrivateHistoryKey(
                    message.getSender(),
                    message.getRecipient()
                );
            case GROUP:
                return keyManager.getGroupHistoryKey(message.getGroupId());
            default:
                throw new IllegalArgumentException("Unknown destination type");
        }
    }

    private String getHistoryKey(MessageDestinationType type, String identifier) {
        switch (type) {
            case PUBLIC:
                return keyManager.getPublicHistoryKey();
            case PRIVATE:
                String[] users = identifier.split(":");
                return keyManager.getPrivateHistoryKey(users[0], users[1]);
            case GROUP:
                return keyManager.getGroupHistoryKey(identifier);
            default:
                throw new IllegalArgumentException("Unknown destination type");
        }
    }
}
```

---

### **5. Group Management Service**

#### **New Class: GroupService**
```java
@Service
@Slf4j
public class GroupService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisKeyManager keyManager;

    /**
     * Create new group
     */
    public ChatGroup createGroup(String groupName, String createdBy, Set<String> members) {
        String groupId = UUID.randomUUID().toString();
        ChatGroup group = ChatGroup.builder()
            .groupId(groupId)
            .groupName(groupName)
            .createdBy(createdBy)
            .createdAt(System.currentTimeMillis())
            .members(members)
            .type(GroupType.PRIVATE)
            .build();

        // Store group metadata
        String metaKey = keyManager.getGroupMetaKey(groupId);
        redisTemplate.opsForValue().set(metaKey, JsonUtil.parseObjToJson(group));

        // Store members in Set
        String membersKey = keyManager.getGroupMembersKey(groupId);
        members.forEach(member ->
            redisTemplate.opsForSet().add(membersKey, member)
        );

        // Update each user's group list
        members.forEach(member -> {
            String userGroupsKey = keyManager.getUserGroupsKey(member);
            redisTemplate.opsForSet().add(userGroupsKey, groupId);
        });

        log.info("Created group: {} with {} members", groupId, members.size());
        return group;
    }

    /**
     * Add member to group
     */
    public void addMember(String groupId, String username) {
        String membersKey = keyManager.getGroupMembersKey(groupId);
        redisTemplate.opsForSet().add(membersKey, username);

        String userGroupsKey = keyManager.getUserGroupsKey(username);
        redisTemplate.opsForSet().add(userGroupsKey, groupId);

        log.info("Added user {} to group {}", username, groupId);
    }

    /**
     * Remove member from group
     */
    public void removeMember(String groupId, String username) {
        String membersKey = keyManager.getGroupMembersKey(groupId);
        redisTemplate.opsForSet().remove(membersKey, username);

        String userGroupsKey = keyManager.getUserGroupsKey(username);
        redisTemplate.opsForSet().remove(userGroupsKey, groupId);

        log.info("Removed user {} from group {}", username, groupId);
    }

    /**
     * Get group members
     */
    public Set<String> getGroupMembers(String groupId) {
        String membersKey = keyManager.getGroupMembersKey(groupId);
        return redisTemplate.opsForSet().members(membersKey);
    }

    /**
     * Get user's groups
     */
    public Set<String> getUserGroups(String username) {
        String userGroupsKey = keyManager.getUserGroupsKey(username);
        return redisTemplate.opsForSet().members(userGroupsKey);
    }

    /**
     * Check if user is member of group
     */
    public boolean isMember(String groupId, String username) {
        String membersKey = keyManager.getGroupMembersKey(groupId);
        return Boolean.TRUE.equals(
            redisTemplate.opsForSet().isMember(membersKey, username)
        );
    }
}
```

---

### **6. Refactored Message Routing**

#### **New Class: MessageRoutingService**
```java
@Service
@Slf4j
public class MessageRoutingService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MessageStorageService storageService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RedisKeyManager keyManager;

    /**
     * Route message to appropriate channel
     */
    public void routeMessage(ChatMessage message) {
        // Set timestamp if not present
        if (message.getTimestamp() == null) {
            message.setTimestamp(System.currentTimeMillis());
        }

        // Set message ID for deduplication
        if (message.getMessageId() == null) {
            message.setMessageId(UUID.randomUUID().toString());
        }

        // Store message
        storageService.storeMessage(message);

        // Publish to appropriate channel
        String channel = getChannel(message);
        String payload = JsonUtil.parseObjToJson(message);
        redisTemplate.convertAndSend(channel, payload);

        log.info("Routed message {} to channel {}", message.getMessageId(), channel);
    }

    /**
     * Validate message before routing
     */
    public void validateAndRoute(ChatMessage message) throws ValidationException {
        switch (message.getDestination()) {
            case PUBLIC:
                validatePublicMessage(message);
                break;
            case PRIVATE:
                validatePrivateMessage(message);
                break;
            case GROUP:
                validateGroupMessage(message);
                break;
            default:
                throw new ValidationException("Invalid destination type");
        }

        routeMessage(message);
    }

    private void validatePublicMessage(ChatMessage message) throws ValidationException {
        if (message.getSender() == null || message.getSender().isEmpty()) {
            throw new ValidationException("Sender is required");
        }
    }

    private void validatePrivateMessage(ChatMessage message) throws ValidationException {
        if (message.getRecipient() == null || message.getRecipient().isEmpty()) {
            throw new ValidationException("Recipient is required for private messages");
        }
        validatePublicMessage(message);
    }

    private void validateGroupMessage(ChatMessage message) throws ValidationException {
        if (message.getGroupId() == null || message.getGroupId().isEmpty()) {
            throw new ValidationException("Group ID is required for group messages");
        }

        if (!groupService.isMember(message.getGroupId(), message.getSender())) {
            throw new ValidationException("Sender is not a member of the group");
        }

        validatePublicMessage(message);
    }

    private String getChannel(ChatMessage message) {
        switch (message.getDestination()) {
            case PUBLIC:
                return keyManager.getPublicChannel();
            case PRIVATE:
                return keyManager.getPrivateChannel();
            case GROUP:
                return keyManager.getGroupChannel(message.getGroupId());
            default:
                throw new IllegalArgumentException("Unknown destination type");
        }
    }
}
```

---

### **7. Refactored ChatController**

```java
@Slf4j
@Controller
public class ChatController {

    @Autowired
    private MessageRoutingService routingService;

    @Autowired
    private MessageStorageService storageService;

    @Autowired
    private GroupService groupService;

    /**
     * Public chat message
     */
    @MessageMapping("/chat.sendMessage")
    public void sendPublicMessage(@Payload ChatMessage chatMessage) {
        try {
            chatMessage.setDestination(MessageDestinationType.PUBLIC);
            routingService.validateAndRoute(chatMessage);
        } catch (Exception e) {
            log.error("Error sending public message: {}", e.getMessage(), e);
        }
    }

    /**
     * Private message (user-to-user)
     */
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
        try {
            chatMessage.setDestination(MessageDestinationType.PRIVATE);
            chatMessage.setType(ChatMessage.MessageType.PRIVATE);
            routingService.validateAndRoute(chatMessage);
        } catch (Exception e) {
            log.error("Error sending private message: {}", e.getMessage(), e);
        }
    }

    /**
     * Group message
     */
    @MessageMapping("/chat.group")
    public void sendGroupMessage(@Payload ChatMessage chatMessage) {
        try {
            chatMessage.setDestination(MessageDestinationType.GROUP);
            routingService.validateAndRoute(chatMessage);
        } catch (Exception e) {
            log.error("Error sending group message: {}", e.getMessage(), e);
        }
    }

    /**
     * Get public chat history
     */
    @MessageMapping("/chat.getPublicHistory")
    public void getPublicHistory(@Payload Map<String, Object> request) {
        try {
            int page = (int) request.getOrDefault("page", 0);
            int size = (int) request.getOrDefault("size", 50);
            String username = (String) request.get("username");

            List<ChatMessage> history = storageService.getHistory(
                MessageDestinationType.PUBLIC,
                null,
                page,
                size
            );

            // Send to requesting user
            simpMessagingTemplate.convertAndSendToUser(
                username,
                "/topic/public.history",
                history
            );
        } catch (Exception e) {
            log.error("Error getting public history: {}", e.getMessage(), e);
        }
    }

    /**
     * Get private chat history
     */
    @MessageMapping("/chat.getPrivateHistory")
    public void getPrivateHistory(@Payload ChatMessage request) {
        try {
            String identifier = request.getSender() + ":" + request.getRecipient();
            List<ChatMessage> history = storageService.getHistory(
                MessageDestinationType.PRIVATE,
                identifier,
                0,
                100
            );

            simpMessagingTemplate.convertAndSendToUser(
                request.getSender(),
                "/topic/private.history",
                history
            );
        } catch (Exception e) {
            log.error("Error getting private history: {}", e.getMessage(), e);
        }
    }

    /**
     * Get group chat history
     */
    @MessageMapping("/chat.getGroupHistory")
    public void getGroupHistory(@Payload Map<String, Object> request) {
        try {
            String groupId = (String) request.get("groupId");
            String username = (String) request.get("username");
            int page = (int) request.getOrDefault("page", 0);
            int size = (int) request.getOrDefault("size", 50);

            // Verify membership
            if (!groupService.isMember(groupId, username)) {
                log.warn("User {} attempted to access group {} history without membership",
                    username, groupId);
                return;
            }

            List<ChatMessage> history = storageService.getHistory(
                MessageDestinationType.GROUP,
                groupId,
                page,
                size
            );

            simpMessagingTemplate.convertAndSendToUser(
                username,
                "/topic/group.history",
                history
            );
        } catch (Exception e) {
            log.error("Error getting group history: {}", e.getMessage(), e);
        }
    }

    /**
     * Create group
     */
    @MessageMapping("/chat.createGroup")
    public void createGroup(@Payload Map<String, Object> request) {
        try {
            String groupName = (String) request.get("groupName");
            String createdBy = (String) request.get("createdBy");
            Set<String> members = new HashSet<>((List<String>) request.get("members"));

            ChatGroup group = groupService.createGroup(groupName, createdBy, members);

            // Notify all members
            members.forEach(member ->
                simpMessagingTemplate.convertAndSendToUser(
                    member,
                    "/topic/group.created",
                    group
                )
            );
        } catch (Exception e) {
            log.error("Error creating group: {}", e.getMessage(), e);
        }
    }
}
```

---

### **8. Refactored RedisListenerHandle**

```java
@Slf4j
@Component
public class RedisListenerHandle extends MessageListenerAdapter {

    @Autowired
    private ChatService chatService;

    @Autowired
    private RedisKeyManager keyManager;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String rawMsg = redisTemplate.getStringSerializer().deserialize(message.getBody());
            String channel = redisTemplate.getStringSerializer().deserialize(message.getChannel());

            log.debug("Received message from channel: {}", channel);

            ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
            if (chatMessage == null) {
                log.warn("Failed to parse message from channel: {}", channel);
                return;
            }

            // Route based on destination type
            routeReceivedMessage(chatMessage, channel);

        } catch (Exception e) {
            log.error("Error processing Redis message: {}", e.getMessage(), e);
        }
    }

    private void routeReceivedMessage(ChatMessage chatMessage, String channel) {
        switch (chatMessage.getDestination()) {
            case PUBLIC:
                if (chatMessage.getType() == ChatMessage.MessageType.JOIN
                    || chatMessage.getType() == ChatMessage.MessageType.LEAVE) {
                    chatService.alertUserStatus(chatMessage);
                } else {
                    chatService.sendPublicMessage(chatMessage);
                }
                break;

            case PRIVATE:
                chatService.sendPrivateMessage(chatMessage);
                break;

            case GROUP:
                chatService.sendGroupMessage(chatMessage);
                break;

            default:
                log.warn("Unknown destination type: {}", chatMessage.getDestination());
        }
    }
}
```

---

### **9. Enhanced ChatService**

```java
@Slf4j
@Service
public class ChatService {

    @Autowired
    private SimpMessageSendingOperations messagingOperations;

    @Autowired
    private GroupService groupService;

    public void sendPublicMessage(ChatMessage chatMessage) {
        log.debug("Broadcasting public message: {}", chatMessage.getMessageId());
        messagingOperations.convertAndSend("/topic/public", chatMessage);
    }

    public void alertUserStatus(ChatMessage chatMessage) {
        log.debug("Broadcasting user status: {}", chatMessage.getSender());
        messagingOperations.convertAndSend("/topic/public", chatMessage);
    }

    public void sendPrivateMessage(ChatMessage chatMessage) {
        log.debug("Sending private message from {} to {}",
            chatMessage.getSender(), chatMessage.getRecipient());

        // Send to recipient
        messagingOperations.convertAndSendToUser(
            chatMessage.getRecipient(),
            "/topic/private",
            chatMessage
        );

        // Send copy to sender for confirmation
        messagingOperations.convertAndSendToUser(
            chatMessage.getSender(),
            "/topic/private",
            chatMessage
        );
    }

    public void sendGroupMessage(ChatMessage chatMessage) {
        log.debug("Sending group message to group: {}", chatMessage.getGroupId());

        // Get all group members
        Set<String> members = groupService.getGroupMembers(chatMessage.getGroupId());

        // Send to each member
        members.forEach(member ->
            messagingOperations.convertAndSendToUser(
                member,
                "/topic/group." + chatMessage.getGroupId(),
                chatMessage
            )
        );
    }
}
```

---

## Migration Strategy

### **Phase 1: Add New Services (Backward Compatible)**
1. Add `RedisKeyManager`, `MessageStorageService`, `GroupService`
2. Add new models (`MessageDestinationType`, enhanced `ChatMessage`)
3. Keep existing code functional

### **Phase 2: Dual Write**
1. Write to both old Sets and new ZSets
2. Test new retrieval methods
3. Verify message ordering and pagination

### **Phase 3: Switch Reads**
1. Update history endpoints to use new storage
2. Monitor for issues
3. Keep dual writes active

### **Phase 4: Clean Up**
1. Remove old Set-based storage
2. Remove deprecated code
3. Clean up old Redis keys

---

## Configuration Updates

### **application.properties**
```properties
# Message history settings
chat.history.max-messages=1000
chat.history.ttl-days=30
chat.history.page-size=50

# Redis channels (unified namespace)
redis.namespace=websocket
redis.channel.public=${redis.namespace}:channel:public
redis.channel.private=${redis.namespace}:channel:private
redis.channel.group=${redis.namespace}:channel:group
redis.channel.userStatus=${redis.namespace}:channel:userStatus
```

---

## Testing Checklist

- [ ] Public chat message storage and retrieval
- [ ] Private (user-to-user) message storage and retrieval
- [ ] Group creation and member management
- [ ] Group message storage and retrieval
- [ ] Message ordering by timestamp
- [ ] Pagination for all message types
- [ ] History size limits and cleanup
- [ ] TTL expiration
- [ ] Cross-instance message delivery (clustering)
- [ ] Member-only access to group messages
- [ ] Concurrent message handling
- [ ] Error handling and validation

---

## Benefits of This Design

1. **Unified Architecture**: Same pattern for public/private/group
2. **Proper Ordering**: ZSets maintain chronological order
3. **Scalability**: Pagination and size limits prevent memory issues
4. **Maintainability**: Centralized key management, no magic strings
5. **Extensibility**: Easy to add new message types or destinations
6. **Data Integrity**: Validation before routing
7. **Backward Compatible Migration**: Can deploy incrementally
