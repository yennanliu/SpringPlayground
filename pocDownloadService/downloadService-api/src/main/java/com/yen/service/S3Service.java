package com.yen.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface S3Service {

    String getS3UrlByFilePath(String filePath);

    List<String> listFileByPath(String path);

    void downloadFile(String path);

    void deleteFile(String path);

    boolean checkFileExist(String path);

    String mergeS3File(String path);

}
