package com.yen.FlinkRestService.model.dto.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobUpdateDto {

    @NotNull(message = "Job ID is required")
    private Integer id;

    private String jobId;
    private Long startTime;
    private Long endTime;
    private Long duration;
    private String state;
    private Long lastModify;
}
