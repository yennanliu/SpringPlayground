package com.yen.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jsoup.nodes.Document;

import java.io.File;
import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectInputStream;

public interface FileService {

    public String getUrl();

    public void getDownloadCsv(String url);

    public void mergeDownloadCsv(Path[] paths, String destFile) throws IOException;

    public void saveFile(String srcFile,  String destFile);

    public void saveS3File(S3ObjectInputStream s3InputStream,  String destFile);

    public void saveByteToFile(byte[] bytes, String fileName);
}
