package com.yen.wallet.service;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/UserAccountService.java

import com.yen.wallet.entity.bo.AccountBO;
import com.yen.wallet.entity.bo.AccountOpenBO;
import com.yen.wallet.entity.dto.AccountOpenDTO;
import com.yen.wallet.entity.dto.AccountQueryDTO;
import java.util.List;

public interface UserAccountService {

    /** open new account */
    AccountOpenBO openAcc(AccountOpenDTO accountOpenDTO);

    /** query account */
    List<AccountBO> queryAcc(AccountQueryDTO accountQueryDTO);
}
