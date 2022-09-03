package com.wudimanong.access.demo.service;

import com.wudimanong.access.demo.entity.TestShuntEffectBO;
import com.wudimanong.access.demo.entity.TestShuntEffectDTO;

/**
 * @author jiangqiao
 */
public interface AbtestService {

    /**
     * 测试实验分流效果
     *
     * @param testShuntEffectDTO
     * @return
     */
    TestShuntEffectBO testShuntEffect(TestShuntEffectDTO testShuntEffectDTO);

}
