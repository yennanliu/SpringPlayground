package com.yen.wallet.client;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/client/PaymentConfiguration.java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfiguration {

    // TODO : check this method
    @Bean
    PaymentClientFallbackFactory paymentClientFallbackFactory() {
        return new PaymentClientFallbackFactory();
    }

}