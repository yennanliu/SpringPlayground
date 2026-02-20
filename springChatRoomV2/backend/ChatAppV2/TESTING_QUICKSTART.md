# Testing Quick Start Guide

## Prerequisites

### 1. Start Redis
```bash
docker run -d --name chatapp-redis -p 6379:6379 redis:latest
```

Verify Redis is running:
```bash
docker ps | grep redis
```

### 2. Verify PostgreSQL (Optional for Tests)
Tests use H2 in-memory database, but production uses PostgreSQL.

---

## Run All Tests

```bash
cd backend/ChatAppV2
./mvnw test
```

---

## Run Specific Test Suites

### Concurrent Messaging Tests (MOST IMPORTANT)
```bash
./mvnw test -Dtest="ConcurrentMessagingTest"
```

**Tests**:
- 10 users sending 50 messages concurrently
- 100 concurrent messages stress test
- Multiple subscribers receiving same messages
- Message ordering by timestamp
- Thread-safe processing

**Expected Duration**: 60-120 seconds

---

### Message Flow Integration Tests
```bash
./mvnw test -Dtest="MessageFlowIntegrationTest"
```

**Tests**:
- Complete message flow (WebSocket → Service → DB → Redis)
- Message field validation
- Different message types (TEXT, IMAGE, FILE)
- Special characters and emoji handling
- Database persistence
- Redis caching

**Expected Duration**: 30-60 seconds

---

### WebSocket Connection Tests
```bash
./mvnw test -Dtest="WebSocketConnectionTest"
```

**Tests**:
- Connection lifecycle
- Online/offline status in Redis
- Multiple simultaneous connections
- Reconnection handling
- Session events

**Expected Duration**: 30-60 seconds

---

### Error Handling Tests
```bash
./mvnw test -Dtest="ErrorHandlingIntegrationTest"
```

**Tests**:
- Unauthorized access prevention
- Not found errors
- Validation errors
- Edge cases
- SQL injection protection

**Expected Duration**: 30-45 seconds

---

## Run Single Test Method

```bash
./mvnw test -Dtest="ConcurrentMessagingTest#testConcurrentMessageSending"
```

---

## View Test Results

### Console Output
Tests will show pass/fail in console.

### Detailed Reports
```bash
# Text reports
ls target/surefire-reports/*.txt

# XML reports (for CI/CD)
ls target/surefire-reports/*.xml

# View specific test
cat target/surefire-reports/com.yen.ChatAppV2.integration.ConcurrentMessagingTest.txt
```

---

## Troubleshooting

### Redis Connection Error
```
Error: Cannot connect to Redis at localhost:6379
```

**Solution**: Start Redis
```bash
docker run -d -p 6379:6379 redis
```

---

### Port Already in Use
```
Error: Port 6379 already in use
```

**Solution**: Stop existing Redis or use different port
```bash
docker stop chatapp-redis
docker rm chatapp-redis
```

---

### All Tests Failing
Check if Redis is running:
```bash
docker ps | grep redis
```

If not running, start Redis and re-run tests.

---

## Expected Results

When all tests pass, you should see:
```
[INFO] Results:
[INFO]
[INFO] Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

---

## Test Coverage

### What's Tested ✅
- Message sending via WebSocket
- Concurrent message handling (10+ users)
- Message persistence to database
- Message caching in Redis
- Broadcasting to subscribers
- Connection lifecycle
- Online/offline status tracking
- Error handling and security
- Edit/delete operations

### What's NOT Tested ❌
- Performance benchmarks
- Load testing (1000+ users)
- Network failure scenarios
- Redis failover
- Frontend integration

---

## CI/CD Integration

### GitHub Actions Example
```yaml
name: Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest

    services:
      redis:
        image: redis:latest
        ports:
          - 6379:6379

    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'

      - name: Run Tests
        run: |
          cd backend/ChatAppV2
          ./mvnw test

      - name: Publish Test Results
        uses: EnricoMi/publish-unit-test-result-action@v1
        if: always()
        with:
          files: backend/ChatAppV2/target/surefire-reports/*.xml
```

---

## Quick Test Command Summary

```bash
# Start Redis
docker run -d -p 6379:6379 redis

# Run all tests
cd backend/ChatAppV2 && ./mvnw test

# Run concurrent tests (most important)
./mvnw test -Dtest="ConcurrentMessagingTest"

# Run with verbose output
./mvnw test -X

# Skip tests
./mvnw clean install -DskipTests
```

---

## Questions?

See full test report: `TEST_REPORT.md`
