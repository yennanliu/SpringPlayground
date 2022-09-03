package com.wudimanong.elasticjob.starter;

import java.util.ArrayList;
import java.util.List;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author qiaojiang
 */
public class ElasticJobBeanPostProcessor implements BeanPostProcessor, DisposableBean {

    /**
     * Zookeeper注册器
     */
    private ZookeeperRegistryCenter zookeeperRegistryCenter;
    /**
     * 已注册任务列表
     */
    private List<ScheduleJobBootstrap> schedulers = new ArrayList<>();

    /**
     * 构造注册中心
     */
    public ElasticJobBeanPostProcessor(ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    /**
     * Spring IOC容器扩展接口方法，Bean初始化后执行
     */
    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class<?> clazz = o.getClass();
        //只处理自定义ElasticTask注解
        if (!clazz.isAnnotationPresent(ElasticTask.class)) {
            return o;
        }
        if (!(o instanceof ElasticJob)) {
            return o;
        }
        ElasticJob job = (ElasticJob) o;
        //获取注解定义
        ElasticTask annotation = clazz.getAnnotation(ElasticTask.class);
        //注解任务名称定义
        String jobName = annotation.jobName();
        //注解cron表达式定义
        String cron = annotation.cron();
        //注解任务参数设置
        String jobParameter = annotation.jobParameter();
        //注解任务描述信息设置
        String description = annotation.description();
        //注解数据分片数设置
        int shardingTotalCount = annotation.shardingTotalCount();
        //注解数据分片参数设置
        String shardingItemParameters = annotation.shardingItemParameters();
        //注解是否禁用分片项设置
        boolean disabled = annotation.disabled();
        //注解重启任务定义信息是否覆盖设置
        boolean overwrite = annotation.overwrite();
        //注解是否开启故障转移设置
        boolean failover = annotation.failover();
        //注解是否开启错过任务重新执行设置
        boolean misfire = annotation.misfire();
        //注解分片策略类配置
        String jobShardingStrategyClass = annotation.jobShardingStrategyClass();

        //根据自定义注解配置，设置ElasticJob任务配置信息
        JobConfiguration coreConfiguration = JobConfiguration
                .newBuilder(jobName, shardingTotalCount).cron(cron).jobParameter(jobParameter).overwrite(overwrite)
                .failover(failover).misfire(misfire).description(description)
                .shardingItemParameters(shardingItemParameters).disabled(disabled)
                .jobShardingStrategyType(jobShardingStrategyClass)
                .build();
        //创建任务调度对象
        ScheduleJobBootstrap scheduleJobBootstrap = new ScheduleJobBootstrap(zookeeperRegistryCenter, job,
                coreConfiguration);
        //触发任务调度
        scheduleJobBootstrap.schedule();
        //将创建的任务对象加入集合，便于统一销毁
        schedulers.add(scheduleJobBootstrap);
        return job;
    }

    /**
     * 任务销毁方法
     */
    @Override
    public void destroy() {
        schedulers.forEach(jobScheduler -> jobScheduler.shutdown());
    }
}
