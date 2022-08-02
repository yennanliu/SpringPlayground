package com.yen.service.impl;

import com.yen.service.S3Service;

import java.io.File;

public class S3ServiceImpl implements S3Service {

    @Override
    public String putObject(String bucket, String key, File file) {
        return null;
    }

    @Override
    public void putS3Object(String bucket, String key, File file) {

    }

    @Override
    public void putS3Object(String key, File file) {

    }

}
