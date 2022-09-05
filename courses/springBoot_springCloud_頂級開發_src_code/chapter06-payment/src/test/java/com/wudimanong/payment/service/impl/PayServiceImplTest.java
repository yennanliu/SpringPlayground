package com.wudimanong.payment.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.wudimanong.payment.dao.mapper.PayOrderDao;
import com.wudimanong.payment.dao.model.PayOrderPO;
import com.wudimanong.payment.entity.dto.UnifiedPayDTO;
import com.wudimanong.payment.service.PayChannelService;
import com.wudimanong.payment.service.PayChannelServiceFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jiangqiao
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PayServiceImpl.class})
@ActiveProfiles("test")
public class PayServiceImplTest {

    /**
     * 目标测试代码类的实例依赖
     */
    @Autowired
    PayServiceImpl payServiceImpl;

    /**
     * 通过Mockito框架注解Mock(模拟)Redis分布式锁依赖对象
     */
    @MockBean
    private RedisLockRegistry redisLockRegistry;

    /**
     * Mock支付订单持久层依赖接口
     */
    @MockBean
    PayOrderDao payOrderDao;

    /**
     * Mock渠道处理工厂类对象
     */
    @MockBean
    PayChannelServiceFactory payChannelServiceFactory;

    /**
     * Mock支付宝渠道处理类对象
     */
    @MockBean
    PayChannelService aliPayServiceImpl;

    /**
     * 统一支付单元测试代码
     */
    @Test
    public void unifiedPay() {
        //Mock请求参数对象
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

        //Mock Redis分布式锁Mock对象行为，以便逻辑走通
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

        //1、Mock持久层依赖方法执行时返回支付订单模拟数据对象
        given(payOrderDao.selectByMap(any(Map.class))).willReturn(null);

        //2、Mock渠道Service工厂返回渠道处理实例对象
        given(payChannelServiceFactory.createPayChannelService(any(Integer.class))).willReturn(aliPayServiceImpl);

        //3、执行单元测试代码
        payServiceImpl.unifiedPay(unifiedPayDTO);
        //4、验证分布式锁获取方法执行过
        verify(redisLockRegistry).obtain(any(String.class));
        //5、验证数据库查询方法执行过
        verify(payOrderDao).selectByMap(any(Map.class));
        //6、验证支付订单入库逻辑被执行过
        verify(payOrderDao).insert(any(PayOrderPO.class));
        //7、验证工厂方法执行过
        verify(payChannelServiceFactory).createPayChannelService(any(Integer.class));
        //8、验证支付方法执行过
        verify(aliPayServiceImpl).pay(any(UnifiedPayDTO.class));
    }
}
