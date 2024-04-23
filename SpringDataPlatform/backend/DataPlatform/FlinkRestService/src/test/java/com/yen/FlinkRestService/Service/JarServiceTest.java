package com.yen.FlinkRestService.Service;

import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;


import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.jar.UploadJarDto;
import com.yen.FlinkRestService.model.response.JarUploadResponse;
import com.yen.FlinkRestService.util.JarUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JarServiceTest {

    @Mock
    private JobJarRepository jobJarRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JarService jarService;

//    @Value("${flink.base_url}")
//    private String BASE_URL; // "http://localhost:8081/";


    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    // TODO: fix below
//    @Test
//    void testAddJobJar() {
//
//        UploadJarDto uploadJarDto = new UploadJarDto();
//        uploadJarDto.setJarFile(String.valueOf(new File("path/to/jarFile.jar")));
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"status\":\"success\"}", HttpStatus.OK);
//        // mock
//        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(responseEntity);
//
//        String url = "base_url" + "/jars/upload";
//        jarService.addJobJar(uploadJarDto);
//
//        verify(jobJarRepository, times(1)).save(any(JobJar.class));
//    }

    @Test
    void testGetJarByJobId() {

        JobJar jobJar = new JobJar();
        jobJar.setId(1);
        jobJar.setFileName("test.jar");
        jobJar.setStatus("success");
        jobJar.setUploadTime(new Date());
        jobJar.setSavedJarName("saved.jar");

        // mock
        when(jobJarRepository.findById(1)).thenReturn(java.util.Optional.of(jobJar));

        JobJar result = jarService.getJarByJobId(1);

        assertNotNull(result);
        assertEquals(jobJar.getId(), result.getId());
        assertEquals(jobJar.getFileName(), result.getFileName());
        assertEquals(jobJar.getStatus(), result.getStatus());
        assertEquals(jobJar.getUploadTime(), result.getUploadTime());
        assertEquals(jobJar.getSavedJarName(), result.getSavedJarName());
    }

    @Test
    public void testGetBytes(){

        String str = "PANKAJ";
        byte[] byteArr = str.getBytes();
        // print the byte[] elements
        System.out.println("String to byte array: " + Arrays.toString(byteArr));
    }

    // https://mvnrepository.com/artifact/org.apache.flink/flink-clients/1.17.2
    // https://blog.51cto.com/u_16175517/8075303
    @Test
    public void testFlinkClient() throws Exception {

        String restURL = "localhost";

        Configuration config = new Configuration();
        config.setString("rest.address", restURL);
        //new org.apache.flink.configuration();
        RestClusterClient client = new RestClusterClient(config, restURL);

        // string -> byte[]
        // byte[] byteArrray = inputString.getBytes("IBM01140");
        // https://www.baeldung.com/java-string-to-byte-array
        String flinkJobId = "36f62fd9d9cff27eb51b6325a1d0b86d";
        byte[] byteFlinkJobId = flinkJobId.getBytes(); //flinkJobId.getBytes(StandardCharsets.UTF_8); //flinkJobId.getBytes(); //Base64.decodeBase64(flinkJobId); //flinkJobId.getBytes();
        System.out.println("String to byte array: " + Arrays.toString(byteFlinkJobId));
        System.out.println("client = " + client.toString());
        System.out.println("ClusterId = " + client.getClusterId());
        //System.out.println("JobStatus = " + client.getJobStatus(jobId));
        JobGraph jobGraph = new JobGraph("Flink Streaming Job");
        client.submitJob(jobGraph);
    }

}