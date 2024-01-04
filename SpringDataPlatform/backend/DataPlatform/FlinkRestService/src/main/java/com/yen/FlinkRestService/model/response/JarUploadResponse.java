package com.yen.FlinkRestService.model.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class JarUploadResponse {


    /**
     * filename : /data/flink/upload_jars/flink-web-upload/184ffd19-0280-4aa8-8a64-fd5bc91a36e0_WordCount.jar
     * status : success
     */
    private String filename;
    private String status;
    private List<String> errors;
}