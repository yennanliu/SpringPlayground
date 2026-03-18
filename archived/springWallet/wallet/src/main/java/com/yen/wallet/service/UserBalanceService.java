package com.yen.wallet.service;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/UserBalanceService.java

import com.yen.wallet.entity.bo.AddBalanceBO;

public interface UserBalanceService {

    /**
     * top up method
     */
    boolean addBalance(AddBalanceBO addBalanceBO);
}
