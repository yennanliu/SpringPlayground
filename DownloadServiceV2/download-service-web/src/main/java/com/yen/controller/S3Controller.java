package com.yen.controller;

import com.yen.service.S3Service;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import java.net.URL;

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
        String KEY = destFile;  // upload file name (s3)
        String file = "README.md"; // to-upload file name (local)

        log.info(">>> putS3Object run ... destFile={}", destFile);

        s3Service.putS3Object(BUCKET_NAME, KEY, new File(file));
    }

    @GetMapping("/s3/download")
    public void download(){

        String BUCKET_NAME =  "yen-bucket1";
        String KEY = "";  // upload file name (s3)
        String fileName = "README.md"; // s3 file we want to download
        s3Service.downloadFile(BUCKET_NAME, KEY, fileName);
    }

    @GetMapping("/s3/get_download_url")
    public String getDownloadURL(@RequestParam("s3_file") String s3File){

        Path currentRelativePath = Paths.get("");
        String current_path = currentRelativePath.toAbsolutePath().toString();
        log.info("current_path : {}", current_path);

        String BUCKET_NAME =  "yen-bucket1";
        String KEY = s3File;  // upload file name (s3)
        String file = "README.md"; // to-upload file name (local)
        log.info(">>> BUCKET_NAME={}, Key={}, file={}", BUCKET_NAME, s3File, file);

        URL s3FileURL = s3Service.getS3FileUrl(BUCKET_NAME, KEY, new File(file));
        log.info(">>> s3FileURL = {}", s3FileURL);
        log.info(">>> s3FileURL.getPath() = {}", s3FileURL.getPath());
        log.info(">>> s3FileURL.getFile() = {}", s3FileURL.getFile());


//        try {
//            log.info(">>> s3FileURL = {}", s3FileURL.getContent());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return s3FileURL.getPath();
    }

}
