package com.yen.FlinkRestService.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ClusterPingResponse {

    private Boolean isAccessible;
    private String message;
}
