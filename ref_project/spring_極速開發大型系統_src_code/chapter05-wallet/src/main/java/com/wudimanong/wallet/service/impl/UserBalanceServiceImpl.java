package com.wudimanong.wallet.service.impl;

import com.wudimanong.wallet.dao.mapper.UserBalanceDao;
import com.wudimanong.wallet.dao.mapper.UserBalanceFlowDao;
import com.wudimanong.wallet.dao.model.UserBalanceFlowPO;
import com.wudimanong.wallet.dao.model.UserBalancePO;
import com.wudimanong.wallet.entity.bo.AddBalanceBO;
import com.wudimanong.wallet.service.UserBalanceService;
import com.wudimanong.wallet.utils.DateUtils;
import com.wudimanong.wallet.utils.IDutils;
import com.wudimanong.wallet.utils.SnowFlakeIdGenerator;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class UserBalanceServiceImpl implements UserBalanceService {

    @Autowired
    UserBalanceDao userBalanceDao;

    @Autowired
    UserBalanceFlowDao userBalanceFlowDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addBalance(AddBalanceBO addBalanceBO) {
        //查询更新用户账户余额
        Map param = new HashMap<>();
        param.put("user_id", addBalanceBO.getUserId());
        param.put("acc_type", addBalanceBO.getAccType());
        List<UserBalancePO> userBalancePOList = userBalanceDao.selectByMap(param);
        UserBalancePO userBalancePO = userBalancePOList.get(0);
        userBalancePO.setBalance(userBalancePO.getBalance() + addBalanceBO.getAmount());
        userBalancePO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userBalanceDao.updateById(userBalancePO);
        //生成账户变动流程记录
        UserBalanceFlowPO userBalanceFlowPO = createUserBalanceFlow(addBalanceBO, userBalancePO);
        //持久层入库处理
        userBalanceFlowDao.insert(userBalanceFlowPO);
        return true;
    }

    /**
     * 生成账户变动流水记录方法
     *
     * @param addBalanceBO
     * @param userBalancePO
     * @return
     */
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

    /**
     * 以特定规则生成账户变动流水号私有方法
     *
     * @return
     */
    private String getFlowId() {
        //雪花算法ID生成器
        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(IDutils.getWorkId(), 1);
        //以日期yyyyMMddHHmmss+随机ID生成器的方式生成充值订单号
        return DateUtils.getStringByFormat(new Date(), DateUtils.sf3) + idGenerator.nextId();
    }
}
