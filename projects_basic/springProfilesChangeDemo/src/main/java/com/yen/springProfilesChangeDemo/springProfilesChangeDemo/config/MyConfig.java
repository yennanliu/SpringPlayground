package com.yen.springProfilesChangeDemo.springProfilesChangeDemo.config;

// https://www.youtube.com/watch?v=newMNCS4sik&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=82

import com.yen.springProfilesChangeDemo.springProfilesChangeDemo.bean.Color;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MyConfig {

    /** we can add @Profile annotation over method as well */
    @Profile("prod")
    @Bean
    public Color red(){
        return new Color();
    }

    @Profile("dev")
    @Bean
    public Color blue(){
        return new Color();
    }

}
