package com.yen.SpringBootPart1;

// https://www.youtube.com/watch?v=N7dsPcsmxM4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=6
// https://www.youtube.com/watch?v=dJIksiVQDj4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=8
// https://www.youtube.com/watch?v=GGWMK2BJs7E&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=9
// https://www.youtube.com/watch?v=uUpNr3PzNsY&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=10
// https://www.youtube.com/watch?v=AM6wJHknah0&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=10
// https://www.youtube.com/watch?v=lDzXRsOODXA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=16

import com.yen.SpringBootPart1.bean.Pet;
import com.yen.SpringBootPart1.bean.User;
import com.yen.SpringBootPart1.config.MyConfig;
import org.aopalliance.aop.Advice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.context.ConfigurableApplicationContext;

import javax.xml.crypto.Data;
import java.awt.image.DataBuffer;

/**
 *  Main application : entry point
 *  Main conf class
 *
 *  let Spring know that this script is a spring application
 */

@SpringBootApplication(scanBasePackages = "com.yen") // via scanBasePackages = "com.yen" so spring can get WorldController, which is out of default scope
public class MainApplication {

    public static void main(String[] args) {

        // 1, return IOC container
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        System.out.println("--------------------");

        // 2. check above container's content
        String[] names = run.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

        System.out.println("--------------------");

//        // 3. get component from container
//        Pet tom01 = run.getBean("tom", Pet.class);
//        Pet tom02  = run.getBean("tom", Pet.class);
//
//        System.out.println("component tom01 == tom02 ? : " + (tom01 == tom02) );
//
//        System.out.println("--------------------");
//
//        // 4. com.yen.SpringBootPOC1.config.MyConfig$$EnhancerBySpringCGLIB$$3f2ef3de@28cb3a25
//        MyConfig bean = run.getBean(MyConfig.class);
//        System.out.println(bean);
//
//        System.out.println("--------------------");
//
//        // 5. if @Configuration(proxyBeanMethods=true) in Myconfig
//        //    -> will use "proxy" call method
//        //    (spring boot will always check above)
//        //    -> keep component singleton
//        User user = bean.user01();
//        User user1 = bean.user01();
//
//        System.out.println("component user == user1 ? : " + (user == user1) );
//
//        System.out.println("--------------------");
//
//        // 6. get customized import components ( @Import({User.class, DataBuffer.class}) in MyConfig.java)
//        String[] beanNamesForType = run.getBeanNamesForType(User.class);
//        for (String s : beanNamesForType){
//            System.out.println(s);
//        }

//        DBHelper bean1 = run.getBean(DBHelper.class);
//        System.out.println(bean1);

        boolean tom = run.containsBean("tom");
        System.out.println("tom component exists ? " + tom); // false, since we comment @Bean("tom") in MyConfig, so this value is false

        boolean user01 = run.containsBean("user01");
        System.out.println("user01 component exists ? " + user01); // false now, since we have @ConditionalOnBean(name="tom") in MyConfig, and tom component is NOT injected at the moment

        boolean cat = run.containsBean("cat");
        System.out.println("cat = " + cat);

        String[] beanNamesForType= run.getBeanNamesForType(CacheAspectSupport.class);
        System.out.println("beanNamesForType length = " + beanNamesForType.length);

        //SpringApplication.run(MainApplication.class, args);
    }
}
