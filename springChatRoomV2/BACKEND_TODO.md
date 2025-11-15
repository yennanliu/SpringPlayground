# Backend Implementation TODO

## Project Setup

### Initial Configuration
- [ ] Initialize Spring Boot project (version 3.x)
  - Spring Web
  - Spring WebSocket
  - Spring Data JPA
  - Spring Data Redis
  - PostgreSQL Driver
  - Lombok
  - Spring Security (Phase 2)
  - Validation
  - Spring Boot DevTools

- [ ] Configure `application.yml`
  - Database configuration
  - Redis configuration
  - WebSocket settings
  - CORS configuration
  - Logging configuration

- [ ] Setup project structure
  ```
  src/main/java/com/chatapp/
  ├── config/
  ├── controller/
  ├── model/
  ├── repository/
  ├── service/
  ├── dto/
  ├── exception/
  └── util/
  ```

- [ ] Database setup
  - Create PostgreSQL database `chatapp`
  - Run schema creation scripts
  - Setup connection pool configuration

- [ ] Redis setup
  - Install/configure Redis server
  - Test Redis connection
  - Configure Redis template

---

## Phase 1: MVP (Minimum Viable Product)

### Goal
Basic WebSocket messaging with single group room, Redis caching, and PostgreSQL persistence.

### 1.1 Database Schema & Entities

#### Database Schema
- [ ] Create SQL migration files (`schema.sql`)
  ```sql
  -- Users table
  CREATE TABLE users (
      id BIGSERIAL PRIMARY KEY,
      username VARCHAR(50) UNIQUE NOT NULL,
      email VARCHAR(100) UNIQUE NOT NULL,
      display_name VARCHAR(100),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

  -- Channels table
  CREATE TABLE channels (
      id BIGSERIAL PRIMARY KEY,
      channel_type VARCHAR(20) NOT NULL,
      name VARCHAR(100),
      created_by BIGINT REFERENCES users(id),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT check_channel_type CHECK (channel_type IN ('GROUP', 'DIRECT'))
  );

  -- Channel members table
  CREATE TABLE channel_members (
      channel_id BIGINT REFERENCES channels(id) ON DELETE CASCADE,
      user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
      joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      last_read_at TIMESTAMP,
      PRIMARY KEY (channel_id, user_id)
  );

  -- Messages table
  CREATE TABLE messages (
      id BIGSERIAL PRIMARY KEY,
      channel_id BIGINT REFERENCES channels(id) ON DELETE CASCADE,
      sender_id BIGINT REFERENCES users(id),
      content TEXT NOT NULL,
      message_type VARCHAR(20) DEFAULT 'TEXT',
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

  -- Indexes
  CREATE INDEX idx_channels_type ON channels(channel_type);
  CREATE INDEX idx_channel_members_user ON channel_members(user_id);
  CREATE INDEX idx_messages_channel ON messages(channel_id, created_at DESC);
  CREATE INDEX idx_messages_sender ON messages(sender_id);
  ```

#### Entity Classes
- [ ] **User.java**
  ```java
  @Entity
  @Table(name = "users")
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(unique = true, nullable = false, length = 50)
      private String username;

      @Column(unique = true, nullable = false, length = 100)
      private String email;

      @Column(name = "display_name", length = 100)
      private String displayName;

      @Column(name = "created_at")
      private LocalDateTime createdAt;

      @PrePersist
      protected void onCreate() {
          createdAt = LocalDateTime.now();
      }
  }
  ```

- [ ] **Channel.java**
  ```java
  @Entity
  @Table(name = "channels")
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class Channel {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Enumerated(EnumType.STRING)
      @Column(name = "channel_type", nullable = false)
      private ChannelType channelType;

      @Column(length = 100)
      private String name;

      @Column(name = "created_by")
      private Long createdBy;

      @Column(name = "created_at")
      private LocalDateTime createdAt;

      @PrePersist
      protected void onCreate() {
          createdAt = LocalDateTime.now();
      }
  }

  public enum ChannelType {
      GROUP, DIRECT
  }
  ```

- [ ] **ChannelMember.java**
  ```java
  @Entity
  @Table(name = "channel_members")
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @IdClass(ChannelMemberId.class)
  public class ChannelMember {
      @Id
      @Column(name = "channel_id")
      private Long channelId;

      @Id
      @Column(name = "user_id")
      private Long userId;

      @Column(name = "joined_at")
      private LocalDateTime joinedAt;

      @Column(name = "last_read_at")
      private LocalDateTime lastReadAt;

      @PrePersist
      protected void onCreate() {
          joinedAt = LocalDateTime.now();
      }
  }

  @Data
  public class ChannelMemberId implements Serializable {
      private Long channelId;
      private Long userId;
  }
  ```

- [ ] **Message.java**
  ```java
  @Entity
  @Table(name = "messages")
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class Message {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(name = "channel_id", nullable = false)
      private Long channelId;

      @Column(name = "sender_id")
      private Long senderId;

      @Column(nullable = false, columnDefinition = "TEXT")
      private String content;

      @Enumerated(EnumType.STRING)
      @Column(name = "message_type")
      private MessageType messageType = MessageType.TEXT;

      @Column(name = "created_at")
      private LocalDateTime createdAt;

      @PrePersist
      protected void onCreate() {
          createdAt = LocalDateTime.now();
      }
  }

  public enum MessageType {
      TEXT, IMAGE, FILE
  }
  ```

### 1.2 Repository Layer

- [ ] **UserRepository.java**
  ```java
  public interface UserRepository extends JpaRepository<User, Long> {
      Optional<User> findByUsername(String username);
      Optional<User> findByEmail(String email);
      boolean existsByUsername(String username);
      boolean existsByEmail(String email);
  }
  ```

