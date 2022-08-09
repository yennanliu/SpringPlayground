package com.yen.cron;

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

        String BUCKET_NAME =  "yen-bucket1";
        String KEY = "test2";  // upload file name (s3)
        String S3_SRC_FILE = "README.md"; // s3 file we want to download
        String DEST_FILE = "readme_download.md";

        log.info(">>> download from s3");
        // save file
        S3Object s3Object = s3Service.downloadFile(BUCKET_NAME, KEY, S3_SRC_FILE);
        S3ObjectInputStream s3is = s3Object.getObjectContent();
        fileService.saveS3File(s3is, DEST_FILE);

        log.info(">>> transform");

        log.info(">>> upload to s3");
        s3Service.putObject(BUCKET_NAME, "upload1/" +DEST_FILE, new File(DEST_FILE));

        log.info(">>> get PresignedUrl");
        URL presignedUrl = s3Service.createPresignedUrl(BUCKET_NAME, "upload1/"+DEST_FILE);
        log.info(">>> presignedUrl = " + presignedUrl);
    }

}
