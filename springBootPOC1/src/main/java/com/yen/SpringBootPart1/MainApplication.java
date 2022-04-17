package com.yen.SpringBootPart1;

// https://www.youtube.com/watch?v=N7dsPcsmxM4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=6
// https://www.youtube.com/watch?v=dJIksiVQDj4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=8
// https://www.youtube.com/watch?v=GGWMK2BJs7E&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=9

import com.yen.SpringBootPart1.bean.Pet;
import com.yen.SpringBootPart1.bean.User;
import com.yen.SpringBootPart1.config.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *  Main application : entry point
 *
 *  let Spring knows that this script is a spring application
 */

@SpringBootApplication(scanBasePackages = "com.yen")
public class MainApplication {

    public static void main(String[] args) {

        // 1, return IOC container
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        // 2. check above container's content
        String[] names = run.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

        // 3. get component from container
        Pet tom01 = run.getBean("tom", Pet.class);
        Pet tom02  = run.getBean("tom", Pet.class);

        System.out.println("component tom01 == tom02 ? : " + (tom01 == tom02) );

        // 4. com.yen.SpringBootPOC1.config.MyConfig$$EnhancerBySpringCGLIB$$3f2ef3de@28cb3a25
        MyConfig bean = run.getBean(MyConfig.class);
        System.out.println(bean);

        // 5. if @Configuration(proxyBeanMethods=true) in Myconfig
        //    -> will use "proxy" call method
        //    (spring boot will always check above)
        //    -> keep component singleton
        User user = bean.user01();
        User user1 = bean.user01();

        System.out.println("component user == user1 ? : " + (user == user1) );

        //SpringApplication.run(MainApplication.class, args);
    }
}
