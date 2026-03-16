# Backend Code Review & Refactoring Recommendations

**Project:** FlinkRestService (Spring Data Platform)
**Date:** 2026-03-16
**Reviewer:** Senior Java Developer

---

## REFACTORING COMPLETED

The following refactoring has been implemented:

### New Files Created:
- `model/enums/ClusterStatus.java` - Enum for cluster status values
- `model/enums/JobStatus.java` - Enum for job status values
- `exception/EntityNotFoundException.java` - Custom exception for entity not found
- `exception/ValidationException.java` - Custom exception for validation errors
- `exception/ExternalServiceException.java` - Custom exception for external service errors
- `exception/GlobalExceptionHandler.java` - Global exception handler with @RestControllerAdvice

### Files Refactored:

| File | Changes |
|------|---------|
| `Service/ClusterService.java` | Constructor injection, proper Optional handling, ClusterStatus enum, @Transactional, fixed duplicate findById calls |
| `Service/JobService.java` | Constructor injection, proper Optional handling, @Transactional, UriComponentsBuilder, static Gson instance |
| `Service/RestTemplateService.java` | Constructor injection, removed thread-safety issues, proper exception handling, parameterized logging |
| `Service/ZeppelinService.java` | Constructor injection, fixed string comparison bug (== to .equals), fixed return value bug, proper exception handling |
| `Service/SqlJobService.java` | Constructor injection, UriComponentsBuilder, improved logging |
| `Controller/ClusterController.java` | Constructor injection, @Valid annotations, RESTful endpoints, legacy backward-compatible endpoints |
| `Controller/JobController.java` | Constructor injection, @Valid annotations, RESTful endpoints, legacy backward-compatible endpoints |
| `Controller/SqlJobController.java` | Constructor injection, @Valid annotations |
| `Repository/JobRepository.java` | Added `findByJobId(String jobId)` method |
| `Task/UpdateJobStatus.java` | Constructor injection, SLF4J logging, configurable cron, try-catch for scheduler safety |
| `Config/WebConfig.java` | Removed duplicate CORS config, made allowed-origins configurable |
| `Common/ApiResponse.java` | Added Lombok @Getter, static factory methods, timestamp at construction |
| `model/dto/cluster/AddClusterDto.java` | Added @NotBlank, @NotNull, @Min, @Max validations |
| `model/dto/cluster/UpdateClusterDto.java` | Added validation annotations |
| `model/dto/cluster/PingClusterDto.java` | Added @NotBlank validation |
| `model/dto/job/JobSubmitDto.java` | Added @NotNull, @Min validations |
| `model/dto/job/JobUpdateDto.java` | Added @NotNull validation |
| `model/dto/job/SqlJobSubmitDto.java` | Added @NotBlank validation, renamed toString method |
| `application.properties` | Added cors.allowed-origins and job.status.update.cron configs |

### Tests Updated:
- `Service/ClusterServiceTest.java` - Updated for constructor injection, added more test cases
- `Service/JobServiceTest.java` - Updated for constructor injection, proper mocking

---

## Executive Summary

The FlinkRestService is a Spring Boot REST API that orchestrates Apache Flink cluster operations with Zeppelin notebook integration. The codebase follows a standard layered architecture (Controller → Service → Repository) but has several areas that need improvement for better maintainability, reliability, and adherence to Spring best practices.

**Overall Assessment:** 🟡 Moderate - Functional but needs refactoring

---

## Table of Contents

