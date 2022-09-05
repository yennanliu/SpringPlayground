package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author longzhonghua
 * @data 2018/11/04 22:30
 */

@Configuration
//過時可以用WebMvcConfigurer
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //設定登入處理動作
        registry.addViewController("/home/login").setViewName("user/login");
        registry.addViewController("/admin/login").setViewName("admin/login");

        registry.addViewController("/jwt/login").setViewName("jwt/login");
       // registry.addViewController("/403").setViewName("error/error");
//        registry.addViewController("/ulogin").setViewName("/sys/ulogin");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和頁面有關的靜態目錄都放在專案的static目錄下 2019.1.5日開啟了,因為背景存取不了這個位置
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //上傳的圖片在D碟下的OTA目錄下，存取路徑如：http://localhost:8081/OTA/d3cf0281-bb7f-40e0-ab77-406db95ccf2c.jpg
        //其中OTA表示存取的前綴。"file:D:/OTA/"是檔案真實的儲存路徑
        registry.addResourceHandler("/UPLOAD/**").addResourceLocations("file:F:/UPLOAD/");
    }
}
