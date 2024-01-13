package com.yen.FlinkRestService.model.dto.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CancelJobDto {

    private String jobId;
}
