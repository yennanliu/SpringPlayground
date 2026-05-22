package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.SqlJobRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void testSubmitSQLJob() {
        // Mock the response for the first request to create a session
        String sessionHandle = "session123";
        String createSessionResponse = "{\"sessionHandle\":\"" + sessionHandle + "\"}";
        ResponseEntity<String> createSessionResponseEntity = new ResponseEntity<>(createSessionResponse, HttpStatus.OK);

        // Mock the response for the second request to submit the SQL job
        String operationHandle = "operation456";
        String submitJobResponse = "{\"operationHandle\":\"" + operationHandle + "\"}";
        ResponseEntity<String> submitJobResponseEntity = new ResponseEntity<>(submitJobResponse, HttpStatus.OK);

        // Return different responses for sequential calls
        when(restTemplateService.sendPostRequest(anyString(), anyString(), any(MediaType.class)))
                .thenReturn(createSessionResponseEntity)
                .thenReturn(submitJobResponseEntity);

        // Prepare the SQL job submit DTO
        SqlJobSubmitDto sqlJobSubmitDto = new SqlJobSubmitDto();
        sqlJobSubmitDto.setStatement("SELECT 1");

        // Call the method under test
        String result = sqlJobService.submitSQLJob(sqlJobSubmitDto);

        // Method now returns the operationHandle, not the statement URL
        assertEquals(operationHandle, result);

        // Verify that sendPostRequest was called twice
        verify(restTemplateService, times(2)).sendPostRequest(anyString(), anyString(), any(MediaType.class));
    }

    @Test
    void testSubmitSQLJob_WithStatement() {
        String sessionHandle = "session-abc";
        String createSessionResponse = "{\"sessionHandle\":\"" + sessionHandle + "\"}";
        ResponseEntity<String> sessionResponse = new ResponseEntity<>(createSessionResponse, HttpStatus.OK);

        String operationHandle = "op-123";
        String submitResponse = "{\"operationHandle\":\"" + operationHandle + "\"}";
        ResponseEntity<String> opResponse = new ResponseEntity<>(submitResponse, HttpStatus.OK);

        when(restTemplateService.sendPostRequest(anyString(), anyString(), any(MediaType.class)))
                .thenReturn(sessionResponse)
                .thenReturn(opResponse);

        SqlJobSubmitDto dto = new SqlJobSubmitDto();
        dto.setStatement("SELECT * FROM users");

        String result = sqlJobService.submitSQLJob(dto);

        assertEquals(operationHandle, result);
        verify(restTemplateService, times(2)).sendPostRequest(anyString(), anyString(), any(MediaType.class));
    }
}
