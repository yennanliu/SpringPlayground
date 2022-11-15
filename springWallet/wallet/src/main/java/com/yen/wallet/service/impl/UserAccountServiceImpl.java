package com.yen.wallet.service.impl;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/impl/UserAccountServiceImpl.java

import com.yen.wallet.entity.bo.AccountBO;
import com.yen.wallet.entity.bo.AccountOpenBO;
import com.yen.wallet.entity.dto.AccountOpenDTO;
import com.yen.wallet.entity.dto.AccountQueryDTO;
import com.yen.wallet.service.UserAccountService;

import java.util.List;

// TODO : complete it
public class UserAccountServiceImpl implements UserAccountService {
    @Override
    public AccountOpenBO openAcc(AccountOpenDTO accountOpenDTO) {
        return null;
    }

    @Override
    public List<AccountBO> queryAcc(AccountQueryDTO accountQueryDTO) {
        return null;
    }
}
