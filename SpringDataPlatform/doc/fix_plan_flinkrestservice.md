# FlinkRestService — Code Review Fix Plan

Source: `/SpringDataPlatform/backend/DataPlatform/FlinkRestService`
Review date: 2026-05-22
Severity legend: **P0** crash / data loss · **P1** wrong behavior · **P2** resource / reliability · **P3** config / maintainability

---

## Fix 1 — `JobService.updateAllJobs()`: NPE when Flink returns null body (P0)

**File:** `Service/JobService.java` · **Line:** 121–123

### Problem
`parseJobOverviewResponse()` calls `GSON.fromJson(responseBody, ...)`, which returns `null` when
`responseBody` is `null`. The caller immediately dereferences `jobOverviewResponse.getJobs()` without
a null check. `NullPointerException` is not a subtype of `ExternalServiceException`, so it escapes
the catch block and propagates to the scheduler — breaking every 10-second job-status update cycle
silently while Flink is unreachable.

### Fix

```java
// Before
JobOverviewResponse jobOverviewResponse = parseJobOverviewResponse(responseEntity.getBody());
List<JobOverview> jobs = jobOverviewResponse.getJobs();

// After
JobOverviewResponse jobOverviewResponse = parseJobOverviewResponse(responseEntity.getBody());
if (jobOverviewResponse == null || jobOverviewResponse.getJobs() == null) {
    log.warn("Empty or unparseable response from Flink job overview");
    return;
}
List<JobOverview> jobs = jobOverviewResponse.getJobs();
```

---

## Fix 2 — `JobService.addJob()`: NPE on null GSON parse result (P0)

**File:** `Service/JobService.java` · **Line:** 80–81

### Problem
`GSON.fromJson(responseEntity.getBody(), JobSubmitResponse.class)` returns `null` when the
response body is null or non-JSON. The immediately following `.getJobid()` call throws NPE,
aborting the transaction with no record saved.

### Fix

```java
// After: null-check GSON result before use
JobSubmitResponse jobSubmitResponse = GSON.fromJson(responseEntity.getBody(), JobSubmitResponse.class);
if (jobSubmitResponse == null || jobSubmitResponse.getJobid() == null) {
    throw new ExternalServiceException("Flink", "Invalid or empty response from job submit endpoint");
}
log.info("Job submitted successfully, flinkJobId={}", jobSubmitResponse.getJobid());
```

---

## Fix 3 — `SqlJobService.submitSQLJob()`: NPE on null session response + wrong return value (P0)

**File:** `Service/SqlJobService.java` · **Lines:** 42, 57, 61

### Problem — Part A: NPE
`JSON.parseObject(sessionResponse.getBody(), ...)` returns `null` when the body is null.
`sessionResult.getSessionHandle()` then throws NPE. The same pattern repeats for `statementResult`
on line 57. Additionally, if `sessionHandle` is non-null but the value is null at runtime, Spring's
`UriComponentsBuilder.pathSegment(...)` silently serializes it as the string `"null"`, sending a
malformed URL to the gateway.

### Problem — Part B: Wrong return value
The method returns `statementUrl` (a URL string) rather than the `operationHandle` needed to poll
`/v1/sessions/{s}/statements/{op}/result`. The `operationHandle` is logged but discarded.

### Fix

```java
public String submitSQLJob(SqlJobSubmitDto sqlJobSubmitDto) {
    // --- session ---
    ResponseEntity<String> sessionResponse = restTemplateService.sendPostRequest(
            sessionUrl, "", MediaType.APPLICATION_JSON);

    SqlJobSubmitResponse sessionResult = JSON.parseObject(
            sessionResponse.getBody(), SqlJobSubmitResponse.class);
    if (sessionResult == null || sessionResult.getSessionHandle() == null) {
        throw new ExternalServiceException("SqlGateway", "Invalid session creation response");
    }
    String sessionHandle = sessionResult.getSessionHandle();

    // --- statement ---
    ResponseEntity<String> statementResponse = restTemplateService.sendPostRequest(
            statementUrl, sqlPayload, MediaType.APPLICATION_JSON);

    SqlJobSubmitResponse statementResult = JSON.parseObject(
            statementResponse.getBody(), SqlJobSubmitResponse.class);
    if (statementResult == null || statementResult.getOperationHandle() == null) {
        throw new ExternalServiceException("SqlGateway", "Invalid statement submission response");
    }

    String operationHandle = statementResult.getOperationHandle();
    log.info("SQL job submitted, sessionHandle={}, operationHandle={}", sessionHandle, operationHandle);

    return operationHandle;  // return the handle callers need to poll results
}
```

Update the return type in the interface/controller accordingly.

---

