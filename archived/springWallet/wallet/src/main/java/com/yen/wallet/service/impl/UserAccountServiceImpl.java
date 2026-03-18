package com.yen.wallet.service.impl;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/impl/UserAccountServiceImpl.java

import com.yen.wallet.convert.UserBalanceConvert;
import com.yen.wallet.dao.mapper.UserBalanceDao;
import com.yen.wallet.dao.model.UserBalancePO;
import com.yen.wallet.entity.bo.AccountBO;
import com.yen.wallet.entity.bo.AccountOpenBO;
import com.yen.wallet.entity.dto.AccountOpenDTO;
import com.yen.wallet.entity.dto.AccountQueryDTO;
import com.yen.wallet.entity.enums.BusinessCodeEnum;
import com.yen.wallet.exception.ServiceException;
import com.yen.wallet.service.UserAccountService;
import com.yen.wallet.utils.DateUtils;
import com.yen.wallet.utils.IDutils;
import com.yen.wallet.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    UserBalanceDao userBalanceDao;


    @Override
    public AccountOpenBO openAcc(AccountOpenDTO accountOpenDTO) {

        // check if given userID has account with specific type
        Map paramMap = new HashMap<>();
        paramMap.put("user_id", accountOpenDTO.getUserId());
        paramMap.put("acc_type", accountOpenDTO.getAccType());
        List<UserBalancePO> userBalancePOList = userBalanceDao.selectByMap(paramMap);
        if (userBalancePOList != null && userBalancePOList.size() > 0) {
            throw new ServiceException(BusinessCodeEnum.BUSI_ACCOUNT_FAIL_1000.getCode(),
                    BusinessCodeEnum.BUSI_ACCOUNT_FAIL_1000.getDesc());
        }
        // transform DTO obj to PO obj
        UserBalancePO userBalancePO = UserBalanceConvert.INSTANCE.convertUserBalancePO(accountOpenDTO);
        // generated account setup
        String accountNo = getAccountNo();
        userBalancePO.setAccNo(accountNo);
        // set up init account balance
        userBalancePO.setBalance(0);
        // setup time
        userBalancePO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userBalancePO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        // insert PO obj into DB
        userBalanceDao.insert(userBalancePO);
        // encapsulate and return BO obj
        AccountOpenBO accountOpenBO = UserBalanceConvert.INSTANCE.convertAccountOpenBO(userBalancePO);
        return accountOpenBO;
    }

    @Override
    public List<AccountBO> queryAcc(AccountQueryDTO accountQueryDTO) {

        // account query condition setup
        Map paramMap = new HashMap<>();
        paramMap.put("user_id", accountQueryDTO.getUserId());
        if (accountQueryDTO.getAccType() != null) {
            paramMap.put("acc_type", accountQueryDTO.getAccType());
        }
        if (accountQueryDTO.getCurrency() != null && !"".equals(accountQueryDTO.getCurrency())) {
            paramMap.put("currency", accountQueryDTO.getCurrency());
        }
        List<AccountBO> accountBOList = new ArrayList<>();
        List<UserBalancePO> userBalancePOList = userBalanceDao.selectByMap(paramMap);
        if (userBalancePOList != null && userBalancePOList.size() > 0) {
            // transform account PO obj to BO obj
            accountBOList = UserBalanceConvert.INSTANCE.convertAccountBO(userBalancePOList);
        }
        return accountBOList;
    }


    /** private helper methods */
    /** generate account id */
    private String getAccountNo() {
        //雪花算法ID生成器
        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(IDutils.getWorkId(), 1);
        //以日期YYYYMMDD+随机ID生成器的方式产生账户号
        return DateUtils.getStringByFormat(new Date(), DateUtils.sf1) + idGenerator.nextId();
    }

}
