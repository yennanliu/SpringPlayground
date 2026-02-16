# Concurrency Locking Design for Hotel Booking System

## Overview

When running multiple instances of the hotel booking application, we need to handle concurrency issues to prevent double booking of rooms. This document outlines different locking strategies to ensure data consistency and prevent race conditions.

## Problem Statement

Multiple users booking the same room simultaneously can cause:
- Double booking of the same room
- Data inconsistency 
- Race conditions between application instances
- Loss of booking data

## Locking Strategies

## 1. Optimistic Locking

### **Concept:**
- Assumes conflicts are rare
- Uses version numbers/timestamps to detect conflicts
- Allows concurrent reads, detects conflicts at commit time
- Retries failed operations

### **Implementation Strategy:**

**A. Version-based Optimistic Locking:**
```java
@Entity
public class Room {
    @Version
    private Long version;  // JPA will auto-increment on updates
    
    private Boolean available;
    // other fields...
}

@Entity  
public class Booking {
    @Version
    private Long version;
    
    @ManyToOne
    private Room room;
    // other fields...
}
```

**B. Service Layer with Retry Logic:**
```java
@Service
@Transactional
public class HotelService {
    
    @Retryable(value = {OptimisticLockException.class}, maxAttempts = 3)
    public Booking createBooking(Long roomId, ...) {
        // 1. Read room with current version
        Room room = roomRepository.findById(roomId);
        
        // 2. Check availability
        if (!room.getAvailable()) {
            throw new RoomNotAvailableException();
        }
        
        // 3. Create booking and update room
        room.setAvailable(false);
        roomRepository.save(room); // JPA checks version here
        
        return bookingRepository.save(new Booking(...));
        // If version changed, OptimisticLockException thrown -> retry
    }
}
```

**C. Custom Optimistic Lock with Timestamp:**
```java
@Entity
public class Room {
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
    
    @PreUpdate
    @PrePersist  
    public void updateTimestamp() {
        lastModified = LocalDateTime.now();
    }
}

// In service:
public Booking createBookingWithTimestamp(Long roomId, LocalDateTime expectedLastModified, ...) {
    Room room = roomRepository.findById(roomId);
    
    if (!room.getLastModified().equals(expectedLastModified)) {
        throw new OptimisticLockException("Room was modified by another user");
    }
    
    // Proceed with booking...
}
```

### **Pros:**
- High throughput and concurrency
- No blocking of other transactions
- Simple to implement with JPA
- Good for low-contention scenarios

### **Cons:**
- Requires retry logic
- May cause user frustration if retries fail
- Not suitable for high-contention scenarios

## 2. Pessimistic Locking

### **Concept:**
- Assumes conflicts are common
- Acquires exclusive locks immediately
- Blocks other transactions until lock is released
- Prevents conflicts but reduces concurrency

### **Implementation Strategy:**

**A. JPA Pessimistic Locking:**
```java
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.id = :id")
    Optional<Room> findByIdWithLock(@Param("id") Long id);
    
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT r FROM Room r WHERE r.available = true")
    List<Room> findAvailableRoomsWithReadLock();
}
```

**B. Service with Pessimistic Lock:**
```java
@Service
@Transactional
public class HotelService {
    
    public Booking createBookingWithLock(Long roomId, ...) {
        // 1. Acquire exclusive lock on room
        Room room = roomRepository.findByIdWithLock(roomId)
            .orElseThrow(() -> new RoomNotFoundException());
            
        // 2. Only this transaction can modify room now
        if (!room.getAvailable()) {
            throw new RoomNotAvailableException();
        }
        
        // 3. Update room and create booking
        room.setAvailable(false);
        roomRepository.save(room);
        
        return bookingRepository.save(new Booking(...));
        // Lock released when transaction commits
    }
}
```

**C. Database-level Locking:**
```java
@Repository
public class RoomRepositoryImpl {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Room lockRoomForBooking(Long roomId) {
        return entityManager
            .createQuery("SELECT r FROM Room r WHERE r.id = :id", Room.class)
            .setParameter("id", roomId)
            .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
            .getSingleResult();
    }
}
```

