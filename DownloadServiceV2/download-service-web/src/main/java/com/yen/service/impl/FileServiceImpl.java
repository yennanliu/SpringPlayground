package com.yen.service.impl;

import com.yen.service.FileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public void getDownloadCsv(String url) {

    }

    // TODO : optimize performance : either use library or tune algorithm
    @Override
    public void mergeDownloadCsv(Path[] paths, String destFile) throws IOException {

        // https://gist.github.com/alexwanng/d15941b58237ea89dc13e040ba6bc534
        // https://github.com/james-schmidt/CSVDataMerge
        // https://github.com/search?l=Java&o=desc&q=merge+csv&s=stars&type=Repositories

        System.out.println(">>> paths = " + paths.toString());

        Set<String> header = new HashSet<>(Arrays.asList());

        List<String> mergedLines = new ArrayList<>();

        for (Path p : paths){
            List<String> lines = Files.readAllLines(p, Charset.forName("UTF-8"));
            if (!lines.isEmpty() && !header.contains(lines.get(0)) ){
                // add header (only once)
                mergedLines.add(lines.get(0));
                header.add(lines.get(0));
            }
            mergedLines.addAll(lines.subList(1, lines.size()));
        }
        System.out.println(">>> mergedLines = " + mergedLines);

        // save to csv
        try {
            File myObj = new File(destFile);
            FileWriter fWriter = new FileWriter(destFile);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    @Override
    public void uploadFile(String url) {
        System.out.println("upload file");
    }

}
