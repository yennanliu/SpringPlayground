package com.yen.FlinkRestService.util;

public class SqlUtil {

    // "{\"statement\": \"SELECT 1, 2, 3\"}";
    public String prepareSQLcmd(String sqlCmd){
        return "{\"statement\": " + '"' + sqlCmd + '"' + "}";
    }

    // TODO : implement it
    // CREATE table print_sink( a int, b int ) WITH ( 'connector' = 'print', 'print-identifier' = '', ) INSERT INTO print_sink SELECT 1, 2
    public String transfromToOneLine(String sqlCMd){
        return sqlCMd;
    }

}
