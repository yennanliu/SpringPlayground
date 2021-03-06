package com.yen.springBootPOC2AdminSystem.servlet;

// https://www.youtube.com/watch?v=oi6ChwpC6rc&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=57

/**  Web native component inject (Servlet, Filter, Listener) V2
 *   MyRegistConfig : A config file
 *
 *    1) same purpose as MyFilter, MyServlet, MyServletContextListener
 *    2) NOTE !!!
 *      -> since it's a config file, we need below annotations:
 *          @Configuration
 *          @Bean
 *      ref :  https://github.com/yennanliu/SpringPlayground/blob/main/springBootPOC1/src/main/java/com/yen/SpringBootPart1/config/MyConfig.java
 *
 */

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

//@Configuration(proxyBeanMethods = false) // NOT use this, since we don't want to create a new instance every time
@Configuration(proxyBeanMethods = true) // use this, make sure instance is singleton (單實例)
public class MyRegistConfig {

    @Bean
    public ServletRegistrationBean myServlet(){

        MyServlet myServlet = new MyServlet();
        return new ServletRegistrationBean(myServlet, "/my", "/my01");
    }

    @Bean
    public FilterRegistrationBean myFilter(){

        MyFilter myFilter = new MyFilter();

        // method 1
        //return new FilterRegistrationBean(myFilter, myServlet());

        // method 2
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/my", "/my01"));
        return filterRegistrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean myListener(){
        MyServletContextListener myServletContextListener = new MyServletContextListener();
        return new ServletListenerRegistrationBean(myServletContextListener);
    }

}
