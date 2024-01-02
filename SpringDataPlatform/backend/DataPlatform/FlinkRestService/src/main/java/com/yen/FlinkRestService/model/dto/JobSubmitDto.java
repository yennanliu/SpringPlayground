package com.yen.FlinkRestService.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 *  example url:
 *
 *      - http://localhost:8081/jars/927a9fac-c7bf-48cd-b1b8-b4e536449eb0_StreamSQLExample.jar/run?entry-class=org.apache.flink.table.examples.java.basics.StreamSQLExample
 *
 *
 *  example param:
 *      - entry-class=org.apache.flink.table.examples.java.basics.StreamSQLExample
 *      {"entryClass":"org.apache.flink.table.examples.java.basics.StreamSQLExample","parallelism":null,"programArgs":null,"savepointPath":null,"allowNonRestoredState":null}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobSubmitDto {

    private String jarId; // 927a9fac-c7bf-48cd-b1b8-b4e536449eb0_StreamSQLExample.jar
    private String entryClass; // "org.apache.flink.table.examples.java.basics.StreamSQLExample"
    private Integer parallelism = 1; // default as 1
    private String programArgs;
    private String savePointPath;
    private Boolean allowNonRestoredState;
}
