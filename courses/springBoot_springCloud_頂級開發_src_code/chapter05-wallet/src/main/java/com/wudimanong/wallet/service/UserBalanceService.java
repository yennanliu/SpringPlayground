package com.wudimanong.wallet.service;

import com.wudimanong.wallet.entity.bo.AddBalanceBO;

/**
 * @author jiangqiao
 */
public interface UserBalanceService {

    /**
     * 余额增加业务层方法
     *
     * @param addBalanceBO
     * @return
     */
    boolean addBalance(AddBalanceBO addBalanceBO);

}
