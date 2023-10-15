package com.yen.springWarehouse.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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

}
