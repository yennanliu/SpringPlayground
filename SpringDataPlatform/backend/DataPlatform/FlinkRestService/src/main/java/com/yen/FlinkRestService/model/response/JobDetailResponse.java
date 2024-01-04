package com.yen.FlinkRestService.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class JobDetailResponse {

    private String jid;
    private String name;
    private boolean isStoppable;
    private String state;
    private long startTime;
    private long endTime;
    private int duration;
    private int maxParallelism;
    private long now;
    private JobTimeStampResponse timestamps;
    private String vertices;
    private String statusCounts;
    private String plan;
}
