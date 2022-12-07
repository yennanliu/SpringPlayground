package com.yen.springPayment.service.impl;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/service/impl/AliPayServiceImpl.java

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.yen.springPayment.convert.UnifiedPayConvert;
import com.yen.springPayment.exception.ServiceException;
import com.yen.springPayment.service.PayChannelService;
import entity.BusinessCodeEnum;
import entity.bo.UnifiedPayBO;
import entity.dto.UnifiedPayDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AliPayServiceImpl implements PayChannelService {

    /**
     * 支付接口地址引入
     */
    @Value("${channel.alipay.payUrl}")
    private String payUrl;
    /**
     * 支付宝应用ID
     */
    @Value("${channel.alipay.appId}")
    private String appId;
    /**
     * 支付宝应用私钥
     */
    @Value("${channel.alipay.privateKey}")
    private String privateKey;
    /**
     * 支付宝公钥
     */
    @Value("${channel.alipay.publicKey}")
    private String publicKey;

    private String format = "json";
    private String charset = "UTF-8";
    private String signType = "RSA2";


    @Override
    public UnifiedPayBO pay(UnifiedPayDTO unifiedPayDTO) {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(payUrl, appId, privateKey, format, charset, publicKey,
                signType);
        //创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //在公共参数中设置回跳和通知地址
        alipayRequest.setReturnUrl(unifiedPayDTO.getReturnUrl());
        alipayRequest.setNotifyUrl(alipayRequest.getNotifyUrl());
        //填充业务参数(参考具体支付产品的请求参数要求)
        BizContent bizContent = BizContent.builder().out_trade_no(String.valueOf(unifiedPayDTO.getOrderId()))
                .product_code("FAST_INSTANT_TRADE_PAY").total_amount(Double.valueOf(unifiedPayDTO.getAmount()) / 100)
                .subject(unifiedPayDTO.getSubject()).body(unifiedPayDTO.getBody())
                .passback_params("merchantBizType%" + unifiedPayDTO.getTradeType())
                .build();
        alipayRequest.setBizContent(JSON.toJSONString(bizContent));
        //用户支付宝跳转页面Form表单信息
        String form = "";
        try {
            //调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            //支付渠道错误异常转换封装为系统可识别异常码
            throw new ServiceException(BusinessCodeEnum.BUSI_CHANNEL_FAIL_2000.getCode(),
                    BusinessCodeEnum.BUSI_CHANNEL_FAIL_2000.getDesc(), e);
        }
        //通过MapStruct转换大部分返回参数
        UnifiedPayBO unifiedPayBO = UnifiedPayConvert.INSTANCE.convertUnifiedPayBO(unifiedPayDTO);
        //设置需要接入方处理的form表单数据
        unifiedPayBO.setExtraInfo(form);
        //由于电脑网页支付此时请求并未真正发送至第三方支付渠道，因此这里的TradeNo用接入方订单填充
        unifiedPayBO.setTradeNo(unifiedPayDTO.getOrderId());
        //设置支付状态为待支付
        unifiedPayBO.setPayStatus(0);
        return unifiedPayBO;
    }

    /**
     * 此内部类用于对支付宝业务参数的对象化封装
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class BizContent {

        private String out_trade_no;
        private String product_code;
        private Double total_amount;
        private String subject;
        private String body;
        private String passback_params;
    }
}