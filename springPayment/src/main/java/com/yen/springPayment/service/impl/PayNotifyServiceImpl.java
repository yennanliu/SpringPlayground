package com.yen.springPayment.service.impl;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/service/impl/PayNotifyServiceImpl.java


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yen.springPayment.convert.PayNotifyConvert;
import com.yen.springPayment.dao.mapper.PayChannelParamDao;
import com.yen.springPayment.dao.mapper.PayNotifyDao;
import com.yen.springPayment.dao.mapper.PayOrderDao;
import com.yen.springPayment.dao.model.PayChannelParamPO;
import com.yen.springPayment.dao.model.PayNotifyPO;
import com.yen.springPayment.dao.model.PayOrderPO;
import com.yen.springPayment.service.PayNotifyService;
import entity.dto.AliPayReceiveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PayNotifyServiceImpl implements PayNotifyService {

    @Autowired
    PayChannelParamDao payChannelParamDao;

    @Autowired
    PayOrderDao payOrderDao;

    @Autowired
    PayNotifyDao payNotifyDao;

    @Override
    public String aliPayReceive(AliPayReceiveDTO aliPayReceiveDTO) {

        // sign verification
        boolean verifyResult = aliPayReceiveMsgVerify(aliPayReceiveDTO);
        if (!verifyResult){
            return "sign verification failed";
        }

        // get payment order info
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pay_id", aliPayReceiveDTO.getOut_trade_no());
        List<PayOrderPO> payOrderPOList = payOrderDao.selectByMap(paramMap);
        if (payOrderPOList == null || payOrderPOList.size() <= 0){
            return "order not existed";
        }

        // verification success, then save notification msg
        PayOrderPO payOrderPO = payOrderPOList.get(0);
        // transform obj via MapStruct // TODO : check it
        PayNotifyPO payNotifyPO = PayNotifyConvert.INSTANCE.convertPayNotifyPO(payOrderPO);
        payNotifyPO.setMerchantId(aliPayReceiveDTO.getApp_id());
        // set status as "processed"
        payNotifyPO.setReceiveStatus("2");
        // transform msg to json and save to DB
        payNotifyPO.setFullinfo(JSON.toJSONString(aliPayReceiveDTO));
        payNotifyDao.insert(payNotifyPO);
        // update order status (put in to DB transaction) (can decouple it as well)
        payOrderPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        payOrderPO.setStatus("2");
        payOrderPO.setTradeNo(aliPayReceiveDTO.getTrade_no());
        payOrderDao.updateById(payOrderPO);
        // TODO : send async notification to users (not yet implemented) (can use RabbitMQ for internal service, for external, can use HTTP)
        return "success";
    }

    /** private helper method */

    // verify Ali pay msg
    private boolean aliPayReceiveMsgVerify(AliPayReceiveDTO aliPayReceiveDTO) {
        //查询支付宝RSA公钥信息
        QueryWrapper<PayChannelParamPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wq -> wq.eq("partner", aliPayReceiveDTO.getApp_id()))
                .and(wq -> wq.eq("status", "0")).and(wq -> wq.eq("key_type", "publickey"));
        PayChannelParamPO payChannelParamPO = payChannelParamDao.selectOne(queryWrapper);
        //参数信息不存在
        if (payChannelParamPO == null) {
            return false;
        }
        //将参数对象转换为Map
        Map<String, String> paramMap = JSON.parseObject(JSON.toJSONString(aliPayReceiveDTO), Map.class);
        //调用SDK验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature
                    .rsaCheckV1(paramMap, payChannelParamPO.getKeyContext(), "UTF-8", payChannelParamPO.getSignType());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //模拟时由于需要支付宝私钥签名，为便于测试这里默认返回验签成功
        return true;
    }

}
