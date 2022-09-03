package com.wudimanong.monitor.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Timer.Builder;
import java.util.function.Consumer;

/**
 * @author jiangqiao
 */
public class TimerBuilder {

    private final MeterRegistry meterRegistry;

    private Timer.Builder builder;

    private Consumer<Builder> consumer;

    public TimerBuilder(MeterRegistry meterRegistry, String name, Consumer<Timer.Builder> consumer) {
        this.builder = Timer.builder(name);
        this.meterRegistry = meterRegistry;
        this.consumer = consumer;
    }

    public Timer build() {
        this.consumer.accept(builder);
        return builder.register(meterRegistry);
    }
}
