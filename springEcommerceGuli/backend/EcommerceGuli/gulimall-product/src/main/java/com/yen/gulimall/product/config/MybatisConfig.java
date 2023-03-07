package com.yen.gulimall.product.config;

// https://youtu.be/dG2Bo8noDtY?t=103

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement // enable transaction (事務性)
@MapperScan("com.yen.gulimall.product.dao")
public class MybatisConfig {

    // import paging plugin : https://youtu.be/dG2Bo8noDtY?t=152
    @Bean
    public PaginationInterceptor paginationInterceptor(){

        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // if page > max page size, true: back to homepage, false: continue
        paginationInterceptor.setOverflow(true);
        // max record in single page
        paginationInterceptor.setLimit(500);

        return paginationInterceptor;
    }

}
