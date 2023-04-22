package com.yen.gulimall.ware.config;

// https://youtu.be/L83Bxqy8UEE?t=1199
// https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/backend/EcommerceGuli/gulimall-product/src/main/java/com/yen/gulimall/product/config/MybatisConfig.java

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Bean;

@EnableTransactionManagement
@MapperScan("com.yen.gulimall.ware.dao")
@Configuration
public class WareMyBatisConfig {

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
