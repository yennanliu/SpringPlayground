package com.yen.FlinkRestService.util;

public class SqlUtil {

    // "{\"statement\": \"SELECT 1, 2, 3\"}";
    public String prepareSQLcmd(String sqlCmd){
        return "{\"statement\": " + '"' + sqlCmd + '"' + "}";
    }

}
