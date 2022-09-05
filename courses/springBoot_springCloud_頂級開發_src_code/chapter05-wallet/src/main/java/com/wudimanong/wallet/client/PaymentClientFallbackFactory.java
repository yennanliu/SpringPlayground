package com.wudimanong.wallet.client;

import com.wudimanong.wallet.client.bo.UnifiedPayBO;
import com.wudimanong.wallet.client.dto.UnifiedPayDTO;
import com.wudimanong.wallet.entity.BusinessCodeEnum;
import com.wudimanong.wallet.entity.ResponseResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangqiao
 */
@Slf4j
public class PaymentClientFallbackFactory implements FallbackFactory<PaymentClient> {

    @Override
    public PaymentClient create(Throwable cause) {
        return new PaymentClient() {
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
