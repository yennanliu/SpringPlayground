package com.yen.FlinkRestService.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobUpdateDto {

    private Integer id;
    private String jobId;
    private Long startTime;
    private Long endTime;
    private Long duration;
    private String state;
    private Long lastModify;
}
