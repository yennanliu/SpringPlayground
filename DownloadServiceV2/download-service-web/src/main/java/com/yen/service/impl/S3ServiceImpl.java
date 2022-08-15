package com.yen.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.util.IOUtils;
import com.yen.service.S3Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.S3Object;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.StringUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.zip.ZipInputStream;

@Service
@Log4j2
public class S3ServiceImpl implements S3Service {

    String REGION = "ap-northeast-1";
    String DEFAULT_BUCKET = "default_bucket";

    private static final int DEFAULT_EXPIRE_TIME = 5;

    @Override
    public String putObject(String bucket, String key, File file) {

        String bucketName = StringUtils.isBlank(bucket)? DEFAULT_BUCKET:bucket;
        log.info(String.format("bucket=%s, region=%s, key=%s, file=%s", bucketName, REGION, key, file.getPath()));

        final AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(REGION)
                .build();

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
            throw new RuntimeException(">>> File push to S3 system retry " + DEFAULT_EXPIRE_TIME + " times fail");
        }
    }

    @Override
    public void putS3Object(String key, File file) {

        putS3Object(DEFAULT_BUCKET, key, file);
    }

    @Override
    public URL getS3FileUrl(String bucket, String key, File file) {
        try{
            final AmazonS3 s3 = AmazonS3ClientBuilder
                    .standard()
                    .withRegion(REGION)
                    .build();

            URL s3URL = s3.getUrl(bucket, key+file);
            // s3Client.getUrl("your-bucket", "some-path/some-key.jpg").toString()
            return s3URL;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public S3Object downloadFile(String bucket, String key, String fileName) {

        // https://stackoverflow.com/questions/44120235/how-to-download-a-file-from-s3-using-provided-url
        // https://www.programcreek.com/java-api-examples/?api=com.amazonaws.services.s3.AmazonS3URI

        final String prefix = "https://s3.amazonaws.com/";

        final AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(REGION)
                .build();

        //URI fileToBeDownloaded = null;
        URI fileToBeDownloaded = null;
        try {
            //fileToBeDownloaded = new URI("https://s3.amazonaws.com/account-update/input.csv");
            fileToBeDownloaded = new URI("https://s3.amazonaws.com/yen-bucket1/README.md");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        //String fileURI = prefix + bucket + fileName;
        //String fileURI = "https://s3.amazonaws.com/yen-bucket1/README.md";
        AmazonS3URI s3URI = new AmazonS3URI(fileToBeDownloaded);

        //log.info(">>> fileURI = {}", fileURI);
        log.info(">>> s3URI = {}", s3URI);

        S3Object s3Object = s3.getObject(s3URI.getBucket(), s3URI.getKey());
        //fileToBeDownloaded = new URI(prefix + bucket + fileName);
        //S3Object s3Object = s3.getObject(bucket, key);

        return s3Object;
    }

    @Override
    public byte[] downloadS3Object(String bucketName, String path) {

        final AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(REGION)
                .build();

        try {
            log.info(">>> S3ServiceImpl - downloadS3Object OK, bucket={}, path={}", bucketName, path);
            S3Object s3Object = s3.getObject(bucketName, path);
            // download zip file : https://stackoverflow.com/questions/50437965/download-the-files-inside-compressed-gz-files-from-s3-bucket
            ResponseEntity<String> respBody = ResponseEntity.ok(IOUtils.toString(new ZipInputStream(s3Object.getObjectContent())));
            //return IOUtils.toByteArray(s3Object.getObjectContent());
            return respBody.getBody().getBytes();
        }catch (Exception e) {
            log.error(">>> S3ServiceImpl - downloadS3Object failed.", e);
            try {
                throw new Exception(">>> S3ServiceImpl - downloadS3Object failed");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public URL createPresignedUrl(String bucket, String key) {

        // https://docs.aws.amazon.com/AmazonS3/latest/userguide/ShareObjectPreSignedURL.html

        //Regions clientRegion = Regions.DEFAULT_REGION;
        Regions clientRegion = Regions.AP_NORTHEAST_1;
        String bucketName = bucket;
        String objectKey = key;

        log.info(">>> clientRegion = {}, bucketName = {}, objectKey = {}", clientRegion, bucket, key);

        URL url = null;

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = Instant.now().toEpochMilli();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            log.info(">>> Pre-Signed URL: " + url.toString());

            return url;

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            log.info(">>> Pre-Signed URL: " + url.toString());
            return url;
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            log.info(">>> Pre-Signed URL: " + url.toString());
            return url;
        }
    }

}
