package com.yen.controller;

import com.yen.service.S3Service;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void upload(){

        Path currentRelativePath = Paths.get("");
        String current_path = currentRelativePath.toAbsolutePath().toString();
        log.info("current_path : {}", current_path);

        String BUCKET_NAME =  "yen-bucket1";
        String KEY =  "test2/";
        //String file = "test.txt";
        String file = "README.md";

        log.info(">>> putS3Object run ...");

        s3Service.putS3Object(BUCKET_NAME, KEY+file, new File(file));
    }

}
