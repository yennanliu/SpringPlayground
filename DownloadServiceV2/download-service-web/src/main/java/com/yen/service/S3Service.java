package com.yen.service;

import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.net.URL;

public interface S3Service {

    public String putObject(String bucket, String key, File file);

    public void putS3Object(String bucket, String key, File file);

    public void putS3Object(String key, File file);

    public URL getS3FileUrl(String bucket, String key, File file);

    public S3Object downloadFile(String bucket, String key, String fileName);

}
