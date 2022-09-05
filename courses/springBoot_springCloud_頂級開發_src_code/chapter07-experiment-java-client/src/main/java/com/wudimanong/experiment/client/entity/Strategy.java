package com.wudimanong.experiment.client.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangqiao
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Strategy {

    /**
     * 策略Key
     */
    private String key;
    /**
     * 权重值
     */
    private String weight;

}
