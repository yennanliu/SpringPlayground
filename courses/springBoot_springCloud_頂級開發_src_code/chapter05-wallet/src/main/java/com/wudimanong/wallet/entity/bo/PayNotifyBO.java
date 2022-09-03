package com.wudimanong.wallet.entity.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@Builder
public class PayNotifyBO {

    /**
     * 接收处理状态，"success-成功；fail-失败"
     */
    private String result;

}
