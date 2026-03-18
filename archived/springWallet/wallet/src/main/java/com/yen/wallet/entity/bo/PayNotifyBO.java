package com.yen.wallet.entity.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/entity/bo/PayNotifyBO.java

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayNotifyBO {

    /**
     * 接收处理状态，"success-成功；fail-失败"
     */
    private String result;
}