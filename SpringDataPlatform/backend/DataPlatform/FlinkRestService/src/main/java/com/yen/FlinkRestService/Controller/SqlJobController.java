package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.SqlJobService;
import com.yen.FlinkRestService.model.dto.job.SqlJobSubmitDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/sql_job")
@RequiredArgsConstructor
public class SqlJobController {

    private final SqlJobService sqlJobService;

    @PostMapping
    public ResponseEntity<ApiResponse> submitSqlJob(@Valid @RequestBody SqlJobSubmitDto sqlJobSubmitDto) {
        String result = sqlJobService.submitSQLJob(sqlJobSubmitDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "SQL job has been submitted: " + result));
    }

    // Legacy endpoint for backward compatibility
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addJobLegacy(@Valid @RequestBody SqlJobSubmitDto sqlJobSubmitDto) {
        return submitSqlJob(sqlJobSubmitDto);
    }
}
