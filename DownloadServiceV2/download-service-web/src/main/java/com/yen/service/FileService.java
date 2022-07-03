package com.yen.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileService {

    public String getUrl();

    public void getDownloadCsv(String url);

    public void mergeDownloadCsv(Path[] paths, String destFile) throws IOException;

    public void uploadFile(String url);
}
