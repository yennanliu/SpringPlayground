package com.yen.wallet.client;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/client/PaymentClient.java

import com.yen.wallet.client.bo.UnifiedPayBO;
import com.yen.wallet.client.dto.UnifiedPayDTO;
import com.yen.wallet.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "payment", configuration = PaymentConfiguration.class, fallbackFactory = PaymentClientFallbackFactory.class)
public interface PaymentClient {

    /**
     * 支付微服务统一支付接口客户端定义
     *
     * @param unifiedPayDTO
     * @return
     */
    @PostMapping("/pay/unifiedPay")
    public ResponseResult<UnifiedPayBO> unifiedPay(@RequestBody @Validated UnifiedPayDTO unifiedPayDTO);
}