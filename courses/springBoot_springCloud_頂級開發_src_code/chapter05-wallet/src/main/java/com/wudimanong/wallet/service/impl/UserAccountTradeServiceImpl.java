package com.wudimanong.wallet.service.impl;

import com.wudimanong.wallet.client.PaymentClient;
import com.wudimanong.wallet.client.bo.UnifiedPayBO;
import com.wudimanong.wallet.client.dto.UnifiedPayDTO;
import com.wudimanong.wallet.convert.UserBalanceOrderConvert;
import com.wudimanong.wallet.dao.mapper.UserBalanceOrderDao;
import com.wudimanong.wallet.dao.model.UserBalanceOrderPO;
import com.wudimanong.wallet.entity.BusinessCodeEnum;
import com.wudimanong.wallet.entity.GlobalCodeEnum;
import com.wudimanong.wallet.entity.ResponseResult;
import com.wudimanong.wallet.entity.bo.AccountChargeBO;
import com.wudimanong.wallet.entity.bo.AddBalanceBO;
import com.wudimanong.wallet.entity.bo.PayNotifyBO;
import com.wudimanong.wallet.entity.dto.AccountChargeDTO;
import com.wudimanong.wallet.entity.dto.PayNotifyDTO;
import com.wudimanong.wallet.entity.enums.TradeType;
import com.wudimanong.wallet.exception.DAOException;
import com.wudimanong.wallet.exception.ServiceException;
import com.wudimanong.wallet.service.UserAccountTradeService;
import com.wudimanong.wallet.service.UserBalanceService;
import com.wudimanong.wallet.utils.DateUtils;
import com.wudimanong.wallet.utils.IDutils;
import com.wudimanong.wallet.utils.SnowFlakeIdGenerator;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Service
public class UserAccountTradeServiceImpl implements UserAccountTradeService {


    @Autowired
    PaymentClient paymentClient;

    /**
     * 充值订单Dao层接口
     */
    @Autowired
    UserBalanceOrderDao userBalanceOrderDao;

    @Override
    public AccountChargeBO chargeOrder(AccountChargeDTO accountChargeDTO) {
        //生成充值订单信息
        UserBalanceOrderPO userBalanceOrderPO = createChargeOrder(accountChargeDTO);
        try {
            userBalanceOrderDao.insert(userBalanceOrderPO);
        } catch (Exception e) {
            //抛出Dao层处理失败异常
            throw new DAOException(BusinessCodeEnum.BUSI_CHARGE_FAIL_2000.getCode(),
                    BusinessCodeEnum.BUSI_CHARGE_FAIL_2000.getDesc(), e);
        }
        //***********调用支付微服务支付接口****************
        //构建支付请求参数
        UnifiedPayDTO unifiedPayDTO = buildUnifiedPayDTO(accountChargeDTO, userBalanceOrderPO);
        ResponseResult<UnifiedPayBO> responseResult = paymentClient.unifiedPay(unifiedPayDTO);
        if (!responseResult.getCode().equals(GlobalCodeEnum.GL_SUCC_0000.getCode())) {
            //支付失败返回错误码
            throw new ServiceException(responseResult.getCode(), responseResult.getMessage());
        }
        //得到支付返回参数
        UnifiedPayBO unifiedPayBO = responseResult.getData();
        //返回生成的充值订单信息
        AccountChargeBO accountChargeBO = UserBalanceOrderConvert.INSTANCE
                .convertUserBalanceOrderBO(unifiedPayBO);
        accountChargeBO.setUserId(accountChargeDTO.getUserId());
        return accountChargeBO;
    }

    /**
     * 用户账户余额服务层接口依赖
     */
    @Autowired
    UserBalanceService userBalanceServiceImpl;

    /**
     * 支付回调逻辑业务层实现方法
     *
     * @param payNotifyDTO
     * @return
     */
    @Override
    public PayNotifyBO receivePayNotify(PayNotifyDTO payNotifyDTO) {
        //查询充值订单判断支付状态是否成功
        Map parmMap = new HashMap<>();
        parmMap.put("order_id", payNotifyDTO.getOrderId());
        List<UserBalanceOrderPO> userBalanceOrderPOList = userBalanceOrderDao.selectByMap(parmMap);
        //充值订单不存在,返回失败结果
        if (userBalanceOrderPOList == null && userBalanceOrderPOList.size() <= 0) {
            return PayNotifyBO.builder().result("fail").build();
        }
        UserBalanceOrderPO userBalanceOrderPO = userBalanceOrderPOList.get(0);
        //这里判断充值订单支付状态，如果已经为成功状态，则说明处理过，无需额外处理，返回成功结果
        if ("2".equals(userBalanceOrderPO.getStatus())) {
            return PayNotifyBO.builder().result("success").build();
        }
        //更新充值订单支付状态为成功
        userBalanceOrderPO.setStatus(String.valueOf(payNotifyDTO.getPayStatus()));
        //设置订单更新时间
        userBalanceOrderPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        //更新状态
        userBalanceOrderDao.updateById(userBalanceOrderPO);
        //如为支付结果成功回调通知，则需要完成用户账户余额的增加
        if (payNotifyDTO.getPayStatus() == 2) {
            AddBalanceBO addBalanceBO = AddBalanceBO.builder().userId(userBalanceOrderPO.getUserId())
                    .amount(userBalanceOrderPO.getAmount()).busiType("charge").accType("0")
                    .currency(userBalanceOrderPO.getCurrency()).build();
            //调用账户余额业务层方法，增加余额
            userBalanceServiceImpl.addBalance(addBalanceBO);
        }
        return PayNotifyBO.builder().result("success").build();
    }

    /**
     * 生成充值订单信息私有方法
     *
     * @param accountChargeDTO
     * @return
     */
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

    /**
     * 以特定规则生成用户充值订单流水号私有方法
     *
     * @return
     */
    private String getOrderId() {
        //雪花算法ID生成器
        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(IDutils.getWorkId(), 1);
        //以日期yyyyMMddHHmmss+随机ID生成器的方式生成充值订单号
        return DateUtils.getStringByFormat(new Date(), DateUtils.sf3) + idGenerator.nextId();
    }

    /**
     * 构建请求支付微服务的统一支付请求参数对象
     *
     * @param userBalanceOrderPO
     * @return
     */
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
