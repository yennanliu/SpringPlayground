# Two-Phase Commit (2PC) Implementation Plan for Hotel Booking App

## Overview
This document outlines the implementation strategy for Two-Phase Commit protocol in the hotel booking application to demonstrate distributed transaction management for POC purposes.

## Current Architecture Analysis
- Single Spring Boot application with MySQL database
- Simple booking flow: Room availability check → Create booking → Update room status
- Monolithic architecture with direct database access

## 2PC Architecture Design

### 1. System Components Decomposition
To demonstrate 2PC, we need to split the monolithic booking process into multiple distributed components:

#### Resource Managers (Participants)
1. **Room Service** - Manages room inventory and availability
2. **Booking Service** - Handles booking records and customer data  
3. **Payment Service** - Processes payment transactions (mock implementation)
4. **Notification Service** - Sends booking confirmations (mock implementation)

#### Transaction Manager (Coordinator)
- **Booking Coordinator Service** - Orchestrates the 2PC protocol across all participants

### 2. Implementation Steps

#### Phase 1: Infrastructure Setup
1. **Create Service Interfaces**
   - Define `TransactionParticipant` interface with prepare/commit/rollback methods
   - Create `TransactionCoordinator` interface for managing distributed transactions
   - Implement `TransactionContext` to track transaction state

2. **Database Schema Changes**
   - Add transaction log tables for each participant
   - Add prepared transaction state tracking
   - Implement transaction timeout mechanisms

#### Phase 2: Participant Services Implementation
1. **Room Service Participant**
   - Implement room reservation in prepared state
   - Handle commit (finalize reservation) and rollback (release reservation)
   - Add pessimistic locking for room availability during prepare phase

2. **Booking Service Participant** 
   - Create booking record in prepared state
   - Handle commit (activate booking) and rollback (delete booking)
   - Maintain booking state transitions

3. **Payment Service Participant**
   - Mock payment authorization in prepare phase
   - Handle commit (capture payment) and rollback (void authorization)
   - Simulate payment gateway interactions

4. **Notification Service Participant**
   - Queue notification messages in prepare phase
   - Handle commit (send notifications) and rollback (cancel notifications)
   - Mock email/SMS delivery

#### Phase 3: Transaction Coordinator Implementation
1. **Coordinator Logic**
   - Implement 2PC protocol state machine
   - Handle participant registration and communication
   - Manage transaction timeouts and recovery
   - Implement logging for transaction audit trail

2. **Communication Layer**
   - REST API endpoints for participant communication
   - Asynchronous message handling for prepare/commit/rollback
   - Error handling and retry mechanisms

#### Phase 4: Integration and Testing
1. **End-to-End Booking Flow**
   - Replace direct database calls with 2PC transaction calls
   - Update booking controller to use transaction coordinator
   - Handle distributed transaction exceptions

2. **Testing Framework**
   - Unit tests for each participant
   - Integration tests for 2PC protocol
   - Failure scenario testing (network partitions, timeouts)

## Detailed Implementation Architecture

### Transaction Flow Diagram
```
Client Request
     ↓
Booking Controller
     ↓
Transaction Coordinator
     ↓
Phase 1: PREPARE
├── Room Service → PREPARED/ABORTED
├── Booking Service → PREPARED/ABORTED  
├── Payment Service → PREPARED/ABORTED
└── Notification Service → PREPARED/ABORTED
     ↓
Phase 2: COMMIT/ROLLBACK
├── Room Service → COMMITTED/ROLLED_BACK
├── Booking Service → COMMITTED/ROLLED_BACK
├── Payment Service → COMMITTED/ROLLED_BACK
└── Notification Service → COMMITTED/ROLLED_BACK
     ↓
Response to Client
```

### Data Structures

#### Transaction Context
```java
class TransactionContext {
    String transactionId;
    TransactionState state;
    List<String> participants;
    LocalDateTime startTime;
    LocalDateTime timeoutTime;
    Map<String, ParticipantState> participantStates;
}
```

#### Transaction Log Entry
```java
class TransactionLogEntry {
    String transactionId;
    String participantId;
    TransactionPhase phase;
    ParticipantState state;
    LocalDateTime timestamp;
    String payload;
}
```

## Technical Considerations

### 1. Consistency and Durability
- Each participant must log prepare decisions before responding
- Coordinator must log commit/rollback decisions before proceeding
- Use write-ahead logging (WAL) for transaction recovery

### 2. Timeout Management
- Set appropriate timeouts for prepare and commit phases
- Implement participant heartbeat monitoring
- Handle coordinator failure and recovery scenarios

### 3. Error Handling
- Network partition tolerance
- Participant failure during prepare/commit phases
- Coordinator failure and recovery mechanisms
- Handling of uncertain transaction outcomes

### 4. Performance Optimization
- Minimize blocking time during prepare phase
- Implement parallel participant communication
- Add transaction pooling and connection management
- Consider read-only transaction optimizations

## Mock Services Implementation Strategy

### Payment Service Mock
- Simulate 10% failure rate for testing
- Add artificial delays to simulate network latency
- Implement idempotent operations for retry scenarios

### Notification Service Mock  
- Queue-based message handling
- Simulate delivery confirmations
- Handle rollback scenarios (message cancellation)

## Testing Strategy

### 1. Happy Path Testing
- Successful booking with all participants committing
- Verify data consistency across all services
- Performance benchmarking against direct database approach

### 2. Failure Scenario Testing
- Participant failure during prepare phase
- Participant failure during commit phase
- Coordinator failure and recovery
- Network partition scenarios
- Timeout handling

### 3. Concurrency Testing
- Multiple simultaneous booking requests
- Race condition handling
- Deadlock prevention and detection

## Migration Strategy

### Phase 1: Parallel Implementation
- Keep existing booking flow intact
- Implement 2PC as alternative booking path
- Add feature flag to switch between approaches

### Phase 2: Gradual Rollout
- Route percentage of traffic through 2PC
- Monitor performance and error rates
- Gradual increase in 2PC traffic

### Phase 3: Full Migration
- Switch all booking traffic to 2PC
- Remove legacy booking code
- Optimize based on production metrics

## Expected Outcomes

### Benefits Demonstration
- Guaranteed consistency across distributed components
- Atomic booking operations spanning multiple services
- Clear transaction boundaries and rollback capabilities

### Trade-offs Showcase
- Increased latency due to 2PC protocol overhead
- Higher complexity in error handling and recovery
- Resource overhead from transaction logging and coordination

### Learning Objectives
- Understanding distributed transaction challenges
- Experience with consensus protocols
- Appreciation for simpler consistency models (like optimistic locking)

## Implementation Timeline

1. **Week 1**: Infrastructure and interfaces setup
2. **Week 2**: Participant services implementation
3. **Week 3**: Transaction coordinator development
4. **Week 4**: Integration, testing, and documentation

This POC will provide hands-on experience with distributed transactions while highlighting why simpler approaches (like optimistic locking) are often preferred for single-database applications.