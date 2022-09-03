package com.wudimanong.experiment.starter.feign;

import com.wudimanong.experiment.client.entity.bo.ConfigBO;
import java.util.Optional;

/**
 * @author jiangqiao
 */
public class ExperimentFeignSource {

    /**
     * 实验微服务Feign接口组件
     */
    private ExperimentFeignClient experimentFeignClient;

    public ExperimentFeignSource(ExperimentFeignClient experimentFeignClient) {
        this.experimentFeignClient = experimentFeignClient;
    }

    /**
     * 获取实验配置信息
     *
     * @param factorTag
     * @return
     */
    public ConfigBO getDeliverConfig(String factorTag) {
        return Optional.of(experimentFeignClient.findByFactorTag(factorTag).getData()).get();
    }
}
