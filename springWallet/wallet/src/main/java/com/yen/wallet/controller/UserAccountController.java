package com.yen.wallet.controller;

// book 5-13
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/controller/UserAccountController.java

import java.util.List;

import com.yen.wallet.entity.ResponseResult;
import com.yen.wallet.entity.bo.AccountBO;
import com.yen.wallet.entity.bo.AccountOpenBO;
import com.yen.wallet.entity.dto.AccountOpenDTO;
import com.yen.wallet.entity.dto.AccountQueryDTO;
import com.yen.wallet.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account")
public class UserAccountController {

    @Autowired
    UserAccountService userAccountService;

    /** open new account */
    @PostMapping("/openAcc")
    public ResponseResult<AccountOpenBO> openAcc(@RequestBody @Validated AccountOpenDTO accountOpenDTO){
        return ResponseResult.OK(userAccountService.openAcc(accountOpenDTO));
    }

    /** query account */
    @GetMapping("/queryAcc")
    public ResponseResult<List<AccountBO>> queryAcc(@Validated AccountQueryDTO accountQueryDTO){
        return ResponseResult.OK(userAccountService.queryAcc(accountQueryDTO));
    }

}
