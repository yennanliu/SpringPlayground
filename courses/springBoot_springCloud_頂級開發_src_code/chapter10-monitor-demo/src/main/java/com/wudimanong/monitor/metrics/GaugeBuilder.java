package com.wudimanong.monitor.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Gauge.Builder;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author jiangqiao
 */
public class GaugeBuilder {

    private MeterRegistry meterRegistry;

    private Gauge.Builder builder;

    private Consumer<Builder> consumer;

    public GaugeBuilder(MeterRegistry registry, String name, Supplier<Number> supplier,
            Consumer<Gauge.Builder> consumer) {
        this.meterRegistry = registry;
        this.builder = Gauge.builder(name, supplier);
        this.consumer = consumer;
    }

    public Gauge build() {
        this.consumer.accept(builder);
        return builder.register(meterRegistry);
    }
}
