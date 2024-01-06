package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.SqlJobService;
import com.yen.FlinkRestService.model.dto.JobSubmitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sql_job")
public class SqlJobController {

    @Autowired
    SqlJobService sqlJobService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addJob(){

        //jobService.addJob(jobSubmitDto);
        sqlJobService.submitSQLJob();
        return new ResponseEntity<>(new ApiResponse(true, "SQL job has been added"), HttpStatus.CREATED);
    }

}