## Fix 4 — `JarService.addJobJar()`: `@Transactional` rolls back all FAILED-status saves (P1)

**File:** `Service/JarService.java` · **Lines:** 89, 100, 116, 122

### Problem
`addJobJar()` is annotated `@Transactional`. When the method throws `ExternalServiceException`
(unchecked), Spring rolls back the entire transaction — including the `jobJarRepository.save(jobJar)`
calls that set `STATUS_FAILED`. No failure record is ever committed. Operators monitoring the DB
for failed uploads will never see them.

### Fix

Split the method: persist the initial record outside the upload transaction, then update it inside
a separate smaller transaction (or use `REQUIRES_NEW`):

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void persistFailedJar(JobJar jobJar) {
    jobJarRepository.save(jobJar);
}

@Transactional
public JobJar addJobJar(UploadJarDto uploadJarDto) {
    JobJar jobJar = new JobJar();
    jobJar.setFileName(uploadJarDto.getJarFile());
    jobJar.setUploadTime(new Date());
    jobJar.setStatus(STATUS_FAILED);       // start as FAILED, flip to UPLOADED on success
    jobJarRepository.save(jobJar);         // committed immediately; visible to monitors

    try {
        // ... HTTP upload ...
        jobJar.setStatus(STATUS_UPLOADED);
        jobJar.setSavedJarName(JarUtil.getJarNameFromResponse(jarUploadResponse));
        return jobJarRepository.save(jobJar);
    } catch (Exception e) {
        // jobJar already persisted as STATUS_FAILED; no further save needed
        throw new ExternalServiceException("Flink", e.getMessage(), e);
    }
}
```

Alternatively, save the initial record in a `@Transactional(propagation = REQUIRES_NEW)` method
before entering the upload logic so the initial row survives a rollback.

---

## Fix 5 — `FileSystemStorageService.store()`: null filename causes NPE (P1)

**File:** `Service/FileSystemStorageService.java` · **Line:** 52

### Problem
`MultipartFile.getOriginalFilename()` returns `null` when the HTTP part has no
`Content-Disposition filename` attribute. `Paths.get(null)` throws `NullPointerException`, which
is not caught by `catch (IOException)` and propagates as a raw 500.

### Fix

```java
@Override
public void store(MultipartFile file) {
    try {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new StorageException("Uploaded file has no filename.");
        }
        // Sanitize: strip directory components supplied by the client
        String safeFilename = Paths.get(originalFilename).getFileName().toString();

        Path destinationFile = this.rootLocation.resolve(safeFilename)
                .normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside current directory.");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
    } catch (IOException e) {
        throw new StorageException("Failed to store file.", e);
    }
}
```

Using `Paths.get(originalFilename).getFileName()` also removes any leading path components
(e.g. `../../etc/passwd` → `passwd`), hardening the path-traversal guard.

---

## Fix 6 — `JobService.cancelJob()`: Wrong Flink REST endpoint + no DB update (P1)

**File:** `Service/JobService.java` · **Line:** 168

### Problem
`POST /jobs/{id}/stop` is Flink's graceful savepoint-based stop endpoint. It blocks waiting for a
savepoint and returns 4xx if no savepoint directory is configured. The correct cancel endpoint is
`PATCH /jobs/{id}` with body `{"target-state":"canceled"}`. Additionally, `cancelJob()` never writes
any status change to the local database, so job records stay `RUNNING` indefinitely.

### Fix

```java
@Transactional
public void cancelJob(String jobId) {
    log.info("Cancelling job with flinkJobId={}", jobId);

    String url = UriComponentsBuilder.fromHttpUrl(flinkBaseUrl)
            .pathSegment("jobs", jobId)
            .build()
            .toUriString();

    // Flink cancel API: PATCH /jobs/{jobId}  body: {"target-state":"canceled"}
    restTemplateService.sendPatchRequest(url, "{\"target-state\":\"canceled\"}", MediaType.APPLICATION_JSON);

    // Update local record
    jobRepository.findByJobId(jobId).ifPresent(job -> {
        job.setState("CANCELED");
        jobRepository.save(job);
        log.info("Job state updated to CANCELED in DB, flinkJobId={}", jobId);
    });
}
```

Add `sendPatchRequest` to `RestTemplateService` (mirrors `sendPostRequest`, uses `HttpMethod.PATCH`).

---

## Fix 7 — `JarService`: Unmanaged `RestTemplate` with no timeouts (P2)

**File:** `Service/JarService.java` · **Line:** 42

### Problem
`private final RestTemplate restTemplate = new RestTemplate()` creates an instance outside the
Spring container with no connect/read timeouts (OS-default, effectively infinite). If Flink is slow
or unreachable, the JAR upload call blocks the thread indefinitely while holding a `@Transactional`
DB connection, exhausting both the thread pool and the connection pool under concurrent load.
`RestTemplateService` (which configures 20 s connect / 500 s read) is available as a Spring bean
but is not used here.

### Fix

Remove the `RestTemplate` field and inject `RestTemplateService` instead:

```java
@Service
@RequiredArgsConstructor
public class JarService {

