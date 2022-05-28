package com.yen.springBootPOC3.service.impl;

import com.yen.springBootPOC3.dao.AccountDao;
import com.yen.springBootPOC3.entity.Account;
import com.yen.springBootPOC3.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/** book p.141 */

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    @Transactional
    public void transferAccounts(int fromUser, int toUser, float account) {

        Account fromAccount = accountDao.getOne(fromUser);
        fromAccount.setBalance(fromAccount.getBalance() - account);
        accountDao.save(fromAccount);

        Account toAccount = accountDao.getById(toUser);
        toAccount.setBalance(toAccount.getBalance() + account);
        // make some error
        int zero =  1 / 0;
        accountDao.save(toAccount);
    }

}
