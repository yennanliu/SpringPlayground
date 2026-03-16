package com.yen.FlinkRestService.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.FlinkRestService.Service.SqlJobService;
import com.yen.FlinkRestService.exception.ExternalServiceException;
import com.yen.FlinkRestService.exception.GlobalExceptionHandler;
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

    @Test
    void testSubmitSqlJob() throws Exception {
        SqlJobSubmitDto dto = new SqlJobSubmitDto("SELECT * FROM orders");

        when(sqlJobService.submitSQLJob(any(SqlJobSubmitDto.class)))
                .thenReturn("job-handle-12345");

        mockMvc.perform(post("/sql_job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("SQL job has been submitted")))
                .andExpect(jsonPath("$.message", containsString("job-handle-12345")));

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
    void testSubmitSqlJob_NullStatement() throws Exception {
        SqlJobSubmitDto dto = new SqlJobSubmitDto(null);

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

        when(sqlJobService.submitSQLJob(any(SqlJobSubmitDto.class)))
                .thenReturn("legacy-job-handle");

        mockMvc.perform(post("/sql_job/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("SQL job has been submitted")));

        verify(sqlJobService, times(1)).submitSQLJob(any(SqlJobSubmitDto.class));
    }
}