- [ ] **ChannelRepository.java**
  ```java
  public interface ChannelRepository extends JpaRepository<Channel, Long> {
      List<Channel> findByChannelType(ChannelType channelType);

      @Query("SELECT c FROM Channel c JOIN ChannelMember cm ON c.id = cm.channelId " +
             "WHERE cm.userId = :userId")
      List<Channel> findChannelsByUserId(@Param("userId") Long userId);

      @Query("SELECT c FROM Channel c WHERE c.channelType = 'DIRECT' " +
             "AND EXISTS (SELECT 1 FROM ChannelMember cm1 WHERE cm1.channelId = c.id AND cm1.userId = :userId1) " +
             "AND EXISTS (SELECT 1 FROM ChannelMember cm2 WHERE cm2.channelId = c.id AND cm2.userId = :userId2)")
      Optional<Channel> findDirectChannel(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
  }
  ```

- [ ] **ChannelMemberRepository.java**
  ```java
  public interface ChannelMemberRepository extends JpaRepository<ChannelMember, ChannelMemberId> {
      List<ChannelMember> findByChannelId(Long channelId);
      List<ChannelMember> findByUserId(Long userId);
      boolean existsByChannelIdAndUserId(Long channelId, Long userId);
      void deleteByChannelIdAndUserId(Long channelId, Long userId);
  }
  ```

- [ ] **MessageRepository.java**
  ```java
  public interface MessageRepository extends JpaRepository<Message, Long> {
      Page<Message> findByChannelIdOrderByCreatedAtDesc(Long channelId, Pageable pageable);

      @Query("SELECT m FROM Message m WHERE m.channelId = :channelId " +
             "AND m.createdAt > :since ORDER BY m.createdAt ASC")
      List<Message> findRecentMessages(@Param("channelId") Long channelId,
                                       @Param("since") LocalDateTime since);

      Long countByChannelIdAndCreatedAtAfter(Long channelId, LocalDateTime timestamp);
  }
  ```

### 1.3 WebSocket Configuration

- [ ] **WebSocketConfig.java**
  ```java
  @Configuration
  @EnableWebSocketMessageBroker
  public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

      @Override
      public void configureMessageBroker(MessageBrokerRegistry config) {
          // Enable simple in-memory broker for topics and queues
          config.enableSimpleBroker("/topic", "/queue");
          // Set application destination prefix
          config.setApplicationDestinationPrefixes("/app");
          // Set user destination prefix
          config.setUserDestinationPrefix("/user");
      }

      @Override
      public void registerStompEndpoints(StompEndpointRegistry registry) {
          registry.addEndpoint("/ws")
                  .setAllowedOriginPatterns("*")
                  .withSockJS();
      }

      @Override
      public void configureClientInboundChannel(ChannelRegistration registration) {
          registration.interceptors(new ChannelInterceptor() {
              @Override
              public Message<?> preSend(Message<?> message, MessageChannel channel) {
                  // Log incoming messages, track connections
                  return message;
              }
          });
      }
  }
  ```

- [ ] **WebSocketEventListener.java**
  ```java
  @Component
  @RequiredArgsConstructor
  @Slf4j
  public class WebSocketEventListener {

      private final RedisService redisService;

      @EventListener
      public void handleWebSocketConnectListener(SessionConnectedEvent event) {
          StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
          String sessionId = headerAccessor.getSessionId();
          // Extract userId from headers and mark user as online
          log.info("New WebSocket connection: {}", sessionId);
      }

      @EventListener
      public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
          StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
          String sessionId = headerAccessor.getSessionId();
          // Mark user as offline, clean up Redis
          log.info("WebSocket connection closed: {}", sessionId);
      }
  }
  ```

### 1.4 Redis Service

- [ ] **RedisConfig.java**
  ```java
  @Configuration
  @EnableRedisRepositories
  public class RedisConfig {

      @Bean
      public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
          RedisTemplate<String, Object> template = new RedisTemplate<>();
          template.setConnectionFactory(connectionFactory);

          // Use Jackson for JSON serialization
          Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
          ObjectMapper objectMapper = new ObjectMapper();
          objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
          serializer.setObjectMapper(objectMapper);

          template.setKeySerializer(new StringRedisSerializer());
          template.setValueSerializer(serializer);
          template.setHashKeySerializer(new StringRedisSerializer());
          template.setHashValueSerializer(serializer);

          template.afterPropertiesSet();
          return template;
      }
  }
  ```

