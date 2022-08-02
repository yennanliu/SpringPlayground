package com.yen.service;

import java.io.File;

public interface S3Service {

    String putObject(String bucket, String key, File file);

    void putS3Object(String bucket, String key, File file);

    void putS3Object(String key, File file);
}
