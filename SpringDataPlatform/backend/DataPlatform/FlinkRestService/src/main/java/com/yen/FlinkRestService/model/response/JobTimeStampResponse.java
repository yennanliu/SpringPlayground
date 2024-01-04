package com.yen.FlinkRestService.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class JobTimeStampResponse {

    private Long cancelling;
    private Long created;
    private Long failed;
    private Long cancelled;
    private Long failing;
    private Long finished;
    private Long restarting;
    private Long suspended;
    private Long initializing;
    private Long reconciling;
    private Long running;
}
