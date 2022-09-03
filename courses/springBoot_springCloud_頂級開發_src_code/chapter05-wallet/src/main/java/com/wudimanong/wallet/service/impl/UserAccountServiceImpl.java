package com.wudimanong.wallet.service.impl;

import com.wudimanong.wallet.convert.UserBalanceConvert;
import com.wudimanong.wallet.dao.mapper.UserBalanceDao;
import com.wudimanong.wallet.dao.model.UserBalancePO;
import com.wudimanong.wallet.entity.BusinessCodeEnum;
import com.wudimanong.wallet.entity.bo.AccountBO;
import com.wudimanong.wallet.entity.bo.AccountOpenBO;
import com.wudimanong.wallet.entity.dto.AccountOpenDTO;
import com.wudimanong.wallet.entity.dto.AccountQeuryDTO;
import com.wudimanong.wallet.exception.ServiceException;
import com.wudimanong.wallet.service.UserAccountService;
import com.wudimanong.wallet.utils.DateUtils;
import com.wudimanong.wallet.utils.IDutils;
import com.wudimanong.wallet.utils.SnowFlakeIdGenerator;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

    /**
     * 依赖注入Dao层接口
     */
    @Autowired
    UserBalanceDao userBalanceDao;

    @Override
    public AccountOpenBO openAcc(AccountOpenDTO accountOpenDTO) {
        //数据库判断同一用户ID下是否存在同一类型账户
        Map paramMap = new HashMap<>();
        paramMap.put("user_id", accountOpenDTO.getUserId());
        paramMap.put("acc_type", accountOpenDTO.getAccType());
        List<UserBalancePO> userBalancePOList = userBalanceDao.selectByMap(paramMap);
        if (userBalancePOList != null && userBalancePOList.size() > 0) {
            throw new ServiceException(BusinessCodeEnum.BUSI_ACCOUNT_FAIL_1000.getCode(),
                    BusinessCodeEnum.BUSI_ACCOUNT_FAIL_1000.getDesc());
        }
        //将接口业务层对象转换为数据库对象
        UserBalancePO userBalancePO = UserBalanceConvert.INSTANCE.convertUserBalancePO(accountOpenDTO);
        //生成账户号并设置
        String accountNo = getAccountNo();
        userBalancePO.setAccNo(accountNo);
        //设置初始账号余额
        userBalancePO.setBalance(0);
        //设置时间值
        userBalancePO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userBalancePO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        //持久层组件将账号信息写入数据库
        userBalanceDao.insert(userBalancePO);
        //封装返回BO对象数据
        AccountOpenBO accountOpenBO = UserBalanceConvert.INSTANCE.convertAccountOpenBO(userBalancePO);
        return accountOpenBO;
    }

    @Override
    public List<AccountBO> queryAcc(AccountQeuryDTO accountQeuryDTO) {
        //账户查询条件组装
        Map paramMap = new HashMap<>();
        paramMap.put("user_id", accountQeuryDTO.getUserId());
        if (accountQeuryDTO.getAccType() != null) {
            paramMap.put("acc_type", accountQeuryDTO.getAccType());
        }
        if (accountQeuryDTO.getCurrency() != null && !"".equals(accountQeuryDTO.getCurrency())) {
            paramMap.put("currency", accountQeuryDTO.getCurrency());
        }
        List<AccountBO> accountBOList = new ArrayList<>();
        List<UserBalancePO> userBalancePOList = userBalanceDao.selectByMap(paramMap);
        if (userBalancePOList != null && userBalancePOList.size() > 0) {
            //完成账户数据库对象到业务输出对象的转换
            accountBOList = UserBalanceConvert.INSTANCE.convertAccountBO(userBalancePOList);
        }
        return accountBOList;
    }

    /**
     * 生成余额账户号
     */
    private String getAccountNo() {
        //雪花算法ID生成器
        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(IDutils.getWorkId(), 1);
        //以日期YYYYMMDD+随机ID生成器的方式产生账户号
        return DateUtils.getStringByFormat(new Date(), DateUtils.sf1) + idGenerator.nextId();
    }
}
