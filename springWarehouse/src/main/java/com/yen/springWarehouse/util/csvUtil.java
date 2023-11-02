package com.yen.springWarehouse.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class csvUtil {

    // https://www.baeldung.com/java-csv
    public String covertArrayToCSV(String[] data){
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }


    public List loadCsvAsList(MultipartFile file) {

        if (file.isEmpty()) {
            log.warn("input file is empty");
            return null;
        }

        BufferedReader bufferedReader;
        List result = new ArrayList<>();
        int idx = 0;

        try {
            String line;
            InputStream inputStream = file.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                idx += 1;
                String[] data = line.split(",");
                log.info(">>> (loadCsvAsList) line = " + line);
                if (data == null) {
                    throw new RuntimeException("Input csv schema is wrong");
                }
                if (idx <= 1) {
                    continue; // first line is header
                }
                result.add(StringUtils.join(line, "|"));
            }
        } catch (Exception e) {
            log.error(">>> (loadCsvAsList) Load CSV failed : " + e);
        }
        return result;
    }

}
