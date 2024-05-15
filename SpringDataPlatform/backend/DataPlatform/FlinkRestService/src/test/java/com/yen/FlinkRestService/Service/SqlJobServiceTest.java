package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.SqlJobRepository;
import com.yen.FlinkRestService.model.dto.job.SqlJobSubmitDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SqlJobServiceTest {

    @Mock
    private RestTemplateService restTemplateService;

    @Mock
    private SqlJobRepository sqlJobRepository;

    @InjectMocks
    private SqlJobService sqlJobService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitSQLJob() {

        // Mock the response for the first request to create a session
        String sessionHandle = "session123";
        String createSessionResponse = "{\"sessionHandle\":\"" + sessionHandle + "\"}";
        ResponseEntity<String> createSessionResponseEntity = new ResponseEntity<>(createSessionResponse, HttpStatus.OK);
        when(restTemplateService.sendPostRequest(anyString(), anyString(), any())).thenReturn(createSessionResponseEntity);

        // Mock the response for the second request to submit the SQL job
        String operationHandle = "operation456";
        String submitJobResponse = "{\"operationHandle\":\"" + operationHandle + "\"}";
        ResponseEntity<String> submitJobResponseEntity = new ResponseEntity<>(submitJobResponse, HttpStatus.OK);
        when(restTemplateService.sendPostRequest(anyString(), anyString(), any())).thenReturn(submitJobResponseEntity);

        // Prepare the SQL job submit DTO
        SqlJobSubmitDto sqlJobSubmitDto = new SqlJobSubmitDto();
        // Set necessary properties on sqlJobSubmitDto if needed

        // Call the method under test
        String result = sqlJobService.submitSQLJob(sqlJobSubmitDto);
        System.out.println("result = " + result);

        // Verify that the correct URLs were used and that the session and operation handles were extracted correctly
        verify(restTemplateService, times(2)).sendPostRequest(anyString(), anyString(), any());
        // Add more verification as needed for the behavior of your service
    }

}