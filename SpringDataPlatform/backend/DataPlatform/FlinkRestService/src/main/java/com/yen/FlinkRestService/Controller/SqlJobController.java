package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.SqlJobService;
import com.yen.FlinkRestService.model.SqlJob;
import com.yen.FlinkRestService.model.dto.job.SqlJobSubmitDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sql_job")
@RequiredArgsConstructor
public class SqlJobController {

    private final SqlJobService sqlJobService;

    @GetMapping
    public ResponseEntity<List<SqlJob>> listSqlJobs() {
        return ResponseEntity.ok(sqlJobService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SqlJob> getSqlJobById(@PathVariable Integer id) {
        return ResponseEntity.ok(sqlJobService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SqlJob> submitSqlJob(@Valid @RequestBody SqlJobSubmitDto sqlJobSubmitDto) {
        SqlJob submitted = sqlJobService.submitSQLJob(sqlJobSubmitDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(submitted);
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/")
    public ResponseEntity<List<SqlJob>> listSqlJobsLegacy() {
        return listSqlJobs();
    }

    @PostMapping("/add")
    public ResponseEntity<SqlJob> addJobLegacy(@Valid @RequestBody SqlJobSubmitDto sqlJobSubmitDto) {
        return submitSqlJob(sqlJobSubmitDto);
    }
}