1. [Critical Issues](#1-critical-issues)
2. [Code Smell & Anti-Patterns](#2-code-smells--anti-patterns)
3. [Architecture Improvements](#3-architecture-improvements)
4. [Specific File Recommendations](#4-specific-file-recommendations)
5. [Recommended Refactoring Tasks](#5-recommended-refactoring-tasks)
6. [Best Practices Not Followed](#6-best-practices-not-followed)

---

## 1. Critical Issues

### 1.1 Duplicate Repository Calls

**Location:** `ClusterService.java:33-39`, `ClusterService.java:53-58`, `ClusterService.java:69-76`

```java
// PROBLEM: Calls findById twice - inefficient and potential race condition
if (clusterRepository.findById(clusterId).isPresent()){
    return clusterRepository.findById(clusterId).get();
}
```

**Impact:** Performance degradation, potential inconsistency between checks
**Fix:** Use Optional properly with `orElse()`, `orElseGet()`, or `orElseThrow()`

```java
// RECOMMENDED:
return clusterRepository.findById(clusterId)
    .orElseThrow(() -> new EntityNotFoundException("Cluster not found: " + clusterId));
```

### 1.2 Silent Failures - Returning Null Instead of Exceptions

**Location:** Multiple services return `null` on not-found scenarios

```java
// ClusterService.java:38-39
log.warn("No Cluster found, clusterId = " + clusterId);
return null;  // PROBLEM: Caller must handle null, easy to miss
```

**Impact:** NullPointerExceptions downstream, unclear error handling
**Fix:** Throw custom exceptions or return `Optional<T>`

### 1.3 Missing Input Validation

**Location:** Controllers accept DTOs without validation

```java
// ClusterController.java:42-43
@PostMapping("/add_cluster")
public ResponseEntity<ApiResponse> addJobJar(@RequestBody AddClusterDto addClusterDto){
    // No @Valid annotation, no validation on DTO fields
```

**Impact:** Invalid data can enter the system, potential security issues
**Fix:** Add `@Valid` annotation and validation constraints on DTOs

### 1.4 SQL Injection Risk in Job Lookup

**Location:** `JobService.java:160-170`

```java
// PROBLEM: Iterates through ALL jobs to find by jid - N+1 problem
public Job getJobByJid(String jid) {
    List<Job> jobs = jobRepository.findAll();
    for (Job job : jobs) {
        if (job.getJobId().equals(jid)) {
            return job;
        }
    }
```

**Impact:** Performance degradation as data grows, unnecessary memory usage
**Fix:** Add proper JPA query method

```java
// In JobRepository:
Optional<Job> findByJobId(String jobId);
```

### 1.5 Hardcoded Magic Strings

**Location:** Throughout the codebase

```java
// ClusterService.java:47, 61
cluster.setStatus("Added");
cluster.setStatus("Updated");
cluster.setStatus("connected");
cluster.setStatus("not_connected");
```

**Impact:** Typos can cause bugs, inconsistent status values
**Fix:** Create enum for cluster/job states

```java
public enum ClusterStatus {
    ADDED, UPDATED, CONNECTED, NOT_CONNECTED
}
```

---

## 2. Code Smells & Anti-Patterns

### 2.1 Field Injection vs Constructor Injection

**Location:** All classes use `@Autowired` field injection

```java
@Autowired
private ClusterService clusterService;
```

**Problem:**
- Makes testing harder
- Hides dependencies
- Allows creating objects in invalid state

**Fix:** Use constructor injection with `@RequiredArgsConstructor` (Lombok)

```java
@RequiredArgsConstructor
public class ClusterController {
    private final ClusterService clusterService;
}
```

### 2.2 Inconsistent Exception Handling

**Location:** Throughout services

```java
// Some places throw RuntimeException
throw new RuntimeException("Job jar NOT exists");

// Others return null
return null;

// Others catch and swallow
catch (Exception e){
    e.printStackTrace();  // PROBLEM: Logging via printStackTrace
}
```

**Fix:** Implement global exception handling with `@ControllerAdvice`

### 2.3 REST API Design Issues

**Location:** Controllers

| Current | Problem | Recommended |
|---------|---------|-------------|
| `POST /cluster/add_cluster` | Redundant, not RESTful | `POST /clusters` |
| `POST /cluster/update` | Should be PUT/PATCH | `PUT /clusters/{id}` |
| `POST /cluster/ping` | Query should be GET | `GET /clusters/{id}/ping` |
| `POST /job/cancel` | Missing path variable | `POST /jobs/{id}/cancel` |
| `GET /cluster/` | Trailing slash inconsistency | `GET /clusters` |

### 2.4 CORS Configuration Duplication

**Location:** `WebConfig.java:11-32`

```java
// PROBLEM: CORS is configured TWICE - once in the class, once in the bean
@Override
public void addCorsMappings(CorsRegistry registry) {
    // First configuration
}

@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            // Second configuration - conflicts!
        }
    };
}
```

**Impact:** Confusing, potential conflicts
**Fix:** Remove duplicate, keep only one CORS configuration

### 2.5 Inconsistent Naming Conventions

| Issue | Examples |
|-------|----------|
| Camel vs snake_case in URLs | `/add_cluster`, `/sql_job` vs `/job` |
| DTO naming | `AddClusterDto` vs `SqlJobSubmitDto` |
| Method naming | `addJobJar()` in ClusterController (misleading) |

### 2.6 Missing Transaction Management

**Location:** Service methods that perform multiple DB operations

```java
// JobService.java:139-156 - Multiple saves without @Transactional
jobs.stream().forEach(job -> {
    // ...
    jobRepository.save(newJob);
    // ...
    jobRepository.save(currentJob);
});
```

**Fix:** Add `@Transactional` annotation to service methods

---

## 3. Architecture Improvements

### 3.1 Missing Global Exception Handler

Create `GlobalExceptionHandler.java`:

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse(false, ex.getMessage()));
    }
}
```

### 3.2 Missing Service Interfaces

Services should have interfaces for better testability:

```java
public interface ClusterService {
    List<Cluster> getClusters();
    Cluster getClusterById(Integer clusterId);
    // ...
}

@Service
public class ClusterServiceImpl implements ClusterService {
    // ...
}
```

### 3.3 Missing DTO-Entity Mapper

Currently, mapping is manual and error-prone:

```java
// Current - manual mapping
Cluster cluster = new Cluster();
cluster.setUrl(addClusterDto.getUrl());
cluster.setPort(addClusterDto.getPort());
```

**Recommendation:** Use MapStruct or ModelMapper

```java
@Mapper(componentModel = "spring")
public interface ClusterMapper {
    Cluster toEntity(AddClusterDto dto);
    ClusterDto toDto(Cluster entity);
}
```

### 3.4 Missing Health Check Endpoint

Add Spring Actuator for proper health monitoring instead of custom ping:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 3.5 Consider Adding API Versioning

Current APIs lack versioning:

```java
// Current
@RequestMapping("/cluster")

// Recommended
@RequestMapping("/api/v1/clusters")
```

---

## 4. Specific File Recommendations

### 4.1 ClusterService.java

| Line | Issue | Recommendation |
|------|-------|----------------|
| 22-26 | Field injection | Constructor injection |
| 35-36 | Double findById call | Use Optional.orElseThrow() |
| 47, 61 | Magic strings | Use ClusterStatus enum |
| 79 | Null comparison bug | `resp.toString().equals(null)` should be `resp.getBody() == null` |
| 92 | String status check | Use `resp.getStatusCode().is2xxSuccessful()` |

### 4.2 JobService.java

| Line | Issue | Recommendation |
|------|-------|----------------|
| 44 | Variable naming | `BASE_URL` should be `baseUrl` (not constant) |
| 72-78 | URL building | Use UriComponentsBuilder |
| 88-89 | Gson instance creation | Inject as bean or use static |
| 103-106 | Missing return after warning | Throw exception or add return |
| 160-170 | Full table scan | Add JPA query method |
| 172-200 | RestTemplate created inline | Use injected RestTemplateService |
| 174 | Unused Gson | Remove dead code |

### 4.3 RestTemplateService.java

| Line | Issue | Recommendation |
|------|-------|----------------|
| 16-20 | Instance fields for request state | Remove - creates thread safety issues |
| 44-47 | Modifying shared headers | Create new headers per request |
| 51 | Log message typo | "failed OK" - should be "succeeded" |
| 52-55 | Generic exception catch | Catch specific exceptions |
| 54 | printStackTrace() | Use log.error() with exception |

### 4.4 ZeppelinService.java

| Line | Issue | Recommendation |
|------|-------|----------------|
| 13 | Unused import | Remove `org.aspectj.weaver.ast.Not` |
| 59-60, 66-67 | Duplicate findById pattern | Extract to method |
| 76 | String comparison bug | `==` should be `.equals()` |
| 100-103 | Catch-log-continue | Either throw or handle properly |
| 299 | Bug - returns null | Should return `res` |

### 4.5 UpdateJobStatus.java (Scheduled Task)

| Line | Issue | Recommendation |
|------|-------|----------------|
| 26 | System.out.println | Use SLF4J logger |
| 23 | Fixed 10s interval | Make configurable via properties |
| - | No error handling | Wrap in try-catch to prevent scheduler death |

### 4.6 WebConfig.java

| Line | Issue | Recommendation |
|------|-------|----------------|
| 11-19 | First CORS config | Keep this one |
| 21-31 | Duplicate CORS bean | Remove entirely |
| 13-18 | Overly permissive | Restrict allowed origins for security |

### 4.7 ApiResponse.java

| Line | Issue | Recommendation |
|------|-------|----------------|
| - | No Lombok | Add @Data or @Getter |
| 23-25 | Timestamp generated on call | Store at construction time |
| - | Missing generic data field | Add `private T data` for payload |

---

## 5. Recommended Refactoring Tasks

### Priority 1: Critical (Security/Reliability)

- [ ] **Task 1.1:** Add input validation with `@Valid` and Bean Validation annotations
- [ ] **Task 1.2:** Fix null return patterns - throw proper exceptions
- [ ] **Task 1.3:** Add `@Transactional` to service methods
- [ ] **Task 1.4:** Fix thread-safety issue in RestTemplateService
- [ ] **Task 1.5:** Add proper JPA query for `getJobByJid()`

### Priority 2: High (Maintainability)

- [ ] **Task 2.1:** Create enums for status fields (ClusterStatus, JobStatus)
- [ ] **Task 2.2:** Implement GlobalExceptionHandler
- [ ] **Task 2.3:** Refactor to constructor injection
- [ ] **Task 2.4:** Fix duplicate Optional.findById() calls
- [ ] **Task 2.5:** Remove duplicate CORS configuration

### Priority 3: Medium (Code Quality)

- [ ] **Task 3.1:** Standardize REST API endpoints (RESTful naming)
- [ ] **Task 3.2:** Add service interfaces
- [ ] **Task 3.3:** Introduce DTO-Entity mappers
- [ ] **Task 3.4:** Replace System.out with SLF4J
- [ ] **Task 3.5:** Add API versioning

### Priority 4: Low (Nice to Have)

- [ ] **Task 4.1:** Add Spring Actuator for health checks
- [ ] **Task 4.2:** Externalize hardcoded timeouts to configuration
- [ ] **Task 4.3:** Add OpenAPI 3.0 documentation (migrate from Swagger 2)
- [ ] **Task 4.4:** Add request/response logging interceptor
- [ ] **Task 4.5:** Increase test coverage (current tests are basic)

---

## 6. Best Practices Not Followed

### 6.1 Logging

| Issue | Location | Fix |
|-------|----------|-----|
| String concatenation | Throughout | Use parameterized logging: `log.info("id={}", id)` |
| printStackTrace() | Multiple services | Use `log.error("msg", exception)` |
| System.out.println | UpdateJobStatus.java | Use SLF4J logger |

### 6.2 Optional Handling

```java
// BAD - current pattern
if (repo.findById(id).isPresent()) {
    return repo.findById(id).get();
}
return null;

// GOOD
return repo.findById(id)
    .orElseThrow(() -> new EntityNotFoundException("Not found: " + id));

// Or if null is acceptable
return repo.findById(id).orElse(null);
```

### 6.3 URL Building

```java
// BAD - string concatenation
String url = BASE_URL + "/jars/" + jarName + "/run";

// GOOD - UriComponentsBuilder
String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
    .pathSegment("jars", jarName, "run")
    .build()
    .toUriString();
```

### 6.4 HTTP Status Code Checking

```java
// BAD - string comparison
if (StringUtils.startsWithIgnoreCase(resp.getStatusCode().toString(), "2"))

// GOOD - built-in method
if (resp.getStatusCode().is2xxSuccessful())
```

### 6.5 Resource Cleanup

```java
// Missing try-with-resources for streams/files
// Add proper cleanup in FileSystemStorageService
```

---

## Appendix A: Proposed Package Structure

```
com.yen.FlinkRestService/
├── FlinkRestApplication.java
├── config/
│   ├── WebConfig.java
│   ├── RestTemplateConfig.java (NEW)
│   └── SwaggerConfig.java (NEW)
├── controller/
│   ├── ClusterController.java
│   ├── JobController.java
│   └── ...
├── service/
│   ├── ClusterService.java (interface)
│   ├── impl/
│   │   └── ClusterServiceImpl.java
│   └── ...
├── repository/
│   └── ...
├── model/
│   ├── entity/
│   │   ├── Cluster.java
│   │   └── Job.java
│   ├── dto/
│   │   ├── request/
│   │   └── response/
│   └── enums/
│       ├── ClusterStatus.java (NEW)
│       └── JobStatus.java (NEW)
├── mapper/
│   └── ClusterMapper.java (NEW)
├── exception/
│   ├── GlobalExceptionHandler.java (NEW)
│   ├── EntityNotFoundException.java (NEW)
│   └── ...
└── util/
    └── ...
```

---

## Appendix B: Estimated Refactoring Effort

| Priority | Tasks | Estimated LOC Changed |
|----------|-------|----------------------|
| Critical | 5 | ~200-300 |
| High | 5 | ~300-400 |
| Medium | 5 | ~400-500 |
| Low | 5 | ~200-300 |
| **Total** | **20** | **~1100-1500** |

---

## Appendix C: Test Coverage Recommendations

Current test files focus only on service layer mocking. Recommendations:

1. **Add Controller Tests** - MockMvc tests for API endpoints
2. **Add Integration Tests** - @SpringBootTest with TestContainers for MySQL
3. **Add Repository Tests** - @DataJpaTest for custom queries
4. **Increase Service Test Coverage** - Cover edge cases and error scenarios

---

*End of Review Document*
