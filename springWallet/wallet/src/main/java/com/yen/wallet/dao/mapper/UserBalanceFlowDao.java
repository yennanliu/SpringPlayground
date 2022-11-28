package com.yen.wallet.dao.mapper;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/dao/mapper/UserBalanceFlowDao.java

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.wallet.dao.model.UserBalanceFlowPO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBalanceFlowDao extends BaseMapper<UserBalanceFlowPO> {

}