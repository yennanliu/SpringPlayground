package com.yen.FlinkRestService.Service;

import com.google.gson.Gson;
import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.Service.JobService;
import com.yen.FlinkRestService.Service.RestTemplateService;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.job.JobSubmitDto;
import com.yen.FlinkRestService.model.response.JobSubmitResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class JobServiceTest {

    @InjectMocks
    private JobService jobService;

    @Mock
    private JobJarRepository jobJarRepository;

    @Mock
    private RestTemplateService restTemplateService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }


//    @Test
//    public void testAddJob() {
//
//        // Arrange
//        MockitoAnnotations.openMocks(this);
//        JobSubmitDto jobSubmitDto = new JobSubmitDto();
//        jobSubmitDto.setJarId(1);
//
//        // Mock the JobJarRepository to return a JobJar
//        JobJar jobJar = new JobJar();
//        jobJar.setId(1);
//        jobJar.setSavedJarName("MyProgram.jar");
//        when(jobJarRepository.findById(1)).thenReturn(Optional.of(jobJar));
//
//        // Mock the RestTemplateService to return a response entity
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("Success", HttpStatus.CREATED);
//        when(restTemplateService.sendPostRequest(anyString(), anyString(), any())).thenReturn(responseEntity);
//
//        // Mock the gson object
//        Gson gson = Mockito.mock(Gson.class);
//
//        // Sample response body
//        String responseBody = "{\"jobid\":\"4972cd1a7ff7ce366c03fb6049e73119\"}";
//
//        // Mock the gson.fromJson method to return a predefined JobSubmitResponse object
//        JobSubmitResponse expectedResponse = new JobSubmitResponse();
//        expectedResponse.setJobid("4972cd1a7ff7ce366c03fb6049e73119");
//
//        // Act
//        jobService.addJob(jobSubmitDto);
//
//        // Assert
//        // Add your assertions here to verify the behavior of the method
//    }

}
