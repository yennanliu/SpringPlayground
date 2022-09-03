package com.wudimanong.monitor.metrics;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.DistributionSummary.Builder;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.function.Consumer;

/**
 * @author jiangqiao
 */
public class SummaryBuilder {

    private MeterRegistry meterRegistry;

    private DistributionSummary.Builder builder;

    private Consumer<Builder> consumer;

    public SummaryBuilder(MeterRegistry meterRegistry, String name, Consumer<DistributionSummary.Builder> consumer) {
        this.meterRegistry = meterRegistry;
        this.builder = DistributionSummary.builder(name);
        this.consumer = consumer;
    }

    public DistributionSummary build() {
        this.consumer.accept(builder);
        return this.builder.register(meterRegistry);
    }

}
