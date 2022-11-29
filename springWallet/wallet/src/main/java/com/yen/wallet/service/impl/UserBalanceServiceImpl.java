package com.yen.wallet.service.impl;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/impl/UserBalanceServiceImpl.java

import com.yen.wallet.dao.mapper.UserBalanceDao;
import com.yen.wallet.dao.mapper.UserBalanceFlowDao;
import com.yen.wallet.dao.model.UserBalanceFlowPO;
import com.yen.wallet.dao.model.UserBalancePO;
import com.yen.wallet.entity.bo.AddBalanceBO;
import com.yen.wallet.service.UserBalanceService;
import com.yen.wallet.utils.DateUtils;
import com.yen.wallet.utils.IDutils;
import com.yen.wallet.utils.SnowFlakeIdGenerator;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class UserBalanceServiceImpl implements UserBalanceService {

    @Autowired
    UserBalanceDao userBalanceDao;

    @Autowired
    UserBalanceFlowDao userBalanceFlowDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addBalance(AddBalanceBO addBalanceBO) {

        // check updated user account balance
        Map param = new HashMap<>();
        param.put("user_id", addBalanceBO.getUserId());
        param.put("acc_type", addBalanceBO.getAccType());
        List<UserBalancePO> userBalancePOList = userBalanceDao.selectByMap(param);
        UserBalancePO userBalancePO = userBalancePOList.get(0);
        userBalancePO.setBalance(userBalancePO.getBalance() + addBalanceBO.getAmount());
        userBalancePO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userBalanceDao.updateById(userBalancePO);
        // generate account change process records
        UserBalanceFlowPO userBalanceFlowPO = createUserBalanceFlow(addBalanceBO, userBalancePO);
        // insert into DB (PO layer)
        userBalanceFlowDao.insert(userBalanceFlowPO);
        return true;
    }

    /** private help methods */

    /** private method : generate account change records */
    private UserBalanceFlowPO createUserBalanceFlow(AddBalanceBO addBalanceBO, UserBalancePO userBalancePO) {
        UserBalanceFlowPO userBalanceFlowPO = new UserBalanceFlowPO();
        userBalanceFlowPO.setUserId(addBalanceBO.getUserId());
        //设置账户变动流水号
        userBalanceFlowPO.setFlowNo(getFlowId());
        //记录账户号
        userBalanceFlowPO.setAccNo(userBalancePO.getAccNo());
        //记录业务类型
        userBalanceFlowPO.setBusiType(addBalanceBO.getBusiType());
        //记录变动金额
        userBalanceFlowPO.setAmount(addBalanceBO.getAmount());
        //币种
        userBalanceFlowPO.setCurrency(userBalancePO.getCurrency());
        //记录账户变动前金额
        userBalanceFlowPO.setBeginBalance(userBalancePO.getBalance() - addBalanceBO.getAmount());
        //记录账户变动后金额
        userBalanceFlowPO.setEndBalance(userBalancePO.getBalance());
        //借贷方向，借方账户
        userBalanceFlowPO.setFundDirect("00");
        //设置创建时间
        userBalanceFlowPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        //设置更新时间
        userBalanceFlowPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return userBalanceFlowPO;
    }


    /** private method :  generate user account change op id */
    private String getFlowId() {
        //雪花算法ID生成器
        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(IDutils.getWorkId(), 1);
        //以日期yyyyMMddHHmmss+随机ID生成器的方式生成充值订单号
        return DateUtils.getStringByFormat(new Date(), DateUtils.sf3) + idGenerator.nextId();
    }

}
