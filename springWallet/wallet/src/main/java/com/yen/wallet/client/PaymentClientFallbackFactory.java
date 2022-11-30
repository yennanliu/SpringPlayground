package com.yen.wallet.client;

// book p.5-59, p.5-60
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/client/PaymentClientFallbackFactory.java

import com.yen.wallet.client.bo.UnifiedPayBO;
import com.yen.wallet.client.dto.UnifiedPayDTO;
import com.yen.wallet.entity.enums.BusinessCodeEnum;
import com.yen.wallet.entity.ResponseResult;
import com.yen.wallet.entity.enums.BusinessCodeEnum;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 *  via FallbackFactory, Fallback, we can define further `Circuit breaker` handling logic
 *  https://www.tpisoftware.com/tpu/articleDetails/2781
 */
@Slf4j
public class PaymentClientFallbackFactory implements FallbackFactory<PaymentClient> {

    @Override
    public PaymentClient create(Throwable cause) {
        return new PaymentClient() { // implement PaymentClient here
            @Override
            public ResponseResult<UnifiedPayBO> unifiedPay(UnifiedPayDTO unifiedPayDTO) {
                log.info("支付服务调用降级逻辑处理...");
                log.error(cause.getMessage());
                return ResponseResult.serviceException(BusinessCodeEnum.BUSI_PAY_FAIL_2001.getCode(),
                        BusinessCodeEnum.BUSI_PAY_FAIL_2001.getDesc());
            }
        };
    }

}