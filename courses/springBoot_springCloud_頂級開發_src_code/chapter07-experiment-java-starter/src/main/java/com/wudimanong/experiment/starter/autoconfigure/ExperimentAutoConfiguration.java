package com.wudimanong.experiment.starter.autoconfigure;

import com.wudimanong.experiment.starter.ExperimentTemplate;
import com.wudimanong.experiment.starter.feign.ExperimentFeignClient;
import com.wudimanong.experiment.starter.feign.ExperimentFeignSource;
import com.wudimanong.experiment.starter.properties.ExperimentProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangqiao
 */
@Configuration
@ConditionalOnProperty(value = "experiment.enable", havingValue = "true")
@EnableConfigurationProperties({ExperimentProperties.class})
public class ExperimentAutoConfiguration {

    @EnableFeignClients(clients = ExperimentFeignClient.class)
    @EnableDiscoveryClient
    @ConditionalOnMissingBean({ExperimentFeignSource.class, ExperimentFeignClient.class})
    public class ExperimentSourceConfiguration {

        @Bean
        @ConditionalOnMissingBean(ExperimentFeignSource.class)
        public ExperimentFeignSource experimentFeignSource(ExperimentFeignClient experimentFeignClient) {
            return new ExperimentFeignSource(experimentFeignClient);
        }
    }

    @Bean
    @ConditionalOnBean(ExperimentFeignSource.class)
    public ExperimentTemplate experimentTemplate(ExperimentFeignSource experimentFeignSource) {
        return new ExperimentTemplate(experimentFeignSource);
    }
}