### **Lock Types:**
- `PESSIMISTIC_READ`: Shared lock, allows other reads
- `PESSIMISTIC_WRITE`: Exclusive lock, blocks all access  
- `PESSIMISTIC_FORCE_INCREMENT`: Like WRITE but increments version

### **Pros:**
- Strong consistency guarantees
- No retry logic needed
- Prevents all conflicts
- Good for high-contention scenarios

### **Cons:**
- Reduced concurrency
- Potential for deadlocks
- Lower throughput
- Lock timeout issues

## 3. Hybrid Approach (Recommended)

### **Strategy:**
Combine both approaches based on conflict probability:

```java
@Service
public class HotelService {
    
    // For high-contention rooms (popular rooms)
    @Transactional
    public Booking createBookingPessimistic(Long roomId, ...) {
        Room room = roomRepository.findByIdWithLock(roomId);
        return doBooking(room, ...);
    }
    
    // For low-contention rooms  
    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
    public Booking createBookingOptimistic(Long roomId, ...) {
        Room room = roomRepository.findById(roomId);
        return doBooking(room, ...);
    }
    
    // Route based on room popularity
    public Booking createBooking(Long roomId, ...) {
        if (isHighContentionRoom(roomId)) {
            return createBookingPessimistic(roomId, ...);
        } else {
            return createBookingOptimistic(roomId, ...);
        }
    }
    
    private boolean isHighContentionRoom(Long roomId) {
        // Check booking frequency, room type, current load
        return bookingMetrics.getRecentBookingAttempts(roomId) > CONTENTION_THRESHOLD;
    }
}
```

### **Dynamic Strategy Selection:**
```java
@Component
public class LockingStrategy {
    
    public LockType determineLockType(Long roomId, LocalDateTime bookingTime) {
        // Peak hours logic
        if (isPeakBookingTime(bookingTime)) {
            return LockType.PESSIMISTIC;
        }
        
        // Popular room logic  
        if (isPopularRoom(roomId)) {
            return LockType.PESSIMISTIC;
        }
        
        // System load logic
        if (getCurrentSystemLoad() > 0.8) {
            return LockType.OPTIMISTIC; // Prefer throughput
        }
        
        return LockType.OPTIMISTIC; // Default
    }
}
```

## 4. Database Constraints Approach

### **Alternative Strategy:**
Use database unique constraints to prevent double booking:

```sql
-- Add unique constraint on room + date range
ALTER TABLE bookings ADD CONSTRAINT unique_room_dates 
UNIQUE (room_id, check_in_date, check_out_date);

-- Or use a status-based approach
ALTER TABLE rooms ADD CONSTRAINT check_available 
CHECK (available IN (true, false));

-- Prevent overlapping bookings
CREATE UNIQUE INDEX idx_room_booking_period 
ON bookings (room_id, check_in_date, check_out_date) 
WHERE status = 'CONFIRMED';
```

```java
@Service
public class HotelService {
    
    public Booking createBookingWithConstraints(Long roomId, ...) {
        try {
            // Let database handle concurrency
            Booking booking = new Booking(room, ...);
            return bookingRepository.save(booking);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("unique_room_dates")) {
                throw new RoomAlreadyBookedException("Room is already booked for these dates");
            }
            throw e;
        }
    }
}
```

### **Benefits:**
- Database-level consistency
- No application-level locking complexity
- Works across multiple application instances
- High performance

## 5. Performance Comparison

| Approach | Throughput | Consistency | Complexity | Best Use Case |
|----------|------------|-------------|------------|---------------|
| Optimistic | High | Eventually Strong | Low | Low contention scenarios |
| Pessimistic | Low-Medium | Strong | Medium | High contention scenarios |
| Hybrid | Medium-High | Strong | High | Mixed workload patterns |
| DB Constraints | High | Strong | Low | Simple booking rules |

## 6. Implementation Considerations

### **A. Retry Configuration:**
```java
@Configuration
@EnableRetry
public class RetryConfig {
    
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate template = new RetryTemplate();
        
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(100); // 100ms between retries
        template.setBackOffPolicy(backOffPolicy);
        
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        template.setRetryPolicy(retryPolicy);
        
        return template;
    }
}
```

