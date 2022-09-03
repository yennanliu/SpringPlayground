package com.wudimanong.wallet.convert;

import com.wudimanong.wallet.client.bo.UnifiedPayBO;
import com.wudimanong.wallet.dao.model.UserBalanceOrderPO;
import com.wudimanong.wallet.entity.bo.AccountChargeBO;
import com.wudimanong.wallet.entity.dto.AccountChargeDTO;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangqiao
 */
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
