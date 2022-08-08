package com.yen.service.impl;

import com.yen.service.FileService;
import org.springframework.stereotype.Service;

import java.io.*;
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

        /** Part 1 : merge csv */
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

        /** Part 2 : save output (as csv) */
        // https://www.programcreek.com/2011/03/java-write-to-a-file-code-example/

        BufferedWriter bw = null;
        try{
            File file = new File(destFile);
            FileOutputStream fos = new FileOutputStream(file);

            bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (String s : mergedLines) {
                bw.write(s);
                bw.newLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (bw != null){
                bw.close();
            }
        }
    }

    @Override
    public void uploadFile(String url) {

        System.out.println("upload file");
    }

}
