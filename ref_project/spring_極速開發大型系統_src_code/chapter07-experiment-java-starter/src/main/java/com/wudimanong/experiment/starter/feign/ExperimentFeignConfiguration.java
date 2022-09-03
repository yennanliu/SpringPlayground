package com.wudimanong.experiment.starter.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangqiao
 */
@Configuration
public class ExperimentFeignConfiguration {

    /**
     * 构建Fallback工厂类
     *
     * @return
     */
    @Bean
    ExperimentFeignFallbackFactory experimentFeignFallbackFactory() {
        return new ExperimentFeignFallbackFactory();
    }
}
