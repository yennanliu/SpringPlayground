package com.wudimanong.monitor.metrics.config;

import com.wudimanong.monitor.metrics.Metrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author jiangqiao
 */
@Configuration
public class CustomMetricsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer(Environment environment) {
        return registry -> {
            registry.config()
                    .commonTags("application", environment.getProperty("spring.application.name"));
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public Metrics metrics() {
        return new Metrics();
    }
}
