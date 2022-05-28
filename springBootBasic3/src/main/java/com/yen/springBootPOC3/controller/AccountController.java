package com.yen.springBootPOC3.controller;

import com.yen.springBootPOC3.service.AccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** book p.140 */

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @RequestMapping("/transfer")
    public String transferAccount(){
        try{
            accountService.transferAccounts(1, 2, 200);
            return "ok";
        }catch (Exception e){
            System.out.println("tranfer fail : " + e);
            return "fail";
        }
    }

}
