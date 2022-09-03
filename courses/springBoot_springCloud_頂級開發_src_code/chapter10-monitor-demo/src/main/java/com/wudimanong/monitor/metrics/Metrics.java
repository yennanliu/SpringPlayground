package com.wudimanong.monitor.metrics;

import com.wudimanong.monitor.metrics.CounterBuilder;
import com.wudimanong.monitor.metrics.GaugeBuilder;
import com.wudimanong.monitor.metrics.SummaryBuilder;
import com.wudimanong.monitor.metrics.TimerBuilder;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Counter.Builder;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.lang.NonNull;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author jiangqiao
 */
public class Metrics implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static Counter newCounter(String name, Consumer<Builder> consumer) {
        MeterRegistry meterRegistry = context.getBean(MeterRegistry.class);
        return new CounterBuilder(meterRegistry, name, consumer).build();
    }

    public static Timer newTimer(String name, Consumer<Timer.Builder> consumer) {
        return new TimerBuilder(context.getBean(MeterRegistry.class), name, consumer).build();
    }

    public static Gauge newGauge(String name, Supplier<Number> supplier, Consumer<Gauge.Builder> consumer) {
        return new GaugeBuilder(context.getBean(MeterRegistry.class), name, supplier, consumer).build();
    }

    public static DistributionSummary newSummary(String name, Consumer<DistributionSummary.Builder> consumer) {
        return new SummaryBuilder(context.getBean(MeterRegistry.class), name, consumer).build();
    }
}