package com.wudimanong.wallet.client;

import com.wudimanong.wallet.client.bo.UnifiedPayBO;
import com.wudimanong.wallet.client.dto.UnifiedPayDTO;
import com.wudimanong.wallet.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author jiangqiao
 */
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
