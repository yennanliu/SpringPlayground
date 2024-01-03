package com.yen.FlinkRestService.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *  Example raw response:
 *
 *  Response Body: {"jobid":"4972cd1a7ff7ce366c03fb6049e73119"}
 */

@NoArgsConstructor
@Data
@ToString
public class JobSubmitResponse {

    private String jobid;
}
