package com.yen.SpringBootPOC1.config;

// https://www.youtube.com/watch?v=GGWMK2BJs7E&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=9

import com.yen.SpringBootPOC1.bean.Pet;
import com.yen.SpringBootPOC1.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  tell spring boot, this is a config class. same as config file
 *
 *  1) we can use @Bean
 *  2) component instance is singleton (單例) by default
 *  3) conf class (MyConfig) is also a component
 *  4) we have
 *          Full (proxyBeanMethods=true)
 *          Lite (proxyBeanMethods=false)
 *      mode in config setting
 *      -> for dealing with component dep.
 */

@Configuration(proxyBeanMethods=true)  // proxyBeanMethods default = true
public class MyConfig {

    /**
     *  1) add component to container, use method name as component id
     *  2) returned type is component type
     *  3) returned value is component instance in container
     *  4) no matter how many times we grab component instance in app,
     *      -> it ALWAYS uses the same registered one (instance) (e.g.  singleton (單例))
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