- [ ] **RedisService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  public class RedisService {

      private final RedisTemplate<String, Object> redisTemplate;
      private static final int MESSAGE_CACHE_SIZE = 100;

      // Message caching
      public void cacheMessage(String channelId, Message message) {
          String key = "channel:" + channelId + ":messages";
          redisTemplate.opsForList().leftPush(key, message);
          redisTemplate.opsForList().trim(key, 0, MESSAGE_CACHE_SIZE - 1);
      }

      public List<Object> getCachedMessages(String channelId, int limit) {
          String key = "channel:" + channelId + ":messages";
          return redisTemplate.opsForList().range(key, 0, limit - 1);
      }

      // Online user tracking
      public void setUserOnline(Long userId, String sessionId) {
          redisTemplate.opsForHash().put("users:online", userId.toString(), sessionId);
      }

      public void setUserOffline(Long userId) {
          redisTemplate.opsForHash().delete("users:online", userId.toString());
      }

      public Set<Object> getOnlineUsers() {
          return redisTemplate.opsForHash().keys("users:online");
      }

      public boolean isUserOnline(Long userId) {
          return redisTemplate.opsForHash().hasKey("users:online", userId.toString());
      }

      // Channel membership
      public void addUserToChannel(Long userId, String channelId) {
          redisTemplate.opsForSet().add("channel:" + channelId + ":members", userId.toString());
          redisTemplate.opsForSet().add("user:" + userId + ":channels", channelId);
      }

      public void removeUserFromChannel(Long userId, String channelId) {
          redisTemplate.opsForSet().remove("channel:" + channelId + ":members", userId.toString());
          redisTemplate.opsForSet().remove("user:" + userId + ":channels", channelId);
      }

      public Set<Object> getChannelMembers(String channelId) {
          return redisTemplate.opsForSet().members("channel:" + channelId + ":members");
      }
  }
  ```

### 1.5 DTOs

- [ ] **ChatMessageDTO.java**
  ```java
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ChatMessageDTO {
      private Long id;
      private Long channelId;
      private Long senderId;
      private String senderName;
      private String content;
      private MessageType messageType;
      private LocalDateTime timestamp;
  }
  ```

- [ ] **MessageRequest.java**
  ```java
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class MessageRequest {
      @NotNull
      private Long channelId;

      @NotNull
      private Long senderId;

      @NotBlank
      @Size(max = 5000)
      private String content;

      private MessageType messageType = MessageType.TEXT;
  }
  ```

### 1.6 Service Layer

- [ ] **ChatService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  @Slf4j
  public class ChatService {

      private final MessageRepository messageRepository;
      private final ChannelMemberRepository channelMemberRepository;
      private final UserRepository userRepository;
      private final RedisService redisService;
      private final SimpMessagingTemplate messagingTemplate;

      public ChatMessageDTO processAndSendMessage(MessageRequest request) {
          // 1. Validate user is member of channel
          if (!channelMemberRepository.existsByChannelIdAndUserId(
                  request.getChannelId(), request.getSenderId())) {
              throw new UnauthorizedException("User is not a member of this channel");
          }

          // 2. Get sender info
          User sender = userRepository.findById(request.getSenderId())
                  .orElseThrow(() -> new NotFoundException("User not found"));

          // 3. Create message
          Message message = new Message();
          message.setChannelId(request.getChannelId());
          message.setSenderId(request.getSenderId());
          message.setContent(request.getContent());
          message.setMessageType(request.getMessageType());

          // 4. Save to database
          message = messageRepository.save(message);

          // 5. Cache in Redis
          redisService.cacheMessage("channel:" + request.getChannelId(), message);

          // 6. Create DTO
          ChatMessageDTO dto = new ChatMessageDTO(
                  message.getId(),
                  message.getChannelId(),
                  message.getSenderId(),
                  sender.getDisplayName() != null ? sender.getDisplayName() : sender.getUsername(),
                  message.getContent(),
                  message.getMessageType(),
                  message.getCreatedAt()
          );

          // 7. Broadcast to channel
          messagingTemplate.convertAndSend(
                  "/topic/channel/" + request.getChannelId(),
                  dto
          );

          log.info("Message sent to channel {}: {}", request.getChannelId(), message.getId());
          return dto;
      }

      public Page<ChatMessageDTO> getChannelMessages(Long channelId, Long userId, Pageable pageable) {
          // Validate user is member
          if (!channelMemberRepository.existsByChannelIdAndUserId(channelId, userId)) {
              throw new UnauthorizedException("User is not a member of this channel");
          }

          // Fetch from database
          Page<Message> messages = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);

          // Convert to DTOs
          return messages.map(this::convertToDTO);
      }

      private ChatMessageDTO convertToDTO(Message message) {
          User sender = userRepository.findById(message.getSenderId()).orElse(null);
          String senderName = sender != null ?
                  (sender.getDisplayName() != null ? sender.getDisplayName() : sender.getUsername())
                  : "Unknown";

          return new ChatMessageDTO(
                  message.getId(),
                  message.getChannelId(),
                  message.getSenderId(),
                  senderName,
                  message.getContent(),
                  message.getMessageType(),
                  message.getCreatedAt()
          );
      }
  }
  ```

- [ ] **UserService.java** (Basic for Phase 1)
  ```java
  @Service
  @RequiredArgsConstructor
  public class UserService {

      private final UserRepository userRepository;

      public User createUser(String username, String email, String displayName) {
          if (userRepository.existsByUsername(username)) {
              throw new ConflictException("Username already exists");
          }
          if (userRepository.existsByEmail(email)) {
              throw new ConflictException("Email already exists");
          }

          User user = new User();
          user.setUsername(username);
          user.setEmail(email);
          user.setDisplayName(displayName);

          return userRepository.save(user);
      }

      public User getUserById(Long id) {
          return userRepository.findById(id)
                  .orElseThrow(() -> new NotFoundException("User not found"));
      }

      public User getUserByUsername(String username) {
          return userRepository.findByUsername(username)
                  .orElseThrow(() -> new NotFoundException("User not found"));
      }
  }
  ```

### 1.7 Controllers

- [ ] **ChatController.java** (WebSocket)
  ```java
  @Controller
  @RequiredArgsConstructor
  @Slf4j
  public class ChatController {

      private final ChatService chatService;

      @MessageMapping("/chat/{channelId}")
      public void sendMessage(@DestinationVariable Long channelId,
                            @Payload MessageRequest request,
                            SimpMessageHeaderAccessor headerAccessor) {
          log.info("Received message for channel {}: {}", channelId, request);

          // Ensure channelId matches
          request.setChannelId(channelId);

          // Process and broadcast
          chatService.processAndSendMessage(request);
      }
  }
  ```

- [ ] **MessageRestController.java** (REST API)
  ```java
  @RestController
  @RequestMapping("/api/messages")
  @RequiredArgsConstructor
  @CrossOrigin(origins = "*")
  public class MessageRestController {

      private final ChatService chatService;

      @GetMapping("/channel/{channelId}")
      public ResponseEntity<Page<ChatMessageDTO>> getChannelMessages(
              @PathVariable Long channelId,
              @RequestParam Long userId,
              @RequestParam(defaultValue = "0") int page,
              @RequestParam(defaultValue = "50") int size) {

          Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
          Page<ChatMessageDTO> messages = chatService.getChannelMessages(channelId, userId, pageable);
          return ResponseEntity.ok(messages);
      }
  }
  ```

- [ ] **UserController.java** (REST API - Basic)
  ```java
  @RestController
  @RequestMapping("/api/users")
  @RequiredArgsConstructor
  @CrossOrigin(origins = "*")
  public class UserController {

      private final UserService userService;

      @PostMapping
      public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
          User user = userService.createUser(
                  request.getUsername(),
                  request.getEmail(),
                  request.getDisplayName()
          );
          return ResponseEntity.status(HttpStatus.CREATED).body(user);
      }

      @GetMapping("/{id}")
      public ResponseEntity<User> getUser(@PathVariable Long id) {
          User user = userService.getUserById(id);
          return ResponseEntity.ok(user);
      }
  }
  ```

