package com.yen.service.impl;

import com.yen.service.FileService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public void getDownloadCsv(String url) {

    }

    @Override
    public void mergeDownloadCsv(List<Path> paths, String destFile) throws IOException {

        // https://gist.github.com/alexwanng/d15941b58237ea89dc13e040ba6bc534

        System.out.println(">>> paths = " + paths.toString());

        List<String> mergedLines = new ArrayList<>();

        for (Path p : paths){
            List<String> lines = Files.readAllLines(p, Charset.forName("UTF-8"));
            if (!lines.isEmpty()){
                // add header (only once)
                mergedLines.add(lines.get(0));
            }
            mergedLines.addAll(lines.subList(1, lines.size()));
        }
        System.out.println(">>> mergedLines = " + mergedLines);
    }

    @Override
    public void uploadFile(String url) {
        System.out.println("upload file");
    }

}
