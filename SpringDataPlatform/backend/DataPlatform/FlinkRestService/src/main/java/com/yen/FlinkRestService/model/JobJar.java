package com.yen.FlinkRestService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "job_jar")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobJar {

    /**
     * {"filename":"/var/folders/tz/5r4lbzxj5hs5q87gwdwyjnph0000gn/T/flink-web-29240320-952c-4ca9-86c0-76af9ee71577/flink-web-upload/b18d3b52-a8a5-4bd4-b2fe-385368fa0590_StreamSQLExample.jar",
     * "status":"success"}
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "status")
    private String status;

    @Column(name = "upload_time")
    private Date uploadTime;

}
