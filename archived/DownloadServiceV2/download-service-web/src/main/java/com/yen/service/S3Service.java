package com.yen.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.net.URL;

public interface S3Service {

    public String putObject(String bucket, String key, File file);

    public void putS3Object(String bucket, String key, File file);

    public void putS3Object(String key, File file);

    public URL getS3FileUrl(String bucket, String key, File file);

    public S3Object downloadFile(String bucket, String key);

    public S3Object downloadFileV2(String bucket, String key);

    public ObjectMetadata downloadFileV3(String bucket, String key, String localFilename);

    public byte[] downloadS3Object(String bucketName, String path);

    public URL createPresignedUrl(String bucket, String key);

    public String createPresignedUrlV2(String bucket, String key);

}
