package com.yen.wallet.convert;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/convert/UserBalanceConvert.java

import com.yen.wallet.dao.model.UserBalancePO;
import com.yen.wallet.entity.bo.AccountBO;
import com.yen.wallet.entity.bo.AccountOpenBO;
import com.yen.wallet.entity.dto.AccountOpenDTO;
import java.util.List;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface UserBalanceConvert {

    // syntax (mapstruct to transform DTO->PO, PO->BO ...)
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