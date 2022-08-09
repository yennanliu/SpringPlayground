package com.yen.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.yen.service.S3Service;

import com.yen.util.S3FileUtil;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
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
        String KEY = "test2";  // upload file name (s3)
        String fileName = "README.md"; // s3 file we want to download
        S3Object s3Object = s3Service.downloadFile(BUCKET_NAME, KEY, fileName);

        S3ObjectInputStream s3is = s3Object.getObjectContent();

        // read content
        S3FileUtil s3FileUtil = new S3FileUtil();
        s3FileUtil.read(s3is);
    }

    // TODO : fix this (get s3 download link)
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

    @GetMapping("/s3/get_presigned_url")
    public String getPresignedURL(@RequestParam("s3_file") String s3File){

        Path currentRelativePath = Paths.get("");
        String current_path = currentRelativePath.toAbsolutePath().toString();
        log.info("current_path : {}", current_path);

        String BUCKET_NAME =  "yen-bucket1";
        String KEY = s3File;  // upload file name (s3)
        String file = "README.md"; // to-upload file name (local)
        log.info(">>> BUCKET_NAME={}, Key={}, file={}", BUCKET_NAME, s3File, file);

        URL s3PresignedUrl = s3Service.createPresignedUrl(BUCKET_NAME, KEY, file);
        log.info(">>> s3PresignedUrl = {}", s3PresignedUrl);
        log.info(">>> s3PresignedUrl.getPath() = {}", s3PresignedUrl.getPath());
        log.info(">>> s3PresignedUrl.getFile() = {}", s3PresignedUrl.getFile());

        return s3PresignedUrl.getPath();
    }

}
