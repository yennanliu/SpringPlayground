package com.yen.SpringBootPOC1.config;

// https://www.youtube.com/watch?v=GGWMK2BJs7E&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=9

import com.yen.SpringBootPOC1.bean.Pet;
import com.yen.SpringBootPOC1.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** tell spring boot, this is a config class. same as config file */
@Configuration
public class MyConfig {

    /**
     *  1) add component to container, use method name as component id
     *  2) returned type is component type
     *  3) returned value is component instance in container
     */
    @Bean
    public User user01(){
        return new User("kyo", 18);
    }

    @Bean("tom") // we can modify the component id via "@Bean("tom")"
    public Pet tomcatPer(){
        return new Pet("tomcat");
    }
}