### **B. Lock Timeout Configuration:**
```properties
# application.properties
spring.jpa.properties.javax.persistence.lock.timeout=5000
spring.jpa.properties.hibernate.dialect.lock_timeout=5000
```

### **C. Exception Handling:**
```java
@ControllerAdvice
public class BookingExceptionHandler {
    
    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(OptimisticLockException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("Room was booked by another user. Please try again."));
    }
    
    @ExceptionHandler(PessimisticLockException.class)  
    public ResponseEntity<ErrorResponse> handlePessimisticLock(PessimisticLockException e) {
        return ResponseEntity.status(HttpStatus.LOCKED)
            .body(new ErrorResponse("Room is temporarily locked. Please try again shortly."));
    }
}
```

## 7. Monitoring & Metrics

```java
@Component
public class BookingMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Map<Long, AtomicInteger> roomContentionMap = new ConcurrentHashMap<>();
    
    public void recordOptimisticLockRetry(String outcome) {
        meterRegistry.counter("booking.optimistic.retry", "outcome", outcome).increment();
    }
    
    public void recordPessimisticLockWait(Duration waitTime) {
        meterRegistry.timer("booking.pessimistic.wait").record(waitTime);
    }
    
    public void recordBookingAttempt(Long roomId) {
        roomContentionMap.computeIfAbsent(roomId, k -> new AtomicInteger(0)).incrementAndGet();
        meterRegistry.counter("booking.attempts", "roomId", roomId.toString()).increment();
    }
    
    public int getRecentBookingAttempts(Long roomId) {
        return roomContentionMap.getOrDefault(roomId, new AtomicInteger(0)).get();
    }
}
```

### **Key Metrics to Track:**
- Optimistic lock retry rates
- Pessimistic lock wait times
- Booking success/failure rates
- Room contention levels
- Database deadlock occurrences

## 8. Testing Strategies

### **A. Concurrency Test:**
```java
@Test
public void testConcurrentBookings() throws InterruptedException {
    Long roomId = 1L;
    int numberOfThreads = 10;
    CountDownLatch latch = new CountDownLatch(numberOfThreads);
    ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
    
    List<Future<Booking>> futures = new ArrayList<>();
    
    for (int i = 0; i < numberOfThreads; i++) {
        Future<Booking> future = executor.submit(() -> {
            try {
                latch.countDown();
                latch.await(); // All threads start simultaneously
                return hotelService.createBooking(roomId, "Guest" + Thread.currentThread().getId(), 
                    "test@email.com", LocalDate.now(), LocalDate.now().plusDays(1));
            } catch (Exception e) {
                return null;
            }
        });
        futures.add(future);
    }
    
    // Only one booking should succeed
    long successfulBookings = futures.stream()
        .map(f -> {
            try { return f.get(); } catch (Exception e) { return null; }
        })
        .filter(Objects::nonNull)
        .count();
        
    assertEquals(1, successfulBookings);
}
```

### **B. Load Testing:**
```java
@Test
public void loadTestBookingSystem() {
    // Simulate high load with JMeter or similar tool
    // Measure throughput, response times, error rates
    // Validate data consistency after test completion
}
```

## 9. Recommendations

### **For Production Implementation:**

1. **Start with Hybrid Approach**: Use optimistic locking as default with pessimistic for high-contention scenarios

2. **Implement Comprehensive Monitoring**: Track lock contention, retry rates, and performance metrics

3. **Use Database Constraints**: Add unique constraints as a safety net

4. **Configure Appropriate Timeouts**: Set reasonable lock timeouts to prevent indefinite blocking

5. **Handle Exceptions Gracefully**: Provide meaningful error messages to users

6. **Test Thoroughly**: Implement comprehensive concurrency and load tests

7. **Consider Business Rules**: Factor in cancellation policies, overbooking strategies, and booking windows

### **Migration Strategy:**
1. Phase 1: Implement optimistic locking with version fields
2. Phase 2: Add monitoring and metrics collection  
3. Phase 3: Implement pessimistic locking for high-contention scenarios
4. Phase 4: Deploy hybrid strategy with dynamic selection
5. Phase 5: Fine-tune based on production metrics

The **hybrid approach** with **database constraints** as a safety net provides the best balance of performance, consistency, and reliability for a production hotel booking system handling multiple concurrent users and application instances.