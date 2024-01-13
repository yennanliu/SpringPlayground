package com.yen.FlinkRestService.model.dto.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 *  # V1
 *  example url:
 *
 *      - http://localhost:8081/jars/927a9fac-c7bf-48cd-b1b8-b4e536449eb0_StreamSQLExample.jar/run?entry-class=org.apache.flink.table.examples.java.basics.StreamSQLExample
 *
 *
 *  example param:
 *      - entry-class=org.apache.flink.table.examples.java.basics.StreamSQLExample
 *      {"entryClass":"org.apache.flink.table.examples.java.basics.StreamSQLExample","parallelism":null,"programArgs":null,"savepointPath":null,"allowNonRestoredState":null}
 *
 *
 *  # V2
 *
 *   Path parameters
 *      - jarid - String value that identifies a jar. When uploading the jar a path is returned, where the filename is the ID. This value is equivalent to the `id` field in the list of uploaded jars (/jars).
 *
 *   Query parameters
 *
 *      - allowNonRestoredState (optional): Boolean value that specifies whether the job submission should be rejected if the savepoint contains state that cannot be mapped back to the job.
 *      - savepointPath (optional): String value that specifies the path of the savepoint to restore the job from.
 *      - program-args (optional): Deprecated, please use 'programArg' instead. String value that specifies the arguments for the program or plan
 *      - programArg (optional): Comma-separated list of program arguments.
 *      - entry-class (optional): String value that specifies the fully qualified name of the entry point class. Overrides the class defined in the jar file manifest.
 *      - parallelism (optional): Positive integer value that specifies the desired parallelism for the job.
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
