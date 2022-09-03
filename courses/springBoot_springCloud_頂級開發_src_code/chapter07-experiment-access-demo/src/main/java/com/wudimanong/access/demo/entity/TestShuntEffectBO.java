package com.wudimanong.access.demo.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@Builder
public class TestShuntEffectBO {

    private Long uid;

    private Boolean isNewLogic;

    private Integer testGroupCounter;

    private Integer controlGroupCounter;
}
