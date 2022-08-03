package com.yen.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import com.yen.service.S3Service;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
@Log4j2
public class S3ServiceImpl implements S3Service {

    String region = "ap-northeast-1";
    String defaultBucket = "default_bucket";

    private static final int DEFAULT_EXPIRE_TIME = 5;

    @Override
    public String putObject(String bucket, String key, File file) {

        String bucketName = StringUtils.isBlank(bucket)? defaultBucket:bucket;
        log.info(String.format("bucket=%s, region=%s, key=%s, file=%s", bucketName, region, key, file.getPath()));
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        s3.putObject(bucketName, key, file);
        return s3.generatePresignedUrl(bucketName, key, null).getPath();
    }

    @Override
    public void putS3Object(String bucket, String key, File file) {

        int time = 0;
        while (time <= DEFAULT_EXPIRE_TIME) {
            try {
                String path = putObject(bucket, key, file);
                log.info(String.format(">>> putS3Object getPath=%s", path));
                break;
            } catch (Exception e) {
                log.error(">>> putS3Object error, retrying...{}", time, e);
                time++;
            }
        }
        if (time > DEFAULT_EXPIRE_TIME) {
            throw new RuntimeException(">>> File push to S3 system retry "+DEFAULT_EXPIRE_TIME+" times fail");
        }
    }

    @Override
    public void putS3Object(String key, File file) {

        putS3Object(defaultBucket, key, file);
    }

    @Override
    public URL getS3FileUrl(String bucket, String key, File file) {
        try{
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
            URL s3URL = s3.getUrl(bucket, key+file);
            // s3Client.getUrl("your-bucket", "some-path/some-key.jpg").toString()
            return s3URL;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
