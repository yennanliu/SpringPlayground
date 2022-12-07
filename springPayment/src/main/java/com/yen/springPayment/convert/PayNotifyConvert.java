package com.yen.springPayment.convert;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/convert/PayNotifyConvert.java

import com.yen.springPayment.dao.model.PayNotifyPO;
import com.yen.springPayment.dao.model.PayOrderPO;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;


@org.mapstruct.Mapper
public interface PayNotifyConvert {

    PayNotifyConvert INSTANCE = Mappers.getMapper(PayNotifyConvert.class);

    /**
     * 支付通知报文日志信息转换方法
     *
     * @param payOrderPO
     * @return
     */
    @Mappings({})
    PayNotifyPO convertPayNotifyPO(PayOrderPO payOrderPO);
}