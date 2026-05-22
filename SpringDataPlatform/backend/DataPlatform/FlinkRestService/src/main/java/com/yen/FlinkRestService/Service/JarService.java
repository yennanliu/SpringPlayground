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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JarService {

    private final JobJarRepository jobJarRepository;
    private final RestTemplateService restTemplateService;

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .create();

    private static final String STATUS_UPLOADED = "uploaded";
    private static final String STATUS_FAILED = "failed";

    @Value("${flink.base_url}")
    private String flinkBaseUrl;

    // Not @Transactional: the HTTP upload runs outside any DB transaction.
    // The record is persisted as STATUS_FAILED upfront (via JpaRepository's own transaction)
    // so the failure entry survives even when an exception is thrown afterwards.
    public JobJar addJobJar(UploadJarDto uploadJarDto) {
        log.info("Uploading JAR file: {}", uploadJarDto.getJarFile());

        File jarFile = new File(uploadJarDto.getJarFile());
        if (!jarFile.exists()) {
            throw new IllegalArgumentException("JAR file does not exist: " + uploadJarDto.getJarFile());
        }

        // Persist as FAILED immediately — committed by JpaRepository's own @Transactional.
        // On success the record is updated to STATUS_UPLOADED below.
        JobJar jobJar = new JobJar();
        jobJar.setFileName(uploadJarDto.getJarFile());
        jobJar.setUploadTime(new Date());
        jobJar.setStatus(STATUS_FAILED);
        jobJar = jobJarRepository.save(jobJar);

        String url = flinkBaseUrl + "/jars/upload";
        log.info("Uploading JAR to Flink at url={}", url);

        try {
            MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
            bodyMap.add("jarfile", new FileSystemResource(jarFile));

            ResponseEntity<String> responseEntity = restTemplateService.sendMultipartPostRequest(url, bodyMap);

            log.info("JAR upload response: status={}, body={}",
                    responseEntity.getStatusCode(), responseEntity.getBody());

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new ExternalServiceException("Flink",
                        "JAR upload failed with status: " + responseEntity.getStatusCode());
            }

            JarUploadResponse jarUploadResponse = GSON.fromJson(
                    responseEntity.getBody(), JarUploadResponse.class);

            if (jarUploadResponse == null || jarUploadResponse.getFilename() == null) {
                throw new ExternalServiceException("Flink", "Invalid response from Flink JAR upload");
            }

            jobJar.setStatus(STATUS_UPLOADED);
            jobJar.setSavedJarName(JarUtil.getJarNameFromResponse(jarUploadResponse));
            JobJar savedJar = jobJarRepository.save(jobJar);

            log.info("JAR uploaded successfully, id={}, savedName={}",
                    savedJar.getId(), savedJar.getSavedJarName());
            return savedJar;

        } catch (ExternalServiceException e) {
            // FAILED record already committed — just rethrow
            log.error("JAR upload to Flink failed: {}", e.getMessage());
            throw e;
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
