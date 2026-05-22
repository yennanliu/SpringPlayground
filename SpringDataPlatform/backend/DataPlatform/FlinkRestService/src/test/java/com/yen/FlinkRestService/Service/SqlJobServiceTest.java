package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.SqlJobRepository;
import com.yen.FlinkRestService.model.SqlJob;
import com.yen.FlinkRestService.model.dto.job.SqlJobSubmitDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SqlJobServiceTest {

    @Mock
    private RestTemplateService restTemplateService;

    @Mock
    private SqlJobRepository sqlJobRepository;

    private SqlJobService sqlJobService;

    @BeforeEach
    void setUp() {
        sqlJobService = new SqlJobService(sqlJobRepository, restTemplateService);
        ReflectionTestUtils.setField(sqlJobService, "sqlGatewayBaseUrl", "http://localhost:8083");
    }

    private SqlJob savedJob(int id, String status) {
        SqlJob j = new SqlJob();
        j.setId(id);
        j.setStatement("SELECT 1");
        j.setStatus(status);
        j.setCreatedAt(LocalDateTime.now());
        return j;
    }

    @Test
    void testGetAll() {
        List<SqlJob> jobs = Arrays.asList(savedJob(1, "RUNNING"), savedJob(2, "FAILED"));
        when(sqlJobRepository.findAll()).thenReturn(jobs);

        List<SqlJob> result = sqlJobService.getAll();

        assertEquals(2, result.size());
        verify(sqlJobRepository).findAll();
    }

    @Test
    void testGetById_found() {
        SqlJob job = savedJob(1, "RUNNING");
        when(sqlJobRepository.findById(1)).thenReturn(Optional.of(job));

        SqlJob result = sqlJobService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testSubmitSQLJob() {
        SqlJob initialJob = savedJob(1, "SUBMITTING");
        SqlJob finalJob   = savedJob(1, "RUNNING");
        finalJob.setSessionHandle("sess-abc");
        finalJob.setOperationHandle("op-xyz");

        // First save (SUBMITTING), second save (RUNNING after submission)
        when(sqlJobRepository.save(any(SqlJob.class)))
                .thenReturn(initialJob)
                .thenReturn(finalJob);

        String sessionResponse   = "{\"sessionHandle\":\"sess-abc\"}";
        String statementResponse = "{\"operationHandle\":\"op-xyz\"}";

        when(restTemplateService.sendPostRequest(anyString(), anyString(), any(MediaType.class)))
                .thenReturn(new ResponseEntity<>(sessionResponse,   HttpStatus.OK))
                .thenReturn(new ResponseEntity<>(statementResponse, HttpStatus.OK));

        SqlJobSubmitDto dto = new SqlJobSubmitDto("SELECT 1");
        SqlJob result = sqlJobService.submitSQLJob(dto);

        assertNotNull(result);
        assertEquals("RUNNING", result.getStatus());
        assertEquals("op-xyz",  result.getOperationHandle());
        // Saved twice: once as SUBMITTING, once as RUNNING
        verify(sqlJobRepository, times(2)).save(any(SqlJob.class));
        verify(restTemplateService, times(2)).sendPostRequest(anyString(), anyString(), any(MediaType.class));
    }

    @Test
    void testSubmitSQLJob_WithStatement() {
        SqlJob initialJob = savedJob(2, "SUBMITTING");
        SqlJob finalJob   = savedJob(2, "RUNNING");
        finalJob.setSessionHandle("session-abc");
        finalJob.setOperationHandle("op-123");

        when(sqlJobRepository.save(any(SqlJob.class)))
                .thenReturn(initialJob)
                .thenReturn(finalJob);

        when(restTemplateService.sendPostRequest(anyString(), anyString(), any(MediaType.class)))
                .thenReturn(new ResponseEntity<>("{\"sessionHandle\":\"session-abc\"}", HttpStatus.OK))
                .thenReturn(new ResponseEntity<>("{\"operationHandle\":\"op-123\"}", HttpStatus.OK));

        SqlJobSubmitDto dto = new SqlJobSubmitDto("SELECT * FROM users");
        SqlJob result = sqlJobService.submitSQLJob(dto);

        assertNotNull(result);
        assertEquals("op-123", result.getOperationHandle());
    }
}
