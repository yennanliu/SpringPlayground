package com.wudimanong.experiment.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jiangqiao
 */
@Data
@ConfigurationProperties("experiment")
public class ExperimentProperties {

    private String enable;
}
