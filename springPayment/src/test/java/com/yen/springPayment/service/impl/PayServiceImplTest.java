package com.yen.springPayment.service.impl;

// book p.6-66
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment/src/test/java/com/wudimanong/payment/service/impl/PayServiceImplTest.java

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import com.yen.springPayment.dao.mapper.PayOrderDao;
import com.yen.springPayment.dao.model.PayOrderPO;
import com.yen.springPayment.service.PayChannelService;
import com.yen.springPayment.service.PayChannelServiceFactory;
import com.yen.springPayment.service.PayService;
import entity.dto.UnifiedPayDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PayServiceImpl.class})
@ActiveProfiles("test")
public class PayServiceImplTest {

    // NOTE : both of approaches work (PayService VS PayServiceImpl)
    @Autowired
    PayService payService;
//    @Autowired
//    PayServiceImpl payService;

    // mark Redis distributed lock instance via MockBean
    @MockBean
    private RedisLockRegistry redisLockRegistry;

    @MockBean
    PayOrderDao payOrderDao;

    @MockBean
    PayChannelServiceFactory payChannelServiceFactory;

    @MockBean
    PayChannelService aliPayServiceImpl;

    @Test
    public void unifiedPay(){

        // mock param
        UnifiedPayDTO unifiedPayDTO = new UnifiedPayDTO();
        unifiedPayDTO.setOrderId("2020021417160000001");
        unifiedPayDTO.setAppId("10001");
        unifiedPayDTO.setTradeType("topup");
        unifiedPayDTO.setChannel(1);
        unifiedPayDTO.setPayType("ALI_PAY_H5");
        unifiedPayDTO.setAmount(10);
        unifiedPayDTO.setCurrency("CNY");
        unifiedPayDTO.setUserId("1002");
        unifiedPayDTO.setSubject("xiaomi 10 pro");
        unifiedPayDTO.setBody("xiaomi 10 pro");
        unifiedPayDTO.setNotifyUrl("http://www.baidu.com");
        unifiedPayDTO.setReturnUrl("http://www.baidu.com");

        // mock Redis distributed lock instance logic, for testing
        given(redisLockRegistry.obtain(any(String.class))).willReturn(new Lock() {
            @Override
            public void lock() {

            }

            @Override
            public void lockInterruptibly() throws InterruptedException {

            }

            @Override
            public boolean tryLock() {
                return true;
            }

            @Override
            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                return true;
            }

            @Override
            public void unlock() {

            }

            @Override
            public Condition newCondition() {
                return null;
            }
        });

//        // step 1) Mock consistent layer (DB) and return simulated order instance
//        //given(payOrderDao.selectByMap(any(Map.class))).will(null);
//        given(payOrderDao.selectByMap(any(Map.class))).willReturn(null);
//
//        // step 2) Mock instance created by service factory
//        //given(payChannelServiceFactory.createPayChannelService(any(Integer.class)));
//        given(payChannelServiceFactory.createPayChannelService(any(Integer.class))).willReturn(aliPayServiceImpl);
//
//        // step 3) run unit test code
//        payService.unifiedPay(unifiedPayDTO);
//
//        // step 4) verify if Redis distributed lock is run
//        verify(redisLockRegistry).obtain(any(String.class));
//
//        // step 5) verify if DB query is run
//        verify(payOrderDao.selectByMap(any(Map.class)));
//
//        // step 6) verify order insert to DB is run
//        verify(payOrderDao).insert(any(PayOrderPO.class));
//
//        // step 7) verify factory method is run
//        verify(payChannelServiceFactory).createPayChannelService(any(Integer.class));
//
//        // step 8) verify payment method is run
//        verify(aliPayServiceImpl).pay(any(UnifiedPayDTO.class));

        //1、Mock持久层依赖方法执行时返回支付订单模拟数据对象
        System.out.println("1、Mock持久层依赖方法执行时返回支付订单模拟数据对象");
        given(payOrderDao.selectByMap(any(Map.class))).willReturn(null);

        //2、Mock渠道Service工厂返回渠道处理实例对象
        System.out.println("2、Mock渠道Service工厂返回渠道处理实例对象");
        given(payChannelServiceFactory.createPayChannelService(any(Integer.class))).willReturn(aliPayServiceImpl);

        //3、执行单元测试代码
        System.out.println("3、执行单元测试代码");
        payService.unifiedPay(unifiedPayDTO);

        //4、验证分布式锁获取方法执行过
        System.out.println("4、验证分布式锁获取方法执行过");
        verify(redisLockRegistry).obtain(any(String.class));

        //5、验证数据库查询方法执行过
        System.out.println("5、验证数据库查询方法执行过");
        verify(payOrderDao).selectByMap(any(Map.class));

        //6、验证支付订单入库逻辑被执行过
        System.out.println("6、验证支付订单入库逻辑被执行过");
        verify(payOrderDao).insert(any(PayOrderPO.class));

        //7、验证工厂方法执行过
        System.out.println("7、验证工厂方法执行过");
        verify(payChannelServiceFactory).createPayChannelService(any(Integer.class));

        //8、验证支付方法执行过
        System.out.println("8、验证支付方法执行过");
        verify(aliPayServiceImpl).pay(any(UnifiedPayDTO.class));
    }

}