    private final JobJarRepository jobJarRepository;
    private final RestTemplateService restTemplateService;  // inject instead of new RestTemplate()

    // Replace direct restTemplate.exchange(...) calls with restTemplateService equivalents.
    // For multipart upload, add a sendMultipartPostRequest method to RestTemplateService:
}
```

Add to `RestTemplateService`:

```java
public ResponseEntity<String> sendMultipartPostRequest(String url, MultiValueMap<String, Object> body) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    } catch (ResourceAccessException e) {
        throw new ExternalServiceException("RestTemplate", "Cannot connect to " + url, e);
    } catch (RestClientException e) {
        throw new ExternalServiceException("RestTemplate", "Request failed: " + e.getMessage(), e);
    }
}
```

---

## Fix 8 — `ZeppelinService.deleteNote()`: External API call inside `@Transactional` (P2)

**File:** `Service/ZeppelinService.java` · **Line:** 85

### Problem
`deleteNote()` is annotated `@Transactional`, so Spring acquires a DB connection before entering
the method. `zeppelinClient.deleteNote(noteId)` on line 85 is an external HTTP call that can take
seconds (or up to the 500 s read timeout). The DB connection is held open for the entire duration,
reducing available pool connections under load.

### Fix

Perform the external call before opening the transaction:

```java
// No @Transactional on this method — external call first
public void deleteNote(String noteId) {
    validateNotNull(noteId, "noteId");
    try {
        zeppelinClient.deleteNote(noteId);
        log.info("Deleted Zeppelin notebook, noteId={}", noteId);
    } catch (Exception e) {
        log.error("Failed to delete Zeppelin notebook noteId={}: {}", noteId, e.getMessage(), e);
        throw new ExternalServiceException("Zeppelin", "Failed to delete notebook: " + e.getMessage(), e);
    }
    deleteNoteFromDb(noteId);  // DB work in its own transaction
}

@Transactional
void deleteNoteFromDb(String noteId) {
    notebookRepository.findByZeppelinNoteId(noteId).ifPresent(notebook -> {
        notebookRepository.delete(notebook);
        log.info("Deleted notebook from database, id={}", notebook.getId());
    });
}
```

Also add a derived query to `NotebookRepository` to avoid the full-table-scan `findAll()`:

```java
Optional<Notebook> findByZeppelinNoteId(String zeppelinNoteId);
```

---

## Fix 9 — `StorageProperties`: `@ConfigurationProperties` commented out (P3)

**File:** `Config/StorageProperties.java` · **Line:** 7

### Problem
`@ConfigurationProperties("storage")` is commented out. The `location` field is permanently
hardcoded to `"upload-dir"` and cannot be overridden via `application.properties`. Any operator
who sets `storage.location=/data/jars` to redirect uploads will have the setting silently ignored.

### Fix

Re-enable the annotation:

```java
@Configuration
@ConfigurationProperties("storage")   // re-enable external binding
public class StorageProperties {
    private String location = "upload-dir";  // default remains; now overridable
    // ...
}
```

Document in `application.properties`:

```properties
# Upload directory for JAR files (default: upload-dir)
# storage.location=upload-dir
```

---

## Summary table

| # | File | Line | Severity | Fix type |
|---|------|------|----------|----------|
| 1 | `JobService.java` | 123 | P0 | Add null check after `parseJobOverviewResponse()` |
| 2 | `JobService.java` | 81 | P0 | Add null check after GSON parse in `addJob()` |
| 3 | `SqlJobService.java` | 42, 57, 61 | P0 | Null-check session/statement results; return `operationHandle` not URL |
| 4 | `JarService.java` | 89 | P1 | Persist initial FAILED record before upload; flip to UPLOADED on success |
| 5 | `FileSystemStorageService.java` | 52 | P1 | Validate + sanitize filename before `Paths.get()` |
| 6 | `JobService.java` | 168 | P1 | Use `PATCH /jobs/{id}` with `{"target-state":"canceled"}`; update DB after cancel |
| 7 | `JarService.java` | 42 | P2 | Remove `new RestTemplate()`; inject `RestTemplateService` |
| 8 | `ZeppelinService.java` | 85 | P2 | Move external call outside `@Transactional`; add `findByZeppelinNoteId` derived query |
| 9 | `StorageProperties.java` | 7 | P3 | Re-enable `@ConfigurationProperties("storage")` |
