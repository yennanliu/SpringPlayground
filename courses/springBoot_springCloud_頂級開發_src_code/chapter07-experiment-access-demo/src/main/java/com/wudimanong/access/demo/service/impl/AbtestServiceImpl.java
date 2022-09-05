package com.wudimanong.access.demo.service.impl;

import com.wudimanong.access.demo.entity.TestShuntEffectBO;
import com.wudimanong.access.demo.entity.TestShuntEffectDTO;
import com.wudimanong.access.demo.service.AbtestService;
import com.wudimanong.experiment.starter.ExperimentTemplate;
import com.wudimanong.experiment.starter.entity.AbtestInfo;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class AbtestServiceImpl implements AbtestService {

    /**
     * 对照组被调用全局计数器
     */
    public static AtomicInteger testGroupCounter = new AtomicInteger(0);
    /**
     * 实验组被调用全局计数器
     */
    public static AtomicInteger controlGroupCounter = new AtomicInteger(0);

    /**
     * Abtest实验接入SDK组件
     */
    @Autowired
    ExperimentTemplate experimentTemplate;

    @Override
    public TestShuntEffectBO testShuntEffect(TestShuntEffectDTO testShuntEffectDTO) {
        boolean isNewLogic = isNewLogic(testShuntEffectDTO.getUid());
        if (isNewLogic) {
            //增加计数次数
            controlGroupCounter.getAndIncrement();
            log.info("执行新逻辑->实验组执行");
        } else {
            log.info("执行老逻辑->对照组执行");
            //增加计数次数
            testGroupCounter.getAndIncrement();
        }
        return TestShuntEffectBO.builder().uid(testShuntEffectDTO.getUid()).isNewLogic(isNewLogic)
                .testGroupCounter(testGroupCounter.get()).controlGroupCounter(controlGroupCounter.get()).build();
    }

    /**
     * 判断是否执行新逻辑（实验组）
     *
     * @param uid
     * @return
     */
    private Boolean isNewLogic(Long uid) {
        //获取实验配置
        AbtestInfo abtestInfo = experimentTemplate.get("experiment_access_demo_2513", String.valueOf(uid));
        //判断流量匹配分组如果为实验组，流量走新逻辑，否则走老逻辑
        if (abtestInfo.isAbtest()) {
            return true;
        }
        return false;
    }
}
