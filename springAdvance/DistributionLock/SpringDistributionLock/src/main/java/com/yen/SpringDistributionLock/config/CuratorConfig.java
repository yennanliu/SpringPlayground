package com.yen.SpringDistributionLock.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  ZK CuratorConfig setting
 *
 *      - CuratorFramework is similar to RedisTemplate, Redisson
 *      - https://youtu.be/BXjMbUVs0rY?si=d7MYUh_AaDHzTwzZ&t=656
 */
@Slf4j
@Configuration
public class CuratorConfig {

    @Bean
    public CuratorFramework curatorFramework(){

        // define retry policy
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(10000, 3);

        CuratorFramework zkClient = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);

        /** NOTE !!! need to start client, otherwise many methods NOT works */
        zkClient.start();
        log.info("zkClient config = " + zkClient.getConfig());

        return zkClient;
    }

}
