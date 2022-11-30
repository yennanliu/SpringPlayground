package com.yen.wallet.service.impl;

// book p. 5-18
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/java/com/wudimanong/wallet/service/impl/UserAccountTradeServiceImpl.java

import com.yen.wallet.client.PaymentClient;
import com.yen.wallet.client.bo.UnifiedPayBO;
import com.yen.wallet.client.dto.UnifiedPayDTO;
import com.yen.wallet.convert.UserBalanceOrderConvert;
import com.yen.wallet.dao.mapper.UserBalanceOrderDao;
import com.yen.wallet.dao.model.UserBalanceOrderPO;
import com.yen.wallet.entity.ResponseResult;
import com.yen.wallet.entity.bo.AccountChargeBO;
import com.yen.wallet.entity.bo.AddBalanceBO;
import com.yen.wallet.entity.bo.PayNotifyBO;
import com.yen.wallet.entity.dto.AccountChargeDTO;
import com.yen.wallet.entity.dto.PayNotifyDTO;
import com.yen.wallet.entity.enums.BusinessCodeEnum;
import com.yen.wallet.entity.enums.GlobalCodeEnum;
import com.yen.wallet.entity.enums.TradeType;
import com.yen.wallet.exception.DAOException;
import com.yen.wallet.exception.ServiceException;
import com.yen.wallet.service.UserAccountTradeService;
import com.yen.wallet.service.UserBalanceService;
import com.yen.wallet.utils.DateUtils;
import com.yen.wallet.utils.IDutils;
import com.yen.wallet.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserAccountTradeServiceImpl implements UserAccountTradeService {

    @Autowired
    PaymentClient paymentClient;

    @Autowired
    UserBalanceOrderDao userBalanceOrderDao;

    @Autowired
    UserBalanceService userBalanceService;

    /** charge order method */
    @Override
    public AccountChargeBO chargeOrder(AccountChargeDTO accountChargeDTO) {
        // generate charge order info
        UserBalanceOrderPO userBalanceOrderPO = createChargeOrder(accountChargeDTO);
        try{
            userBalanceOrderDao.insert(userBalanceOrderPO);
        }catch (Exception e){
            // throw DAO layer op exception
            throw new DAOException(BusinessCodeEnum.BUSI_CHARGE_FAIL_2000.getCode(),
                    BusinessCodeEnum.BUSI_CHARGE_FAIL_2000.getDesc(), e);
        }
        /** call micro-service payment API endpoint */
        // construct request param
        UnifiedPayDTO unifiedPayDTO = buildUnifiedPayDTO(accountChargeDTO, userBalanceOrderPO);
        ResponseResult<UnifiedPayBO> responseResult = paymentClient.unifiedPay(unifiedPayDTO);
        if (! responseResult.getCode().equals(GlobalCodeEnum.GL_SUCC_0000.getCode())){
            // return error code
            throw new ServiceException(responseResult.getCode(), responseResult.getMessage());
        }
        // get payment response with success status
        UnifiedPayBO unifiedPayBO = responseResult.getData();
        // return charge order info
        AccountChargeBO accountChargeBO = UserBalanceOrderConvert.INSTANCE.convertUserBalanceOrderBO(unifiedPayBO);
        accountChargeBO.setUserId(accountChargeDTO.getUserId());
        return accountChargeBO;
    }

    /** user account balance check method */
    @Override
    public PayNotifyBO receivePayNotify(PayNotifyDTO payNotifyDTO) {
        // check whether charge order is success
        Map paramMap = new HashMap<>();
        paramMap.put("order_id", payNotifyDTO.getOrderId());
        List<UserBalanceOrderPO> userBalanceOrderPOList = userBalanceOrderDao.selectByMap(paramMap);
        // if order not existed, return failed result
        if (userBalanceOrderPOList == null && userBalanceOrderPOList.size() <= 0){
            return PayNotifyBO.builder().result("fail").build();
        }
        UserBalanceOrderPO userBalanceOrderPO = userBalanceOrderPOList.get(0); // TODO : double check it
        // check charge order status, if already in success status, then this order already processed, no need to do extra op, return success directly
        if ("2".equals(userBalanceOrderPO.getStatus())){
            return PayNotifyBO.builder().result("success").build();
        }
        // update order to success status
        userBalanceOrderPO.setStatus(String.valueOf(payNotifyDTO.getPayStatus()));
        // update order updated time
        userBalanceOrderPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        // update status
        userBalanceOrderDao.updateById(userBalanceOrderPO);
        // if payment success, then have to update user balance
        if (payNotifyDTO.getPayStatus() == 2){
            AddBalanceBO addBalanceBO = AddBalanceBO
                    .builder()
                    .userId(userBalanceOrderPO.getUserId())
                    .amount(userBalanceOrderPO.getAmount()).busiType("charge").accType("0")
                    .currency(userBalanceOrderPO.getCurrency()).build();

            // update balance
            userBalanceService.addBalance(addBalanceBO);
        }

        return PayNotifyBO.builder().result("success").build();
    }

    /** helper methods */
    /** private method : create charge order */
    private UserBalanceOrderPO createChargeOrder(AccountChargeDTO accountChargeDTO) {
        UserBalanceOrderPO userBalanceOrderPO = UserBalanceOrderConvert.INSTANCE
                .convertUserBalanceOrderPO(accountChargeDTO);
        //生成充值订单流水号
        String orderId = getOrderId();
        userBalanceOrderPO.setOrderId(orderId);
        //设置交易类型为充值
        userBalanceOrderPO.setTradeType(TradeType.CHARGE.getCode());
        //设置支付状态为待支付
        userBalanceOrderPO.setStatus("0");
        //设置交易时间
        userBalanceOrderPO.setTradeTime(new Timestamp(System.currentTimeMillis()));
        //设置订单创建时间
        userBalanceOrderPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        //设置订单初始更新时间
        userBalanceOrderPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return userBalanceOrderPO;
    }

    /** private method : generate order id */
    private String getOrderId() {
        //雪花算法ID生成器
        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(IDutils.getWorkId(), 1);
        //以日期yyyyMMddHHmmss+随机ID生成器的方式生成充值订单号
        return DateUtils.getStringByFormat(new Date(), DateUtils.sf3) + idGenerator.nextId();
    }

    /** private method : create request make a payment order */
    private UnifiedPayDTO buildUnifiedPayDTO(AccountChargeDTO accountChargeDTO, UserBalanceOrderPO userBalanceOrderPO) {
        UnifiedPayDTO unifiedPayDTO = new UnifiedPayDTO();
        //支付微服务为接入方分配的应用ID
        unifiedPayDTO.setAppId("10001");
        //支付业务订单号
        unifiedPayDTO.setOrderId(userBalanceOrderPO.getOrderId());
        //充值交易类型-余额充值
        unifiedPayDTO.setTradeType("topup");
        //支付渠道
        unifiedPayDTO.setChannel(accountChargeDTO.getPaymentType());
        //具体的支付渠道方式，可根据接入的支付产品设定
        unifiedPayDTO.setPayType("ALI_PAY_H5");
        //支付金额
        unifiedPayDTO.setAmount(accountChargeDTO.getAmount());
        //支付币种
        unifiedPayDTO.setCurrency(accountChargeDTO.getCurrency());
        //商户用户标识
        unifiedPayDTO.setUserId(String.valueOf(accountChargeDTO.getUserId()));
        //商品标题，实际情况下根据所购买的商品定义相关内容
        unifiedPayDTO.setSubject("xiaomi 10 pro");
        //商品详情
        unifiedPayDTO.setBody("xiaomi 10 pro testing");
        //支付结果异步通知地址，根据实际情况填充，这里为假的测试地址
        unifiedPayDTO.setNotifyUrl("http://www.baidu.com");
        //支付结果同步返回地址，一般为用户前端页面，根据实际情况填充，这里为假的测试地址
        unifiedPayDTO.setReturnUrl("http://www.baidu.com");
        return unifiedPayDTO;
    }

}
