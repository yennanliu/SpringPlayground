package com.yen.springBootPOC2AdminSystem.config;

// https://www.youtube.com/watch?v=PMaonqe9XCU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=49

import com.yen.springBootPOC2AdminSystem.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  1) make a class implements HandlerInterceptor ( com.yen.springBootPOC2AdminSystem.interceptor.LoginInterceptor in this project)
 *  2) register interceptor to container ( implement WebMvcConfigurer and its addInterceptors method)
 *  3) NOTE : Interceptor roles:
 *        - if intercept ALL, then static resources (html, css...) will be intercepted as well
 */
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // intercepted requests ( "/**" means all requests, include static resources (e.g. html, css..))
                .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**", "/js/**");  // method 2) : not intercept static resources in AdminWebConfig // passed requests (not through interceptor)
    }

}
