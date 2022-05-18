package com.yen.springBootPOC2AdminSystem.config;

// https://www.youtube.com/watch?v=PMaonqe9XCU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=49
// https://www.youtube.com/watch?v=UbGHT87dXtU&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=59

import com.yen.springBootPOC2AdminSystem.interceptor.LoginInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *  AdminWebConfig (work with HandlerInterceptor)
 *
 *  1) make a class implements HandlerInterceptor ( com.yen.springBootPOC2AdminSystem.interceptor.LoginInterceptor in this project)
 *  2) register interceptor to container ( implement WebMvcConfigurer and its addInterceptors method)
 *  3) NOTE : Interceptor roles:
 *        - if intercept ALL, then static resources (html, css...) will be intercepted as well
 */


//@EnableWebMvc // take FULLY CONTROL on SpringMVC, may cause some default setting not work. Be careful
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // intercepted requests ( "/**" means all requests, include static resources (e.g. html, css..))
                .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**", "/js/**");  // method 2) : not intercept static resources in AdminWebConfig // passed requests (not through interceptor)
    }

    // custom define default static resources (if @EnableWebMvc)
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        /**
//         *  make all requests to /my_static/** to classpath:/static/ and do matching there
//         */
//        registry.addResourceHandler("/my_static/**")
//                .addResourceLocations("classpath:/static/");
//    }

    //    @Bean
//    public WebMvcRegistrations webMvcRegistrations(){
//        return new WebMvcRegistrations() {
//            @Override
//            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
//                return null;
//            }
//        };
//    }

}
