package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(MyStarterProperties.class)
/**
 * Description: 當類別路徑classpath下有特殊的類別的情況下進行自動組態
 */
@ConditionalOnClass(MyStarter.class)
/**
 * Description: 組態檔中matchIfMissing =true時進行自動組態
 */
@ConditionalOnProperty(prefix = "spring.mystarter", value = "enabled", matchIfMissing = true)
public class MyStarterServiceAutoConfiguration {
    @Autowired
    private MyStarterProperties myproperties;
    @Bean
    /**
     * Description: 當容器中沒有指定Bean的情況下，自動組態MyStarterService類別
     */
    @ConditionalOnMissingBean(MyStarter.class)
    public MyStarter MyStarterService(){
        MyStarter myStarterService = new MyStarter(myproperties);
        return myStarterService;
    }
}
