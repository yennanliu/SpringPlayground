package com.wudimanong.wallet.controller;

import com.wudimanong.wallet.entity.ResponseResult;
import com.wudimanong.wallet.entity.bo.AccountBO;
import com.wudimanong.wallet.entity.bo.AccountOpenBO;
import com.wudimanong.wallet.entity.dto.AccountOpenDTO;
import com.wudimanong.wallet.entity.dto.AccountQeuryDTO;
import com.wudimanong.wallet.service.UserAccountService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@Slf4j
@RestController
@RequestMapping("/account")
public class UserAccountController {

    /**
     * Spring依赖注入账户层业务层接口
     */
    @Autowired
    UserAccountService userAccountServiceImpl;

    /**
     * 电子账户开通接口
     *
     * @param accountOpenDTO
     * @return
     */
    @PostMapping("/openAcc")
    public ResponseResult<AccountOpenBO> openAcc(
            @RequestBody @Validated AccountOpenDTO accountOpenDTO) {
        return ResponseResult.OK(userAccountServiceImpl.openAcc(accountOpenDTO));
    }

    /**
     * 电子账户查询接口
     *
     * @param accountQeuryDTO
     * @return
     */
    @GetMapping("/queryAcc")
    public ResponseResult<List<AccountBO>> queryAcc(@Validated AccountQeuryDTO accountQeuryDTO) {
        return ResponseResult.OK(userAccountServiceImpl.queryAcc(accountQeuryDTO));
    }
}
