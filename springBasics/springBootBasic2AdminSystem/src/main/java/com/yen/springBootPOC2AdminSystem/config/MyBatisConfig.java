package com.yen.springBootPOC2AdminSystem.config;

// https://www.youtube.com/watch?v=GWxk85Pk5d8&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=67

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        // this is page Interceptor
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // move back to 1st page if page overflow
        paginationInnerInterceptor.setOverflow(true);
        // set max record each page can show
        paginationInnerInterceptor.setMaxLimit(500L); // infinite : -1
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }

}
