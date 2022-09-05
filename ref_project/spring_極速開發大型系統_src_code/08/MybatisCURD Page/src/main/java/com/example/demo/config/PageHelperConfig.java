package com.example.demo.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author longzhonghua
 * @data 2/22/2019 8:51 PM
 */
@Configuration
public class PageHelperConfig {
    @Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        //1.offsetAsPageNum:設定為true時，會將RowBounds第一個參數offset當成pageNum頁碼使用.
        p.setProperty("offsetAsPageNum", "true");
        //2.rowBoundsWithCount:設定為true時，使用RowBounds分頁會進行count查詢.
        p.setProperty("rowBoundsWithCount", "true");
        //3.reasonable：啟用合理化時，若果pageNum<1會查詢第一頁，若果pageNum>pages會查詢最後一頁。
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}