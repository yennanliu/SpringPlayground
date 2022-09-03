package com.wudimanong.monitor.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Counter.Builder;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.function.Consumer;

/**
 * @author jiangqiao
 */
public class CounterBuilder {

    private final MeterRegistry meterRegistry;

    private Counter.Builder builder;

    private Consumer<Builder> consumer;

    public CounterBuilder(MeterRegistry meterRegistry, String name, Consumer<Counter.Builder> consumer) {
        this.builder = Counter.builder(name);
        this.meterRegistry = meterRegistry;
        this.consumer = consumer;
    }

    public Counter build() {
        consumer.accept(builder);
        return builder.register(meterRegistry);
    }
}