package com.yen.FlinkRestService.model.dto.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.criteria.CriteriaBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddClusterDto {

    private String url;
    private Integer port;
}
