package com.wudimanong.elasticjob.starter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * @author qiaojiang
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticTask {
    /**
     * 注解属性，用于定义任务名称
     */
    String jobName();

    /**
     * 注解属性，用于定义cron时间表达式
     */
    String cron();

    /**
     * 注解属性，用于定义任务参数信息
     */
    String jobParameter() default "";

    /**
     * 注解属性，用于定义任务描述信息
     */
    String description() default "";

    /**
     * 注解属性，用于定义任务分片数
     */
    int shardingTotalCount() default 1;

    /**
     * 注解属性，用于定义任务分片参数
     */
    String shardingItemParameters() default "";

    /**
     * 注解属性，用于定时是否禁用分片项设置
     */
    boolean disabled() default false;

    /**
     * 注解属性，配置分片策略
     */
    String jobShardingStrategyClass() default "";

    /**
     * 注解属性，配置是否故障转移功能
     */
    boolean failover() default false;

    /**
     * 注解属性，配置是否运行重启时重至任务定义信息（包括cron时间片设置）
     */
    boolean overwrite() default false;

    /**
     * 注解属性，配置是否开启错误任务重新执行
     */
    boolean misfire() default true;
}
