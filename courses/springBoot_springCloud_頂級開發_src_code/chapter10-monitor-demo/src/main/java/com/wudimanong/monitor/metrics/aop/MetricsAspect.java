package com.wudimanong.monitor.metrics.aop;

import com.wudimanong.monitor.metrics.Metrics;
import com.wudimanong.monitor.metrics.annotation.Count;
import com.wudimanong.monitor.metrics.annotation.Monitor;
import com.wudimanong.monitor.metrics.annotation.Tp;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import java.lang.reflect.Method;
import java.util.function.Function;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author jiangqiao
 */
@Aspect
@Component
public class MetricsAspect {

    /**
     * Prometheus指标管理
     */
    private MeterRegistry registry;

    private Function<ProceedingJoinPoint, Iterable<Tag>> tagsBasedOnJoinPoint;

    public MetricsAspect(MeterRegistry registry) {
        this.init(registry, pjp -> Tags
                .of(new String[]{"class", pjp.getStaticPart().getSignature().getDeclaringTypeName(), "method",
                        pjp.getStaticPart().getSignature().getName()}));
    }

    public void init(MeterRegistry registry, Function<ProceedingJoinPoint, Iterable<Tag>> tagsBasedOnJoinPoint) {
        this.registry = registry;
        this.tagsBasedOnJoinPoint = tagsBasedOnJoinPoint;
    }

    /**
     * 针对@Tp指标配置注解的逻辑实现
     */
    @Around("@annotation(com.wudimanong.monitor.metrics.annotation.Tp)")
    public Object timedMethod(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        Tp tp = method.getAnnotation(Tp.class);
        Timer.Sample sample = Timer.start(this.registry);
        String exceptionClass = "none";
        try {
            return pjp.proceed();
        } catch (Exception ex) {
            exceptionClass = ex.getClass().getSimpleName();
            throw ex;
        } finally {
            try {
                String finalExceptionClass = exceptionClass;
                //创建定义计数器，并设置指标的Tags信息（名称可以自定义）
                Timer timer = Metrics.newTimer("tp.method.timed",
                        builder -> builder.tags(new String[]{"exception", finalExceptionClass})
                                .tags(this.tagsBasedOnJoinPoint.apply(pjp)).tag("description", tp.description())
                                .publishPercentileHistogram().register(this.registry));
                sample.stop(timer);
            } catch (Exception exception) {
            }
        }
    }

    /**
     * 针对@Count指标配置注解的逻辑实现
     */
    @Around("@annotation(com.wudimanong.monitor.metrics.annotation.Count)")
    public Object countMethod(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        Count count = method.getAnnotation(Count.class);
        String exceptionClass = "none";
        try {
            return pjp.proceed();
        } catch (Exception ex) {
            exceptionClass = ex.getClass().getSimpleName();
            throw ex;
        } finally {
            try {
                String finalExceptionClass = exceptionClass;
                //创建定义计数器，并设置指标的Tags信息（名称可以自定义）
                Counter counter = Metrics.newCounter("count.method.counted",
                        builder -> builder.tags(new String[]{"exception", finalExceptionClass})
                                .tags(this.tagsBasedOnJoinPoint.apply(pjp)).tag("description", count.description())
                                .register(this.registry));
                counter.increment();
            } catch (Exception exception) {
            }
        }
    }

    /**
     * 针对@Monitor通用指标配置注解的逻辑实现
     */
    @Around("@annotation(com.wudimanong.monitor.metrics.annotation.Monitor)")
    public Object monitorMethod(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        Monitor monitor = method.getAnnotation(Monitor.class);
        String exceptionClass = "none";
        try {
            return pjp.proceed();
        } catch (Exception ex) {
            exceptionClass = ex.getClass().getSimpleName();
            throw ex;
        } finally {
            try {
                String finalExceptionClass = exceptionClass;
                //计时器Metric
                Timer timer = Metrics.newTimer("tp.method.timed",
                        builder -> builder.tags(new String[]{"exception", finalExceptionClass})
                                .tags(this.tagsBasedOnJoinPoint.apply(pjp)).tag("description", monitor.description())
                                .publishPercentileHistogram().register(this.registry));
                Timer.Sample sample = Timer.start(this.registry);
                sample.stop(timer);

                //计数器Metric
                Counter counter = Metrics.newCounter("count.method.counted",
                        builder -> builder.tags(new String[]{"exception", finalExceptionClass})
                                .tags(this.tagsBasedOnJoinPoint.apply(pjp)).tag("description", monitor.description())
                                .register(this.registry));
                counter.increment();
            } catch (Exception exception) {
            }
        }
    }
}