### 1.8 Exception Handling

- [ ] **Custom Exceptions**
  ```java
  public class NotFoundException extends RuntimeException {
      public NotFoundException(String message) {
          super(message);
      }
  }

  public class UnauthorizedException extends RuntimeException {
      public UnauthorizedException(String message) {
          super(message);
      }
  }

  public class ConflictException extends RuntimeException {
      public ConflictException(String message) {
          super(message);
      }
  }
  ```

- [ ] **GlobalExceptionHandler.java**
  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler {

      @ExceptionHandler(NotFoundException.class)
      public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
          ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
      }

      @ExceptionHandler(UnauthorizedException.class)
      public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
          ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
          return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
      }

      @ExceptionHandler(ConflictException.class)
      public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
          ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
          return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
      }

      @ExceptionHandler(Exception.class)
      public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
          ErrorResponse error = new ErrorResponse(
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  "An unexpected error occurred"
          );
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
      }
  }

  @Data
  @AllArgsConstructor
  class ErrorResponse {
      private int status;
      private String message;
      private LocalDateTime timestamp = LocalDateTime.now();

      public ErrorResponse(int status, String message) {
          this.status = status;
          this.message = message;
      }
  }
  ```

### Phase 1 Testing Checklist
- [ ] Database schema created successfully
- [ ] All entities properly mapped
- [ ] WebSocket connection establishes
- [ ] Messages can be sent via WebSocket
- [ ] Messages are persisted to PostgreSQL
- [ ] Messages are cached in Redis
- [ ] Messages broadcast to subscribed clients
- [ ] Message history can be retrieved via REST API
- [ ] Exception handling works correctly
- [ ] CORS configured properly for frontend

---

## Phase 2: Core Features

### Goal
User authentication, authorization, multiple channels, direct messaging, and channel management.

### 2.1 Security Configuration

- [ ] Add Spring Security dependencies
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-api</artifactId>
      <version>0.11.5</version>
  </dependency>
  <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-impl</artifactId>
      <version>0.11.5</version>
  </dependency>
  <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-jackson</artifactId>
      <version>0.11.5</version>
  </dependency>
  ```

- [ ] Update **User.java** entity
  ```java
  @Entity
  @Table(name = "users")
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(unique = true, nullable = false, length = 50)
      private String username;

      @Column(unique = true, nullable = false, length = 100)
      private String email;

      @Column(nullable = false)
      private String password; // Hashed password

      @Column(name = "display_name", length = 100)
      private String displayName;

      @Column(name = "avatar_url")
      private String avatarUrl;

      @Column(name = "created_at")
      private LocalDateTime createdAt;

      @Column(name = "last_seen_at")
      private LocalDateTime lastSeenAt;

      @PrePersist
      protected void onCreate() {
          createdAt = LocalDateTime.now();
      }
  }
  ```

- [ ] **SecurityConfig.java**
  ```java
  @Configuration
  @EnableWebSecurity
  @RequiredArgsConstructor
  public class SecurityConfig {

      private final JwtAuthenticationFilter jwtAuthFilter;
      private final AuthenticationProvider authenticationProvider;

      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http
              .csrf(csrf -> csrf.disable())
              .cors(cors -> cors.configurationSource(corsConfigurationSource()))
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/auth/**", "/ws/**").permitAll()
                  .anyRequest().authenticated()
              )
              .sessionManagement(session -> session
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )
              .authenticationProvider(authenticationProvider)
              .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

          return http.build();
      }

      @Bean
      public CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:5173"));
          configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          configuration.setAllowedHeaders(Arrays.asList("*"));
          configuration.setAllowCredentials(true);

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", configuration);
          return source;
      }

      @Bean
      public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
      }
  }
  ```

- [ ] **JwtService.java**
  ```java
  @Service
  public class JwtService {

      @Value("${jwt.secret}")
      private String secretKey;

      @Value("${jwt.expiration}")
      private Long jwtExpiration;

      public String extractUsername(String token) {
          return extractClaim(token, Claims::getSubject);
      }

      public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
          final Claims claims = extractAllClaims(token);
          return claimsResolver.apply(claims);
      }

      public String generateToken(UserDetails userDetails) {
          return generateToken(new HashMap<>(), userDetails);
      }

      public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
          return Jwts
                  .builder()
                  .setClaims(extraClaims)
                  .setSubject(userDetails.getUsername())
                  .setIssuedAt(new Date(System.currentTimeMillis()))
                  .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                  .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                  .compact();
      }

      public boolean isTokenValid(String token, UserDetails userDetails) {
          final String username = extractUsername(token);
          return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
      }

      private boolean isTokenExpired(String token) {
          return extractExpiration(token).before(new Date());
      }

      private Date extractExpiration(String token) {
          return extractClaim(token, Claims::getExpiration);
      }

      private Claims extractAllClaims(String token) {
          return Jwts
                  .parserBuilder()
                  .setSigningKey(getSignInKey())
                  .build()
                  .parseClaimsJws(token)
                  .getBody();
      }

      private Key getSignInKey() {
          byte[] keyBytes = Decoders.BASE64.decode(secretKey);
          return Keys.hmacShaKeyFor(keyBytes);
      }
  }
  ```

- [ ] **JwtAuthenticationFilter.java**
  ```java
  @Component
  @RequiredArgsConstructor
  public class JwtAuthenticationFilter extends OncePerRequestFilter {

      private final JwtService jwtService;
      private final UserDetailsService userDetailsService;

      @Override
      protected void doFilterInternal(
              HttpServletRequest request,
              HttpServletResponse response,
              FilterChain filterChain) throws ServletException, IOException {

          final String authHeader = request.getHeader("Authorization");
          final String jwt;
          final String username;

          if (authHeader == null || !authHeader.startsWith("Bearer ")) {
              filterChain.doFilter(request, response);
              return;
          }

          jwt = authHeader.substring(7);
          username = jwtService.extractUsername(jwt);

          if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
              UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

              if (jwtService.isTokenValid(jwt, userDetails)) {
                  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                          userDetails,
                          null,
                          userDetails.getAuthorities()
                  );
                  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authToken);
              }
          }

          filterChain.doFilter(request, response);
      }
  }
  ```

