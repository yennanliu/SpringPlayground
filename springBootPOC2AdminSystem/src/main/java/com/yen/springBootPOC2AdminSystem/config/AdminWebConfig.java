package com.yen.springBootPOC2AdminSystem.config;

// https://www.youtube.com/watch?v=PMaonqe9XCU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=49

import com.yen.springBootPOC2AdminSystem.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // intercepted requests ( "/**" means all requests, include static resources (e.g. html, css..))
                .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**", "/js/**");  // passed requests (not through interceptor)

    }

}
