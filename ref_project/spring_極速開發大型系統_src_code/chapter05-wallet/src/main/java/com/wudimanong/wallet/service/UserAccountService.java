package com.wudimanong.wallet.service;

import com.wudimanong.wallet.entity.bo.AccountBO;
import com.wudimanong.wallet.entity.bo.AccountOpenBO;
import com.wudimanong.wallet.entity.dto.AccountOpenDTO;
import com.wudimanong.wallet.entity.dto.AccountQeuryDTO;
import java.util.List;

/**
 * @author jiangqiao
 */
public interface UserAccountService {

    /**
     * 电子账户开通业务层接口方法
     *
     * @param accountOpenDTO
     * @return
     */
    AccountOpenBO openAcc(AccountOpenDTO accountOpenDTO);

    /**
     * 用户电子账户查询业务层接口方法
     *
     * @param accountQeuryDTO
     * @return
     */
    List<AccountBO> queryAcc(AccountQeuryDTO accountQeuryDTO);
}
