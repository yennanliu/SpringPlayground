package com.yen.springBankApp.config;

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
 *
 *  - https://youtu.be/Sld_bKriREo?si=U8iQGbFAimHeLqpt&t=50
 *
 */
@Configuration
public class RedissonConfig {

    @Value("${redis.url}") // read from application.properties
    private String redisUrl;

    @Bean
    public RedissonClient redissonClient(){

        System.out.println("redisUrl = " + redisUrl);
        // init config
        Config config = new Config();
        // has couples of modes, e.g. : config.useClusterServers() ...
        config.useSingleServer()
               //.setDatabase(0) // setting DB for Redis, has 16 DB in default, use first DB by default
               // .setUsername("xxx").setPassword("yyy")
               // .setConnectionMinimumIdleSize(10) // set up min connection count in pool, better to set this up in prod env
               // .setConnectionPoolSize(100) // set up max connection count in pool, better to set this up in prod env
               //.setIdleConnectionTimeout(60000) // thread timeout time
               // .setConnectTimeout(100) // client get connection time out
               // .setConnectTimeout(1000) // Redis response time out
                .setAddress(redisUrl);
        return  Redisson.create(config);
    }

}
