package com.yen.myrule;

// https://www.youtube.com/watch?v=ipbTc7vYtcQ&list=PLmOn9nNkQxJGVG1ktTV4SedFWuyef_Pi0&index=39

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyselfRule {

    @Bean
    public IRule myRule(){

        // define myRule as RandomRule
        // https://github.com/yennanliu/SpringPlayground/blob/main/springCloud1/doc/pic/ribbon2.png
        return new RandomRule();
    }

}
