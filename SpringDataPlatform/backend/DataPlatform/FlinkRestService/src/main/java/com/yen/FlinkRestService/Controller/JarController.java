package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.model.dto.UploadJarDto;
import com.yen.FlinkRestService.Service.JarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/jar")
public class JarController {

    @Autowired
    JarService jarService;

    /**
     *  curl cmd :
     *
     *  curl -X POST -H "Expect:" -F "jarfile=@table/StreamSQLExample.jar" http://localhost:8081/jars/upload
     *
     *  curl -X POST -F "jarfile=@/Users/yennanliu/flink-1.17.2/examples/table/StreamSQLExample.jar" http://localhost:8081/jars/upload
     */
    @PostMapping("/add_jar")
    public ResponseEntity<ApiResponse> addJobJar(@RequestBody UploadJarDto uploadJarDto){

        jarService.addJobJar(uploadJarDto);
        return new ResponseEntity<>(new ApiResponse(true, "Job jar has been added"), HttpStatus.CREATED);
    }

}
