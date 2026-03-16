package com.yen.FlinkRestService.model.dto.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * DTO for submitting a Flink job.
 *
 * Example URL:
 *   http://localhost:8081/jars/927a9fac-c7bf-48cd-b1b8-b4e536449eb0_StreamSQLExample.jar/run?entry-class=org.apache.flink.table.examples.java.basics.StreamSQLExample
 *
 * Query parameters:
 *   - allowNonRestoredState (optional): Boolean value that specifies whether the job submission should be rejected if the savepoint contains state that cannot be mapped back to the job.
 *   - savepointPath (optional): String value that specifies the path of the savepoint to restore the job from.
 *   - programArg (optional): Comma-separated list of program arguments.
 *   - entry-class (optional): String value that specifies the fully qualified name of the entry point class.
 *   - parallelism (optional): Positive integer value that specifies the desired parallelism for the job.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobSubmitDto {

    @NotNull(message = "JAR ID is required")
    private Integer jarId;

    private String entryClass;

    @Min(value = 1, message = "Parallelism must be at least 1")
    private Integer parallelism = 1;

    private String programArgs;

    private String savePointPath;

    private Boolean allowNonRestoredState;
}
