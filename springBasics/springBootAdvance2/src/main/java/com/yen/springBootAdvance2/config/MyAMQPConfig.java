package com.yen.springBootAdvance2.config;

// https://www.youtube.com/watch?v=FqHO8tiUthQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=18

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAMQPConfig {

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
