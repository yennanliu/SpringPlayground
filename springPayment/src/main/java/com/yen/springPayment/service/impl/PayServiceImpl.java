package com.yen.springPayment.service.impl;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/main/java/com/wudimanong/payment/service/impl/PayServiceImpl.java

import com.yen.springPayment.convert.UnifiedPayConvert;
import com.yen.springPayment.dao.mapper.PayOrderDao;
import com.yen.springPayment.dao.model.PayOrderPO;
import com.yen.springPayment.exception.ServiceException;
import com.yen.springPayment.service.PayChannelService;
import com.yen.springPayment.service.PayChannelServiceFactory;
import com.yen.springPayment.service.PayService;
import com.yen.springPayment.utils.DateUtils;
import entity.BusinessCodeEnum;
import entity.bo.UnifiedPayBO;
import entity.dto.UnifiedPayDTO;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PayServiceImpl implements PayService {

    /** redis distributed lock prefix */
    public final String redisLockPrefix = "pay-order&";

    /** redis distributed lock dep */
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Autowired
    PayOrderDao payOrderDao;

    @Autowired
    PayChannelServiceFactory payChannelServiceFactory;

    @Override
    public UnifiedPayBO unifiedPay(UnifiedPayDTO unifiedPayDTO) {

        // get data BO obj
        UnifiedPayBO unifiedPayBO = null;

        /** create redis distributed lock
         *
         * -> prefix + order-id : get a redis lock, ONLY allow one order being processed at a time
         */
        Lock lock = redisLockRegistry.obtain(redisLockPrefix + unifiedPayBO.getOrderId());
        log.info(">>> (unifiedPay) lock = {}", lock);

        // wait 1 sec when hold a lock
        boolean isLock = false;
        try{
            isLock = lock.tryLock(1, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        if (isLock){
            // DB order deduplication logic
            boolean isRepeatOrder = isSuccessPayOrder(unifiedPayDTO);
            if (isRepeatOrder){
                throw new ServiceException(BusinessCodeEnum.BUSI_PAY_FAIL_1000.getCode(),
                        BusinessCodeEnum.BUSI_PAY_FAIL_1000.getDesc());
            }
            // save order to DB
            String payId = this.payOrderSave(unifiedPayDTO);
            // get payment channel service
            PayChannelService payChannelService = payChannelServiceFactory
                    .createPayChannelService(unifiedPayDTO.getChannel());
            // call payment channel service, pay with 3rd party
            unifiedPayDTO.setOrderId(payId);
            unifiedPayBO = payChannelService.pay(unifiedPayDTO);

            /** RELEASE distributed redis lock */
            lock.unlock();
        }else{
            // hold lock is expired, means other request is being processing, ask users try later
            throw new ServiceException(BusinessCodeEnum.BUSI_PAY_FAIL_1001.getCode(),
                    BusinessCodeEnum.BUSI_PAY_FAIL_1001.getDesc());
        }
        return unifiedPayBO;
    }

    /** private helper method */

    // method : check if success payment order
    private boolean isSuccessPayOrder(UnifiedPayDTO unifiedPayDTO) {
        Map<String, Object> parm = new HashMap<>();
        parm.put("order_id", unifiedPayDTO.getOrderId());
        List<PayOrderPO> payOrderPOList = payOrderDao.selectByMap(parm); // TODO : double check it
        if (payOrderPOList != null && payOrderPOList.size() > 0) {
            //判断支付订单中是否存在成功状态的订单,存在则不处理新的支付请求
            List<PayOrderPO> successPayOrderList = payOrderPOList.stream()
                    .filter(o -> "2".equals(o.getStatus())).collect(Collectors.toList());
            if (successPayOrderList != null && successPayOrderList.size() > 0) {
                return true;
            }
        }
        return false;
    }

    // method : save order to DB
    private String payOrderSave(UnifiedPayDTO unifiedPayDTO) {
        //MapStruct工具进行实体对象类型转换
        PayOrderPO payOrderPO = UnifiedPayConvert.INSTANCE.convertPayOrderPO(unifiedPayDTO);
        //设置支付状态为"待支付"
        payOrderPO.setStatus("0");
        //生成支付平台流水号
        String payId = createPayId();
        payOrderPO.setPayId(payId);
        //订单创建时间
        payOrderPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        //订单更新时间
        payOrderPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        //订单入库操作
        payOrderDao.insert(payOrderPO);
        return payOrderPO.getPayId();
    }

    // method : create payment order ID
    private String createPayId() {
        //获取10000～99999随机数
        Integer random = new Random().nextInt(99999) % (99999 - 10000 + 1) + 10000;
        //时间戳+随机数
        String payId = DateUtils.getStringByFormat(new Date(), DateUtils.sf3) + String.valueOf(random);
        return payId;
    }

}
