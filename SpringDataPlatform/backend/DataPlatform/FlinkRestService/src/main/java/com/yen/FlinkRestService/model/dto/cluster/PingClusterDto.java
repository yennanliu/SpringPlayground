package com.yen.FlinkRestService.model.dto.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PingClusterDto {

    @NotBlank(message = "Cluster ID is required")
    private String id;
}
