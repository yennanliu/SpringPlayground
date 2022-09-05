package com.wudimanong.schedule.job;

import com.wudimanong.elasticjob.starter.ElasticTask;
import java.util.Date;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;

/**
 * @author jiangqiao
 */
@ElasticTask(jobName = "testJob", cron = "*/5 * * * * ?", description = "自定义Task", overwrite = true)
public class TestJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("跑任务->" + new Date());
    }
}
