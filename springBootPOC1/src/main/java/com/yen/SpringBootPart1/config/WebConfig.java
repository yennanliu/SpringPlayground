package com.yen.SpringBootPart1.config;

// https://www.youtube.com/watch?v=QpZEkzjit7o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=27
// https://www.youtube.com/watch?v=2IBSZvwWq5w&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=31

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration(proxyBeanMethods = false)
public class WebConfig {

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        HiddenHttpMethodFilter methodFilter = new HiddenHttpMethodFilter();
        //methodFilter.setMethodParam("_m"); // NOTE !! here we'll override default "_method" to "_m" (in filer). please check index.html
        //methodFilter.setMethodParam("_method");
        return methodFilter;
    }
}
