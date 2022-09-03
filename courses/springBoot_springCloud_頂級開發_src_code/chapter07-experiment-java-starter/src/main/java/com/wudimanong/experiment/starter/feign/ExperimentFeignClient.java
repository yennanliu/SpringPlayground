package com.wudimanong.experiment.starter.feign;

import com.wudimanong.experiment.client.entity.ResponseResult;
import com.wudimanong.experiment.client.entity.bo.ConfigBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jiangqiao
 */
@FeignClient(value = "experiment", configuration = ExperimentFeignConfiguration.class, fallbackFactory = ExperimentFeignFallbackFactory.class)
public interface ExperimentFeignClient {

    /**
     * 获取实验配置信息FeignClient接口定义
     *
     * @param factorTag
     * @return
     */
    @GetMapping("/config/findByFactorTag")
    ResponseResult<ConfigBO> findByFactorTag(@RequestParam("factorTag") String factorTag);
}