- [ ] **CustomUserDetailsService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  public class CustomUserDetailsService implements UserDetailsService {

      private final UserRepository userRepository;

      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          User user = userRepository.findByUsername(username)
                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          return org.springframework.security.core.userdetails.User
                  .withUsername(user.getUsername())
                  .password(user.getPassword())
                  .authorities("USER")
                  .build();
      }
  }
  ```

### 2.2 Authentication Controller & Service

- [ ] **AuthController.java**
  ```java
  @RestController
  @RequestMapping("/api/auth")
  @RequiredArgsConstructor
  @CrossOrigin(origins = "*")
  public class AuthController {

      private final AuthService authService;

      @PostMapping("/register")
      public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
          AuthResponse response = authService.register(request);
          return ResponseEntity.status(HttpStatus.CREATED).body(response);
      }

      @PostMapping("/login")
      public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
          AuthResponse response = authService.login(request);
          return ResponseEntity.ok(response);
      }
  }

  @Data
  class RegisterRequest {
      @NotBlank
      @Size(min = 3, max = 50)
      private String username;

      @NotBlank
      @Email
      private String email;

      @NotBlank
      @Size(min = 6, max = 100)
      private String password;

      private String displayName;
  }

  @Data
  class LoginRequest {
      @NotBlank
      private String username;

      @NotBlank
      private String password;
  }

  @Data
  @AllArgsConstructor
  class AuthResponse {
      private String token;
      private Long userId;
      private String username;
      private String displayName;
  }
  ```

- [ ] **AuthService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  public class AuthService {

      private final UserRepository userRepository;
      private final PasswordEncoder passwordEncoder;
      private final JwtService jwtService;
      private final AuthenticationManager authenticationManager;

      public AuthResponse register(RegisterRequest request) {
          if (userRepository.existsByUsername(request.getUsername())) {
              throw new ConflictException("Username already exists");
          }
          if (userRepository.existsByEmail(request.getEmail())) {
              throw new ConflictException("Email already exists");
          }

          User user = new User();
          user.setUsername(request.getUsername());
          user.setEmail(request.getEmail());
          user.setPassword(passwordEncoder.encode(request.getPassword()));
          user.setDisplayName(request.getDisplayName() != null ?
                  request.getDisplayName() : request.getUsername());

          user = userRepository.save(user);

          String token = jwtService.generateToken(
                  org.springframework.security.core.userdetails.User
                          .withUsername(user.getUsername())
                          .password(user.getPassword())
                          .authorities("USER")
                          .build()
          );

          return new AuthResponse(token, user.getId(), user.getUsername(), user.getDisplayName());
      }

      public AuthResponse login(LoginRequest request) {
          authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(
                          request.getUsername(),
                          request.getPassword()
                  )
          );

          User user = userRepository.findByUsername(request.getUsername())
                  .orElseThrow(() -> new NotFoundException("User not found"));

          String token = jwtService.generateToken(
                  org.springframework.security.core.userdetails.User
                          .withUsername(user.getUsername())
                          .password(user.getPassword())
                          .authorities("USER")
                          .build()
          );

          return new AuthResponse(token, user.getId(), user.getUsername(), user.getDisplayName());
      }
  }
  ```

### 2.3 Channel Management

