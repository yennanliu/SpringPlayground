package com.yen.FlinkRestService.Service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.exception.ExternalServiceException;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.jar.UploadJarDto;
import com.yen.FlinkRestService.model.response.JarUploadResponse;
import com.yen.FlinkRestService.util.JarUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JarService {

    private final JobJarRepository jobJarRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .create();

    private static final String STATUS_UPLOADED = "uploaded";
    private static final String STATUS_FAILED = "failed";

    @Value("${flink.base_url}")
    private String flinkBaseUrl;

    @Transactional
    public JobJar addJobJar(UploadJarDto uploadJarDto) {
        log.info("Uploading JAR file: {}", uploadJarDto.getJarFile());

        // Validate file exists
        File jarFile = new File(uploadJarDto.getJarFile());
        if (!jarFile.exists()) {
            throw new IllegalArgumentException("JAR file does not exist: " + uploadJarDto.getJarFile());
        }

        String url = flinkBaseUrl + "/jars/upload";
        log.info("Uploading JAR to Flink at url={}", url);

        JobJar jobJar = new JobJar();
        jobJar.setFileName(uploadJarDto.getJarFile());
        jobJar.setUploadTime(new Date());

        try {
            // Prepare multipart request
            MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
            bodyMap.add("jarfile", new FileSystemResource(jarFile));

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

            // Upload to Flink
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity, String.class);

            log.info("JAR upload response: status={}, body={}",
                    responseEntity.getStatusCode(), responseEntity.getBody());

            // Check response status
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                jobJar.setStatus(STATUS_FAILED);
                jobJarRepository.save(jobJar);
                throw new ExternalServiceException("Flink",
                        "JAR upload failed with status: " + responseEntity.getStatusCode());
            }

            // Parse response
            JarUploadResponse jarUploadResponse = GSON.fromJson(
                    responseEntity.getBody(), JarUploadResponse.class);

            if (jarUploadResponse == null || jarUploadResponse.getFilename() == null) {
                jobJar.setStatus(STATUS_FAILED);
                jobJarRepository.save(jobJar);
                throw new ExternalServiceException("Flink", "Invalid response from Flink JAR upload");
            }

            // Success - save to DB
            jobJar.setStatus(STATUS_UPLOADED);
            jobJar.setSavedJarName(JarUtil.getJarNameFromResponse(jarUploadResponse));
            JobJar savedJar = jobJarRepository.save(jobJar);

            log.info("JAR uploaded successfully, id={}, savedName={}",
                    savedJar.getId(), savedJar.getSavedJarName());
            return savedJar;

        } catch (ResourceAccessException e) {
            log.error("Cannot connect to Flink cluster at {}: {}", url, e.getMessage());
            jobJar.setStatus(STATUS_FAILED);
            jobJarRepository.save(jobJar);
            throw new ExternalServiceException("Flink", "Cannot connect to Flink cluster: " + e.getMessage(), e);

        } catch (RestClientException e) {
            log.error("JAR upload failed: {}", e.getMessage());
            jobJar.setStatus(STATUS_FAILED);
            jobJarRepository.save(jobJar);
            throw new ExternalServiceException("Flink", "JAR upload failed: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<JobJar> getJars() {
        return jobJarRepository.findAll();
    }

    @Transactional(readOnly = true)
    public JobJar getJarByJobId(Integer jobJarId) {
        return jobJarRepository.findById(jobJarId)
                .orElseThrow(() -> new EntityNotFoundException("JobJar", jobJarId));
    }

    @Transactional
    public void deleteJar(Integer jobJarId) {
        JobJar jobJar = jobJarRepository.findById(jobJarId)
                .orElseThrow(() -> new EntityNotFoundException("JobJar", jobJarId));

        jobJarRepository.delete(jobJar);
        log.info("Deleted JAR record, id={}", jobJarId);
    }
}
