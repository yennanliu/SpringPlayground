package com.yen.controller;

import com.yen.service.S3Service;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Log4j2
public class S3Controller {

    @Autowired
    S3Service s3Service;

    @GetMapping("/s3/upload")
    public void upload(@RequestParam("dest_file") String destFile){

        Path currentRelativePath = Paths.get("");
        String current_path = currentRelativePath.toAbsolutePath().toString();
        log.info("current_path : {}", current_path);

        String BUCKET_NAME =  "yen-bucket1";
        //String KEY =  "README2.md"; // upload file name (s3)
        String KEY = destFile;
        //String file = "test.txt";
        String file = "README.md"; // to-upload file name (local)

        log.info(">>> putS3Object run ... destFile={}", destFile);

        s3Service.putS3Object(BUCKET_NAME, KEY, new File(file));
    }

}
