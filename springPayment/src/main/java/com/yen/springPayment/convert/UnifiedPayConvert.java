package com.yen.springPayment.convert;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/convert/UnifiedPayConvert.java

import com.yen.springPayment.dao.model.PayOrderPO;
import entity.bo.UnifiedPayBO;
import entity.dto.UnifiedPayDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface UnifiedPayConvert { // TODO : double check it

    UnifiedPayConvert INSTANCE = Mappers.getMapper(UnifiedPayConvert.class);

    /**
     * 支付订单数据生成转换方法
     *
     * @param unifiedPayDTO
     * @return
     */
    @Mappings({@Mapping(target = "extraInfo", ignore = true)})
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