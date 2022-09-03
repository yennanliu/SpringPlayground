package com.wudimanong.wallet.convert;

import com.wudimanong.wallet.dao.model.UserBalancePO;
import com.wudimanong.wallet.entity.bo.AccountBO;
import com.wudimanong.wallet.entity.bo.AccountOpenBO;
import com.wudimanong.wallet.entity.dto.AccountOpenDTO;
import java.util.List;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangqiao
 */
@org.mapstruct.Mapper
public interface UserBalanceConvert {

    UserBalanceConvert INSTANCE = Mappers.getMapper(UserBalanceConvert.class);

    /**
     * 电子账户开通数据传输对象到数据库对象的转换映射
     *
     * @param accountOpenDTO
     * @return
     */
    @Mappings({})
    UserBalancePO convertUserBalancePO(AccountOpenDTO accountOpenDTO);

    /**
     * 根据电子账号信息持久层数据对象转换开户业务层返回参数对象
     *
     * @param userBalancePO
     * @return
     */
    @Mappings({})
    AccountOpenBO convertAccountOpenBO(UserBalancePO userBalancePO);

    /**
     * 根据电子账户持久层数据库对象转换查询业务层返回参数对象
     *
     * @param userBalancePOList
     * @return
     */
    @Mappings({})
    List<AccountBO> convertAccountBO(List<UserBalancePO> userBalancePOList);
}
