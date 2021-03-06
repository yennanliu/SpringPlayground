package com.yen.SpringBootPart1.config;

// https://www.youtube.com/watch?v=GGWMK2BJs7E&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=9
// https://www.youtube.com/watch?v=uUpNr3PzNsY&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=10
// https://www.youtube.com/watch?v=odQ6d1xtFKQ&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=12

import com.yen.SpringBootPart1.bean.Car;
import com.yen.SpringBootPart1.bean.Pet;
import com.yen.SpringBootPart1.bean.User;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 *  let spring boot know : this is a config class. same as config file
 *
 *  1) we can use @Bean
 *  2) component instance is a singleton (單例) by default
 *  3) conf class (MyConfig) is also a component
 *  4) we have below mode in config setting:
 *
 *          Full (proxyBeanMethods=true)
 *          Lite (proxyBeanMethods=false)
 *
 *      -> for dealing with component dep.
 *
 *      -> so
 *            if (component) dependents on other components ->  Full (proxyBeanMethods=true)
 *            else -> Lite (proxyBeanMethods=false)  (make spring init faster)
 *
 *  5) @Import can  (e.g. @Import({User.class, DataBuffer.class}))
 *      -> import classes' and init their instances
 *      -> default component name is class name
 *
 *  6) via @ImportResource("classpath:beans.xml"),
 *     we can import config from beans.xml to whatever file (here import to MyConfig.java)
 *
 */

@Import({User.class})
@Configuration(proxyBeanMethods=true)  // proxyBeanMethods default = true
//@ConditionalOnBean(name="tom") // we can also put this condition at class level, so (this condition) will be implemented to whole class scope
//@ConditionalOnMissingBean(name="tom") // inverse condition (with above)
@ImportResource("classpath:beans.xml") // import config from beans.xml
@EnableConfigurationProperties(Car.class) // 1) config biding (Car) 2) auto register component (Car) to container
public class MyConfig {

    /**
     *  1) add component to container, use method name as component id
     *  2) returned type is component type
     *  3) returned value is component instance in container
     *  4) no matter how many times we grab component instance in app,
     *      -> it ALWAYS uses the SAME registered one instance (due to singleton (單例))
     */
    @ConditionalOnBean(name="tom")  // NOTE !! : ONLY if tom component exists, then inject user01 component to the container
    @Bean
    public User user01(){
        return new User("kyo", 18);
    }

    //@Bean("tom") // we can modify the component id via "@Bean("tom")"
    public Pet tomcatPet(){
        return new Pet("tomcat");
    }
}
