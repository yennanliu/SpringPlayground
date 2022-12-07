package com.yen.springPayment.dao.mapper;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/dao/mapper/PayChannelParamDao.java

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.springPayment.dao.model.PayChannelParamPO;
import org.springframework.stereotype.Repository;

@Repository
public interface PayChannelParamDao extends BaseMapper<PayChannelParamPO> {

}