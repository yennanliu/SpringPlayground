package com.yen.SpringDistributionLock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  Config for Redisson (redis client)
 *
 *  - https://youtu.be/ynJQouCae4I?si=FysULnQTAg0s_HL8&t=465
 *  - doc : https://github.com/redisson/redisson/wiki/2.-Configuration
 *
 */
@Configuration
public class RedissonConfig {

    @Value("${redis.url}")
    private String redisUrl;

    @Bean
    public RedissonClient redissonClient(){

        System.out.println("redisUrl = " + redisUrl);
        Config config = new Config();
        config.useSingleServer().setAddress(redisUrl);
        return  Redisson.create(config);
    }

}
