package com.yen.FlinkRestService.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.FlinkRestService.Service.SqlJobService;
import com.yen.FlinkRestService.exception.ExternalServiceException;
import com.yen.FlinkRestService.exception.GlobalExceptionHandler;
import com.yen.FlinkRestService.model.SqlJob;
import com.yen.FlinkRestService.model.dto.job.SqlJobSubmitDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SqlJobControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SqlJobService sqlJobService;

    @InjectMocks
    private SqlJobController sqlJobController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sqlJobController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    private SqlJob makeSqlJob(int id, String statement, String sessionHandle, String operationHandle) {
        SqlJob job = new SqlJob();
        job.setId(id);
        job.setStatement(statement);
        job.setSessionHandle(sessionHandle);
        job.setOperationHandle(operationHandle);
        job.setStatus("RUNNING");
        job.setCreatedAt(LocalDateTime.now());
        return job;
    }

    @Test
    void testListSqlJobs() throws Exception {
        List<SqlJob> jobs = Arrays.asList(
                makeSqlJob(1, "SELECT 1", "sess-1", "op-1"),
                makeSqlJob(2, "SELECT 2", "sess-2", "op-2")
        );
        when(sqlJobService.getAll()).thenReturn(jobs);

        mockMvc.perform(get("/sql_job"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].statement", is("SELECT 1")));

        verify(sqlJobService, times(1)).getAll();
    }

    @Test
    void testSubmitSqlJob() throws Exception {
        SqlJobSubmitDto dto = new SqlJobSubmitDto("SELECT * FROM orders");
        SqlJob returned = makeSqlJob(1, "SELECT * FROM orders", "sess-abc", "op-xyz");

        when(sqlJobService.submitSQLJob(any(SqlJobSubmitDto.class))).thenReturn(returned);

        mockMvc.perform(post("/sql_job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("RUNNING")))
                .andExpect(jsonPath("$.operationHandle", is("op-xyz")));

        verify(sqlJobService, times(1)).submitSQLJob(any(SqlJobSubmitDto.class));
    }

    @Test
    void testSubmitSqlJob_ValidationError() throws Exception {
        SqlJobSubmitDto dto = new SqlJobSubmitDto("");

        mockMvc.perform(post("/sql_job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitSqlJob_ExternalServiceError() throws Exception {
        SqlJobSubmitDto dto = new SqlJobSubmitDto("SELECT * FROM orders");

        when(sqlJobService.submitSQLJob(any(SqlJobSubmitDto.class)))
                .thenThrow(new ExternalServiceException("Flink SQL Gateway", "Connection refused"));

        mockMvc.perform(post("/sql_job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success", is(false)));

        verify(sqlJobService, times(1)).submitSQLJob(any(SqlJobSubmitDto.class));
    }

    @Test
    void testAddJobLegacy() throws Exception {
        SqlJobSubmitDto dto = new SqlJobSubmitDto("SELECT COUNT(*) FROM users");
        SqlJob returned = makeSqlJob(2, "SELECT COUNT(*) FROM users", "sess-leg", "op-leg");

        when(sqlJobService.submitSQLJob(any(SqlJobSubmitDto.class))).thenReturn(returned);

        mockMvc.perform(post("/sql_job/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)));

        verify(sqlJobService, times(1)).submitSQLJob(any(SqlJobSubmitDto.class));
    }
}
