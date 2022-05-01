package com.yen.SpringBootPart1.config;

// https://www.youtube.com/watch?v=QpZEkzjit7o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=27
// https://www.youtube.com/watch?v=2IBSZvwWq5w&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=31

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

@Configuration(proxyBeanMethods = false)
public class WebConfig implements WebMvcConfigurer { // enable MatrixVariable method 2) : implements WebMvcConfigurer

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        HiddenHttpMethodFilter methodFilter = new HiddenHttpMethodFilter();
        //methodFilter.setMethodParam("_m"); // NOTE !! here we'll override default "_method" to "_m" (in filer). please check index.html
        //methodFilter.setMethodParam("_method");
        return methodFilter;
    }

    /** enable MatrixVariable method 1) */
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer(){
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {

                UrlPathHelper urlPathHelper = new UrlPathHelper();

                /** NOTE !! we enable MatrixVariable here by set urlPathHelper.setRemoveSemicolonContent(false) */
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }
        };
    }


    /** enable MatrixVariable method 2) */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

        UrlPathHelper urlPathHelper = new UrlPathHelper();

        /** NOTE !! we enable MatrixVariable here by set setRemoveSemicolonContent = false */
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
}
