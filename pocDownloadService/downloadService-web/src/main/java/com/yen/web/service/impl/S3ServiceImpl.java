package com.yen.web.service.impl;

import com.yen.service.S3Service;

import java.util.List;

public class S3ServiceImpl implements S3Service {

    @Override
    public String getS3UrlByFilePath(String filePath) {
        System.out.println(">>> getS3UrlByFilePath run ...");
        return null;
    }

    @Override
    public List<String> listFileByPath(String path) {
        System.out.println(">>> listFileByPath run ...");
        return null;
    }

    @Override
    public void downloadFile(String path) {
        System.out.println(">>> downloadFile run ...");
    }

    @Override
    public void deleteFile(String path) {
        System.out.println(">>> deleteFile run ...");
    }

    @Override
    public boolean checkFileExist(String path) {
        System.out.println(">>> checkFileExist run ...");
        return false;
    }

    @Override
    public String mergeS3File(String path) {
        System.out.println(">>> mergeS3File run ...");
        return null;
    }

}
