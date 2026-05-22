package com.yen.FlinkRestService.Service;

import com.alibaba.fastjson2.JSON;

import com.yen.FlinkRestService.Repository.SqlJobRepository;
import com.yen.FlinkRestService.exception.ExternalServiceException;
import com.yen.FlinkRestService.model.dto.job.SqlJobSubmitDto;
import com.yen.FlinkRestService.model.response.SqlJobSubmitResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqlJobService {

    @Value("${flink.sql_gateway.base_url}")
    private String sqlGatewayBaseUrl;

    private final SqlJobRepository sqlJobRepository;
    private final RestTemplateService restTemplateService;

    public String submitSQLJob(SqlJobSubmitDto sqlJobSubmitDto) {
        // Create session
        String sessionUrl = UriComponentsBuilder.fromHttpUrl(sqlGatewayBaseUrl)
                .pathSegment("v1", "sessions")
                .build()
                .toUriString();

        log.info("Creating SQL session at url={}", sessionUrl);

        ResponseEntity<String> sessionResponse = restTemplateService.sendPostRequest(sessionUrl, "", MediaType.APPLICATION_JSON);
        log.info("Session created, response={}", sessionResponse.getBody());

        SqlJobSubmitResponse sessionResult = JSON.parseObject(sessionResponse.getBody(), SqlJobSubmitResponse.class);
        if (sessionResult == null || sessionResult.getSessionHandle() == null) {
            throw new ExternalServiceException(
                    "SqlGateway", "Invalid or empty session creation response");
        }
        String sessionHandle = sessionResult.getSessionHandle();
        log.info("Session handle={}", sessionHandle);

        // Submit SQL statement
        String statementUrl = UriComponentsBuilder.fromHttpUrl(sqlGatewayBaseUrl)
                .pathSegment("v1", "sessions", sessionHandle, "statements")
                .build()
                .toUriString();

        log.info("Submitting SQL statement to url={}", statementUrl);

        String sqlPayload = sqlJobSubmitDto.toJsonPayload();
        log.info("SQL payload={}", sqlPayload);

        ResponseEntity<String> statementResponse = restTemplateService.sendPostRequest(statementUrl, sqlPayload, MediaType.APPLICATION_JSON);
        SqlJobSubmitResponse statementResult = JSON.parseObject(statementResponse.getBody(), SqlJobSubmitResponse.class);
        if (statementResult == null || statementResult.getOperationHandle() == null) {
            throw new ExternalServiceException(
                    "SqlGateway", "Invalid or empty statement submission response");
        }

        String operationHandle = statementResult.getOperationHandle();
        log.info("SQL job submitted, sessionHandle={}, operationHandle={}", sessionHandle, operationHandle);

        return operationHandle;
    }
}
