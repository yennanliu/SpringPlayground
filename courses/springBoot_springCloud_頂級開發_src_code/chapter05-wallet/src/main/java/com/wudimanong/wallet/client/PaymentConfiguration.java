package com.wudimanong.wallet.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangqiao
 */
@Configuration
public class PaymentConfiguration {

    @Bean
    PaymentClientFallbackFactory paymentClientFallbackFactory() {
        return new PaymentClientFallbackFactory();
    }
}
