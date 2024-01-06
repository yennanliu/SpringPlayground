package com.yen.FlinkRestService.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *   {"sessionHandle":"435abc8b-f8d5-4ded-967e-f32a4867d234"}
 *
 *   {"operationHandle":"d256d8b1-f93e-4ee3-bb75-447be071cb5d"}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SqlJobSubmitResponse {

    private String sessionHandle;
    private String operationHandle;
}
