package com.yen.springBootAdvance1.config;

// https://www.youtube.com/watch?v=9GiDJMkIdns&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=6

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;


@Configuration
public class MyCacheConfig {

    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator(){

        return new KeyGenerator(){

            @Override
            public Object generate(Object target, Method method, Object... params) {
                System.out.println(">>> myKeyGenerator run ...");
                return method.getName() + "[" + Arrays.asList(params).toString() + "]";
            }
        };

    }

}
