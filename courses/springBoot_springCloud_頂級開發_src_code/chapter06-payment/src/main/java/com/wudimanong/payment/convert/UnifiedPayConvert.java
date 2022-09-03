package com.wudimanong.payment.convert;

import com.wudimanong.payment.dao.model.PayOrderPO;
import com.wudimanong.payment.entity.bo.UnifiedPayBO;
import com.wudimanong.payment.entity.dto.UnifiedPayDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangqiao
 */
@org.mapstruct.Mapper
public interface UnifiedPayConvert {

    UnifiedPayConvert INSTANCE = Mappers.getMapper(UnifiedPayConvert.class);

    /**
     * 支付订单数据生成转换方法
     *
     * @param unifiedPayDTO
     * @return
     */
    @Mappings({
            @Mapping(target = "extraInfo", ignore = true)
    })
    UnifiedPayBO convertUnifiedPayBO(UnifiedPayDTO unifiedPayDTO);

    /**
     * 支付参数对象转换为支付订单持久层实体类
     *
     * @param unifiedPayDTO
     * @return
     */
    @Mappings({})
    PayOrderPO convertPayOrderPO(UnifiedPayDTO unifiedPayDTO);

}
