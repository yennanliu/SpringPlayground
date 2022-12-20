package com.yen.cron;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import com.yen.service.FileService;
import com.yen.service.S3Service;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;

@Component
@Configurable
@EnableScheduling
@Log4j2
public class DownloadUpload2S3Job {

    @Autowired
    S3Service s3Service;

    @Autowired
    FileService fileService;

    @Scheduled(cron = "*/10 * *  * * * ")
    public void run(){

        log.info(">>> DownloadUpload2S3Job run ...");

//        String BUCKET_NAME =  "yen-bucket1";
//        String KEY = "README.md";  // file path (directory + filename) (s3)
//        String DEST_FILE = "readme_downloadXXX.md";

        String BUCKET_NAME =  "yen-bucket2";
        String KEY = "123.png";  // file path (directory + filename) (s3)
        String DEST_FILE = "123_download.png";

        log.info(">>> download from s3");
        // save file
        ObjectMetadata s3Object = s3Service.downloadFileV3(BUCKET_NAME, KEY, DEST_FILE);

        log.info(">>> transform");

        log.info(">>> upload to s3");
        s3Service.putObject(BUCKET_NAME, "/upload1/" +DEST_FILE, new File(DEST_FILE));

        log.info(">>> get PresignedUrl");

        System.out.println(">>> BUCKET_NAME = " + BUCKET_NAME);
        System.out.println(">>> S3_KEY = " + "upload1/"+DEST_FILE);

        //URL presignedUrl = s3Service.createPresignedUrl(BUCKET_NAME, "upload1/"+DEST_FILE);
        //String presignedUrl = s3Service.createPresignedUrlV2(BUCKET_NAME, "upload1/"+DEST_FILE);
        String presignedUrl = s3Service.createPresignedUrlV2(BUCKET_NAME, KEY);
        log.info(">>> presignedUrl = " + presignedUrl);
    }

}
