package com.yen.wallet.convert;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/convert/UserBalanceOrderConvert.java

import com.yen.wallet.client.bo.UnifiedPayBO;
import com.yen.wallet.dao.model.UserBalanceOrderPO;
import com.yen.wallet.entity.bo.AccountChargeBO;
import com.yen.wallet.entity.dto.AccountChargeDTO;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface UserBalanceOrderConvert {

    UserBalanceOrderConvert INSTANCE = Mappers.getMapper(UserBalanceOrderConvert.class);

    /**
     * 充值订单数据生成转换方法
     *
     * @param accountChargeDTO
     * @return
     */
    @Mappings({})
    UserBalanceOrderPO convertUserBalanceOrderPO(AccountChargeDTO accountChargeDTO);

    /**
     * 充值订单业务层返回数据生成转换方法
     *
     * @param unifiedPayBO
     * @return
     */
    @Mappings({})
    AccountChargeBO convertUserBalanceOrderBO(UnifiedPayBO unifiedPayBO);
}