- [ ] **ChannelService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  @Transactional
  public class ChannelService {

      private final ChannelRepository channelRepository;
      private final ChannelMemberRepository channelMemberRepository;
      private final UserRepository userRepository;
      private final RedisService redisService;

      public Channel createGroupChannel(String name, Long creatorId, List<Long> memberIds) {
          // Validate creator exists
          userRepository.findById(creatorId)
                  .orElseThrow(() -> new NotFoundException("Creator not found"));

          // Create channel
          Channel channel = new Channel();
          channel.setChannelType(ChannelType.GROUP);
          channel.setName(name);
          channel.setCreatedBy(creatorId);
          channel = channelRepository.save(channel);

          // Add creator and members
          Set<Long> allMembers = new HashSet<>(memberIds);
          allMembers.add(creatorId);

          String channelKey = "group:" + channel.getId();
          for (Long memberId : allMembers) {
              ChannelMember cm = new ChannelMember();
              cm.setChannelId(channel.getId());
              cm.setUserId(memberId);
              channelMemberRepository.save(cm);

              // Cache in Redis
              redisService.addUserToChannel(memberId, channelKey);
          }

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
              cm.setChannelId(channel.getId());
              cm.setUserId(userId);
              channelMemberRepository.save(cm);

              // Cache in Redis
              redisService.addUserToChannel(userId, channelKey);
          }

          return channel;
      }

      public List<ChannelDTO> getUserChannels(Long userId) {
          List<Channel> channels = channelRepository.findChannelsByUserId(userId);
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

          if (channelMemberRepository.existsByChannelIdAndUserId(channelId, userId)) {
              throw new ConflictException("User is already a member");
          }

          ChannelMember cm = new ChannelMember();
          cm.setChannelId(channelId);
          cm.setUserId(userId);
          channelMemberRepository.save(cm);

          // Cache in Redis
          redisService.addUserToChannel(userId, "group:" + channelId);
      }

      private ChannelDTO convertToDTO(Channel channel, Long currentUserId) {
          List<ChannelMember> members = channelMemberRepository.findByChannelId(channel.getId());

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
  ```

- [ ] **ChannelController.java**
  ```java
  @RestController
  @RequestMapping("/api/channels")
  @RequiredArgsConstructor
  @CrossOrigin(origins = "*")
  public class ChannelController {

      private final ChannelService channelService;

      @GetMapping
      public ResponseEntity<List<ChannelDTO>> getUserChannels(@RequestParam Long userId) {
          List<ChannelDTO> channels = channelService.getUserChannels(userId);
          return ResponseEntity.ok(channels);
      }

      @PostMapping("/group")
      public ResponseEntity<Channel> createGroupChannel(@RequestBody @Valid CreateGroupChannelRequest request) {
          Channel channel = channelService.createGroupChannel(
                  request.getName(),
                  request.getCreatorId(),
                  request.getMemberIds()
          );
          return ResponseEntity.status(HttpStatus.CREATED).body(channel);
      }

      @PostMapping("/direct")
      public ResponseEntity<Channel> createDirectChannel(@RequestBody @Valid CreateDirectChannelRequest request) {
          Channel channel = channelService.getOrCreateDirectChannel(
                  request.getUserId1(),
                  request.getUserId2()
          );
          return ResponseEntity.ok(channel);
      }

      @PostMapping("/{channelId}/members")
      public ResponseEntity<Void> addMember(
              @PathVariable Long channelId,
              @RequestBody @Valid AddMemberRequest request) {
          channelService.addMemberToChannel(channelId, request.getUserId());
          return ResponseEntity.ok().build();
      }
  }
  ```

### 2.4 Enhanced User Management

- [ ] Update **UserService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  public class UserService {

      private final UserRepository userRepository;
      private final RedisService redisService;

      public List<UserDTO> getAllUsers() {
          return userRepository.findAll().stream()
                  .map(this::convertToDTO)
                  .collect(Collectors.toList());
      }

      public List<UserDTO> getOnlineUsers() {
          Set<Object> onlineUserIds = redisService.getOnlineUsers();
          return onlineUserIds.stream()
                  .map(id -> Long.parseLong(id.toString()))
                  .map(this::getUserById)
                  .map(this::convertToDTO)
                  .collect(Collectors.toList());
      }

      public User getUserById(Long id) {
          return userRepository.findById(id)
                  .orElseThrow(() -> new NotFoundException("User not found"));
      }

      public void updateLastSeen(Long userId) {
          User user = getUserById(userId);
          user.setLastSeenAt(LocalDateTime.now());
          userRepository.save(user);
      }

      private UserDTO convertToDTO(User user) {
          boolean isOnline = redisService.isUserOnline(user.getId());
          return new UserDTO(
                  user.getId(),
                  user.getUsername(),
                  user.getDisplayName(),
                  user.getAvatarUrl(),
                  isOnline,
                  user.getLastSeenAt()
          );
      }
  }
  ```

- [ ] Update **UserController.java**
  ```java
  @RestController
  @RequestMapping("/api/users")
  @RequiredArgsConstructor
  @CrossOrigin(origins = "*")
  public class UserController {

      private final UserService userService;

      @GetMapping
      public ResponseEntity<List<UserDTO>> getAllUsers() {
          List<UserDTO> users = userService.getAllUsers();
          return ResponseEntity.ok(users);
      }

      @GetMapping("/online")
      public ResponseEntity<List<UserDTO>> getOnlineUsers() {
          List<UserDTO> users = userService.getOnlineUsers();
          return ResponseEntity.ok(users);
      }

      @GetMapping("/{id}")
      public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
          User user = userService.getUserById(id);
          return ResponseEntity.ok(new UserDTO(user));
      }
  }
  ```

### 2.5 WebSocket Authentication

- [ ] Update **WebSocketConfig.java** to support authentication
  ```java
  @Configuration
  @EnableWebSocketMessageBroker
  @RequiredArgsConstructor
  public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

      private final JwtService jwtService;

      @Override
      public void registerStompEndpoints(StompEndpointRegistry registry) {
          registry.addEndpoint("/ws")
                  .setAllowedOriginPatterns("*")
                  .setHandshakeHandler(new DefaultHandshakeHandler() {
                      @Override
                      protected Principal determineUser(
                              ServerHttpRequest request,
                              WebSocketHandler wsHandler,
                              Map<String, Object> attributes) {
                          // Extract token from query params or headers
                          // Return authenticated principal
                          return new StompPrincipal(username);
                      }
                  })
                  .withSockJS();
      }
  }

  class StompPrincipal implements Principal {
      private String name;

      public StompPrincipal(String name) {
          this.name = name;
      }

      @Override
      public String getName() {
          return name;
      }
  }
  ```

### Phase 2 Testing Checklist
- [ ] User registration works with password hashing
- [ ] User login returns JWT token
- [ ] JWT token validates correctly
- [ ] Protected endpoints require authentication
- [ ] Group channels can be created
- [ ] Direct message channels can be created/retrieved
- [ ] Users can be added to channels
- [ ] Channel list returns user's channels
- [ ] Online user status tracks correctly
- [ ] WebSocket connections authenticate with JWT
- [ ] Authorization prevents unauthorized access

---

## Phase 3: Enhanced Features

### Goal
Typing indicators, read receipts, message search, file uploads, and notifications.

### 3.1 Typing Indicators

- [ ] **TypingIndicatorService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  public class TypingIndicatorService {

      private final RedisService redisService;
      private final SimpMessagingTemplate messagingTemplate;
      private static final long TYPING_TIMEOUT = 5000; // 5 seconds

      public void userStartedTyping(Long userId, Long channelId, String username) {
          String key = "channel:" + channelId + ":typing";
          redisService.addTypingUser(key, userId, TYPING_TIMEOUT);

          // Broadcast typing event
          TypingEvent event = new TypingEvent(userId, username, true);
          messagingTemplate.convertAndSend("/topic/channel/" + channelId + "/typing", event);
      }

      public void userStoppedTyping(Long userId, Long channelId) {
          String key = "channel:" + channelId + ":typing";
          redisService.removeTypingUser(key, userId);

          // Broadcast stopped typing event
          TypingEvent event = new TypingEvent(userId, null, false);
          messagingTemplate.convertAndSend("/topic/channel/" + channelId + "/typing", event);
      }
  }

  @Data
  @AllArgsConstructor
  class TypingEvent {
      private Long userId;
      private String username;
      private boolean isTyping;
  }
  ```

- [ ] Add typing endpoints to **ChatController.java**
  ```java
  @MessageMapping("/typing/{channelId}/start")
  public void startTyping(@DestinationVariable Long channelId, @Payload TypingRequest request) {
      typingIndicatorService.userStartedTyping(request.getUserId(), channelId, request.getUsername());
  }

  @MessageMapping("/typing/{channelId}/stop")
  public void stopTyping(@DestinationVariable Long channelId, @Payload TypingRequest request) {
      typingIndicatorService.userStoppedTyping(request.getUserId(), channelId);
  }
  ```

### 3.2 Read Receipts

- [ ] Update **ChannelMember** entity with `last_read_at`

- [ ] **ReadReceiptService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  public class ReadReceiptService {

      private final ChannelMemberRepository channelMemberRepository;
      private final MessageRepository messageRepository;
      private final SimpMessagingTemplate messagingTemplate;

      public void markAsRead(Long userId, Long channelId, Long messageId) {
          ChannelMember member = channelMemberRepository
                  .findById(new ChannelMemberId(channelId, userId))
                  .orElseThrow(() -> new NotFoundException("Channel member not found"));

          member.setLastReadAt(LocalDateTime.now());
          channelMemberRepository.save(member);

          // Broadcast read receipt
          ReadReceiptEvent event = new ReadReceiptEvent(userId, channelId, messageId);
          messagingTemplate.convertAndSend("/topic/channel/" + channelId + "/read", event);
      }

      public int getUnreadCount(Long userId, Long channelId) {
          ChannelMember member = channelMemberRepository
                  .findById(new ChannelMemberId(channelId, userId))
                  .orElseThrow(() -> new NotFoundException("Channel member not found"));

          LocalDateTime lastRead = member.getLastReadAt();
          if (lastRead == null) {
              lastRead = member.getJoinedAt();
          }

          return messageRepository.countByChannelIdAndCreatedAtAfter(channelId, lastRead).intValue();
      }
  }
  ```

### 3.3 Message Search

- [ ] **SearchService.java**
  ```java
  @Service
  @RequiredArgsConstructor
  public class SearchService {

      private final MessageRepository messageRepository;

      @Query("SELECT m FROM Message m WHERE m.content LIKE %:query% " +
             "AND (:channelId IS NULL OR m.channelId = :channelId) " +
             "ORDER BY m.createdAt DESC")
      public List<Message> searchMessages(@Param("query") String query,
                                         @Param("channelId") Long channelId,
                                         Pageable pageable) {
          return messageRepository.searchMessages(query, channelId, pageable);
      }
  }
  ```

- [ ] **SearchController.java**
  ```java
  @RestController
  @RequestMapping("/api/search")
  @RequiredArgsConstructor
  public class SearchController {

      private final SearchService searchService;

      @GetMapping("/messages")
      public ResponseEntity<Page<ChatMessageDTO>> searchMessages(
              @RequestParam String query,
              @RequestParam(required = false) Long channelId,
              @RequestParam(defaultValue = "0") int page,
              @RequestParam(defaultValue = "20") int size) {

          Pageable pageable = PageRequest.of(page, size);
          List<Message> results = searchService.searchMessages(query, channelId, pageable);
          // Convert to DTOs and return
          return ResponseEntity.ok(/* results */);
      }
  }
  ```

### 3.4 File Upload

- [ ] Add file upload configuration to `application.yml`
  ```yaml
  spring:
    servlet:
      multipart:
        max-file-size: 10MB
        max-request-size: 10MB

  file:
    upload-dir: ./uploads
  ```

- [ ] **FileStorageService.java**
  ```java
  @Service
  public class FileStorageService {

      private final Path fileStorageLocation;

      @Autowired
      public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
          this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
          try {
              Files.createDirectories(this.fileStorageLocation);
          } catch (Exception ex) {
              throw new RuntimeException("Could not create upload directory", ex);
          }
      }

      public String storeFile(MultipartFile file) {
          String fileName = StringUtils.cleanPath(file.getOriginalFilename());
          try {
              String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
              Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
              Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
              return uniqueFileName;
          } catch (IOException ex) {
              throw new RuntimeException("Could not store file " + fileName, ex);
          }
      }

      public Resource loadFileAsResource(String fileName) {
          try {
              Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
              Resource resource = new UrlResource(filePath.toUri());
              if (resource.exists()) {
                  return resource;
              } else {
                  throw new NotFoundException("File not found " + fileName);
              }
          } catch (MalformedURLException ex) {
              throw new NotFoundException("File not found " + fileName);
          }
      }
  }
  ```

- [ ] **FileController.java**
  ```java
  @RestController
  @RequestMapping("/api/files")
  @RequiredArgsConstructor
  public class FileController {

      private final FileStorageService fileStorageService;
      private final ChatService chatService;

      @PostMapping("/upload")
      public ResponseEntity<FileUploadResponse> uploadFile(
              @RequestParam("file") MultipartFile file,
              @RequestParam("channelId") Long channelId,
              @RequestParam("senderId") Long senderId) {

          String fileName = fileStorageService.storeFile(file);
          String fileUrl = "/api/files/download/" + fileName;

          // Create message with file
          MessageRequest request = new MessageRequest();
          request.setChannelId(channelId);
          request.setSenderId(senderId);
          request.setContent(fileUrl);
          request.setMessageType(file.getContentType().startsWith("image/") ?
                  MessageType.IMAGE : MessageType.FILE);

          ChatMessageDTO message = chatService.processAndSendMessage(request);

          return ResponseEntity.ok(new FileUploadResponse(fileName, fileUrl, message.getId()));
      }

      @GetMapping("/download/{fileName:.+}")
      public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
          Resource resource = fileStorageService.loadFileAsResource(fileName);

          String contentType = null;
          try {
              contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
          } catch (IOException ex) {
              contentType = "application/octet-stream";
          }

          return ResponseEntity.ok()
                  .contentType(MediaType.parseMediaType(contentType))
                  .header(HttpHeaders.CONTENT_DISPOSITION,
                          "attachment; filename=\"" + resource.getFilename() + "\"")
                  .body(resource);
      }
  }
  ```

### 3.5 Message Edit/Delete

- [ ] Add fields to **Message** entity
  ```java
  @Column(name = "edited_at")
  private LocalDateTime editedAt;

  @Column(name = "is_deleted")
  private boolean isDeleted = false;
  ```

- [ ] Add methods to **ChatService**
  ```java
  public ChatMessageDTO editMessage(Long messageId, Long userId, String newContent) {
      Message message = messageRepository.findById(messageId)
              .orElseThrow(() -> new NotFoundException("Message not found"));

      if (!message.getSenderId().equals(userId)) {
          throw new UnauthorizedException("Can only edit own messages");
      }

      message.setContent(newContent);
      message.setEditedAt(LocalDateTime.now());
      message = messageRepository.save(message);

      ChatMessageDTO dto = convertToDTO(message);
      messagingTemplate.convertAndSend("/topic/channel/" + message.getChannelId() + "/edit", dto);

      return dto;
  }

  public void deleteMessage(Long messageId, Long userId) {
      Message message = messageRepository.findById(messageId)
              .orElseThrow(() -> new NotFoundException("Message not found"));

      if (!message.getSenderId().equals(userId)) {
          throw new UnauthorizedException("Can only delete own messages");
      }

      message.setIsDeleted(true);
      message.setContent("[Message deleted]");
      messageRepository.save(message);

      messagingTemplate.convertAndSend(
              "/topic/channel/" + message.getChannelId() + "/delete",
              messageId
      );
  }
  ```

### 3.6 User Profiles & Avatars

- [ ] Add avatar upload to **FileController**
- [ ] Add profile update endpoint to **UserController**
  ```java
  @PutMapping("/{id}/profile")
  public ResponseEntity<User> updateProfile(
          @PathVariable Long id,
          @RequestBody UpdateProfileRequest request) {
      User user = userService.updateProfile(id, request);
      return ResponseEntity.ok(user);
  }

  @PostMapping("/{id}/avatar")
  public ResponseEntity<String> uploadAvatar(
          @PathVariable Long id,
          @RequestParam("file") MultipartFile file) {
      String avatarUrl = userService.uploadAvatar(id, file);
      return ResponseEntity.ok(avatarUrl);
  }
  ```

### Phase 3 Testing Checklist
- [ ] Typing indicators work in real-time
- [ ] Read receipts update correctly
- [ ] Unread message counts are accurate
- [ ] Message search returns relevant results
- [ ] File uploads succeed and return URLs
- [ ] Image files display correctly
- [ ] Downloaded files are intact
- [ ] Messages can be edited (with indicator)
- [ ] Messages can be deleted
- [ ] User profiles can be updated
- [ ] Avatar uploads work correctly

---

## Configuration Files

### application.yml
```yaml
spring:
  application:
    name: chat-application

  datasource:
    url: jdbc:postgresql://localhost:5432/chatapp
    username: postgres
    password: ${DB_PASSWORD:password}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}

  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

server:
  port: 8080

jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key-here-change-in-production}
  expiration: 86400000 # 24 hours

file:
  upload-dir: ./uploads

logging:
  level:
    com.chatapp: DEBUG
    org.springframework.messaging: DEBUG
    org.springframework.web.socket: DEBUG
```

### pom.xml (Key Dependencies)
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## Testing Strategy

### Unit Tests
- [ ] Service layer tests with mocked repositories
- [ ] Repository tests with H2 in-memory database
- [ ] Redis service tests with embedded Redis
- [ ] JWT service tests

### Integration Tests
- [ ] WebSocket connection tests
- [ ] REST API endpoint tests
- [ ] Database integration tests
- [ ] Security tests (authentication/authorization)

### Performance Tests
- [ ] Load testing with multiple concurrent users
- [ ] Message throughput testing
- [ ] Redis caching effectiveness
- [ ] Database query performance

---

## Deployment Checklist

- [ ] Environment-specific configuration profiles
- [ ] Database migration scripts (Flyway/Liquibase)
- [ ] Docker containerization
  - Backend Dockerfile
  - docker-compose.yml with PostgreSQL and Redis
- [ ] Health check endpoints
- [ ] Logging configuration (file rotation, levels)
- [ ] Monitoring setup (Actuator, Prometheus)
- [ ] HTTPS/WSS configuration for production
- [ ] Database connection pooling tuned
- [ ] Redis connection pooling configured
- [ ] CORS origins restricted to production domains
- [ ] JWT secret configured securely
- [ ] File upload directory configured with proper permissions

---

## Performance Optimization

### Database
- [ ] Add appropriate indexes on frequently queried columns
- [ ] Implement pagination for large result sets
- [ ] Use database connection pooling (HikariCP)
- [ ] Query optimization and N+1 problem prevention
- [ ] Consider read replicas for scaling

### Redis
- [ ] Implement Redis connection pooling
- [ ] Set appropriate TTL for cached data
- [ ] Use Redis pipelining for batch operations
- [ ] Monitor Redis memory usage
- [ ] Implement cache eviction strategy

### WebSocket
- [ ] Configure appropriate session idle timeout
- [ ] Implement heartbeat mechanism
- [ ] Handle reconnection gracefully
- [ ] Limit maximum connections per user
- [ ] Use message compression for large payloads

---

## Security Best Practices

- [ ] Use HTTPS in production
- [ ] Implement rate limiting
- [ ] Validate all user inputs
- [ ] Sanitize message content (XSS prevention)
- [ ] Use parameterized queries (prevent SQL injection)
- [ ] Implement CSRF protection
- [ ] Secure file uploads (type/size validation)
- [ ] Hash passwords with BCrypt
- [ ] Rotate JWT secrets regularly
- [ ] Implement account lockout after failed attempts
- [ ] Log security events

---

## Monitoring & Observability

- [ ] Spring Boot Actuator endpoints
- [ ] Prometheus metrics export
- [ ] Grafana dashboards
- [ ] ELK stack for log aggregation
- [ ] Application performance monitoring (APM)
- [ ] WebSocket connection metrics
- [ ] Redis cache hit/miss rates
- [ ] Database query performance tracking
