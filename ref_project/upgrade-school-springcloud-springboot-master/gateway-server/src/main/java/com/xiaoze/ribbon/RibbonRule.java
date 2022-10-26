package com.xiaoze.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RibbonRule
 * 不能被@ComponentScan扫到
 *
 * @author xiaoze
 * @date 2020/1/8
 */
@Configuration
public class RibbonRule {

    @Bean
    public IRule mySelfRule(){
        return new MyRule();
    }

}
