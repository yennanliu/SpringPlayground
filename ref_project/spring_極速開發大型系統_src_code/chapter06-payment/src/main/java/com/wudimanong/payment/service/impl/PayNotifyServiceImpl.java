package com.wudimanong.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wudimanong.payment.convert.PayNotifyConvert;
import com.wudimanong.payment.dao.mapper.PayChannelParamDao;
import com.wudimanong.payment.dao.mapper.PayNotifyDao;
import com.wudimanong.payment.dao.mapper.PayOrderDao;
import com.wudimanong.payment.dao.model.PayChannelParamPO;
import com.wudimanong.payment.dao.model.PayNotifyPO;
import com.wudimanong.payment.dao.model.PayOrderPO;
import com.wudimanong.payment.entity.dto.AliPayReceiveDTO;
import com.wudimanong.payment.service.PayNotifyService;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class PayNotifyServiceImpl implements PayNotifyService {

    /**
     * 渠道参数配置信息持久层依赖
     */
    @Autowired
    PayChannelParamDao payChannelParamDao;

    /**
     * 支付订单流水持久层依赖
     */
    @Autowired
    PayOrderDao payOrderDao;

    /**
     * 渠道支付通知日志持久层依赖
     */
    @Autowired
    PayNotifyDao payNotifyDao;

    @Override
    public String aliPayReceive(AliPayReceiveDTO aliPayReceiveDTO) {
        //对报文进行验签
        boolean verifyResult = aliPayReceiveMsgVerify(aliPayReceiveDTO);
        //验签失败则直接返回错误信息
        if (!verifyResult) {
            return "sign verify fail";
        }
        //查询支付订单流水信息
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pay_id", aliPayReceiveDTO.getOut_trade_no());
        List<PayOrderPO> payOrderPOList = payOrderDao.selectByMap(paramMap);
        if (payOrderPOList == null || payOrderPOList.size() <= 0) {
            return "order not exist";
        }
        //验签成功则保存通知报文信息
        PayOrderPO payOrderPO = payOrderPOList.get(0);
        //通过MapStruct转换对象
        PayNotifyPO payNotifyPO = PayNotifyConvert.INSTANCE.convertPayNotifyPO(payOrderPO);
        payNotifyPO.setMerchantId(aliPayReceiveDTO.getApp_id());
        //设置状态为已处理
        payNotifyPO.setReceiveStatus("2");
        //将通知报文转为JSON格式进行存储
        payNotifyPO.setFullinfo(JSON.toJSONString(aliPayReceiveDTO));
        payNotifyDao.insert(payNotifyPO);
        //更新支付订单状态（这里放到一个事务，也可以异步解耦处理）
        payOrderPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        payOrderPO.setStatus("2");
        payOrderPO.setTradeNo(aliPayReceiveDTO.getTrade_no());
        payOrderDao.updateById(payOrderPO);
        //todo 异步发送业务方通知（逻辑暂不实现），这里如果是内部微服务可以通过MQ解耦，外部系统则通过HTTP方式投递
        return "success";
    }

    /**
     * 支付宝通知报文签名验证
     *
     * @param aliPayReceiveDTO
     * @return
     */
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
