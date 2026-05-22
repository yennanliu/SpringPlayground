package com.yen.FlinkRestService.Service;

import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;

import com.yen.FlinkRestService.Repository.SqlJobRepository;
import com.yen.FlinkRestService.exception.ExternalServiceException;
import com.yen.FlinkRestService.model.SqlJob;
import com.yen.FlinkRestService.model.dto.job.SqlJobSubmitDto;
import com.yen.FlinkRestService.model.response.SqlJobSubmitResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqlJobService {

    @Value("${flink.sql_gateway.base_url}")
    private String sqlGatewayBaseUrl;

    private final SqlJobRepository sqlJobRepository;
    private final RestTemplateService restTemplateService;

    private static final Gson GSON = new Gson();

    @Transactional(readOnly = true)
    public List<SqlJob> getAll() {
        return sqlJobRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SqlJob getById(Integer id) {
        return sqlJobRepository.findById(id)
                .orElseThrow(() -> new com.yen.FlinkRestService.exception.EntityNotFoundException("SqlJob", id));
    }

    @Transactional
    public SqlJob submitSQLJob(SqlJobSubmitDto sqlJobSubmitDto) {
        // Persist immediately so the job is visible even if gateway call fails partway
        SqlJob sqlJob = new SqlJob();
        sqlJob.setStatement(sqlJobSubmitDto.getStatement());
        sqlJob.setStatus("SUBMITTING");
        sqlJob.setCreatedAt(LocalDateTime.now());
        sqlJob = sqlJobRepository.save(sqlJob);
        log.info("SqlJob record created, id={}", sqlJob.getId());

        try {
            // ── Step 1: create a SQL Gateway session ────────────────────────
            String sessionUrl = UriComponentsBuilder.fromHttpUrl(sqlGatewayBaseUrl)
                    .pathSegment("v1", "sessions")
                    .build()
                    .toUriString();
            log.info("Creating SQL session at url={}", sessionUrl);

            ResponseEntity<String> sessionResponse = restTemplateService.sendPostRequest(
                    sessionUrl, "", MediaType.APPLICATION_JSON);
            log.info("Session response={}", sessionResponse.getBody());

            SqlJobSubmitResponse sessionResult = JSON.parseObject(
                    sessionResponse.getBody(), SqlJobSubmitResponse.class);
            if (sessionResult == null || sessionResult.getSessionHandle() == null) {
                throw new ExternalServiceException("SqlGateway", "Invalid session creation response");
            }
            String sessionHandle = sessionResult.getSessionHandle();
            sqlJob.setSessionHandle(sessionHandle);
            log.info("Session handle={}", sessionHandle);

            // ── Step 2: submit the SQL statement ────────────────────────────
            String statementUrl = UriComponentsBuilder.fromHttpUrl(sqlGatewayBaseUrl)
                    .pathSegment("v1", "sessions", sessionHandle, "statements")
                    .build()
                    .toUriString();
            log.info("Submitting SQL to url={}", statementUrl);

            // Use Gson for safe JSON serialization — handles quotes, newlines, etc.
            String sqlPayload = GSON.toJson(
                    java.util.Collections.singletonMap("statement", sqlJobSubmitDto.getStatement()));
            log.info("SQL payload={}", sqlPayload);

            ResponseEntity<String> statementResponse = restTemplateService.sendPostRequest(
                    statementUrl, sqlPayload, MediaType.APPLICATION_JSON);
            SqlJobSubmitResponse statementResult = JSON.parseObject(
                    statementResponse.getBody(), SqlJobSubmitResponse.class);
            if (statementResult == null || statementResult.getOperationHandle() == null) {
                throw new ExternalServiceException("SqlGateway", "Invalid statement submission response");
            }

            String operationHandle = statementResult.getOperationHandle();
            sqlJob.setOperationHandle(operationHandle);
            sqlJob.setStatus("RUNNING");
            sqlJob = sqlJobRepository.save(sqlJob);
            log.info("SQL job submitted, id={}, sessionHandle={}, operationHandle={}",
                    sqlJob.getId(), sessionHandle, operationHandle);

            return sqlJob;

        } catch (Exception e) {
            sqlJob.setStatus("FAILED");
            sqlJobRepository.save(sqlJob);
            log.error("SQL job submission failed, id={}: {}", sqlJob.getId(), e.getMessage());
            throw e;
        }
    }
}
