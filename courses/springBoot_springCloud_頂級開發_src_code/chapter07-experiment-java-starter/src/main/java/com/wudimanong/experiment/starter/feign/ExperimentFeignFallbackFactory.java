package com.wudimanong.experiment.starter.feign;

import com.wudimanong.experiment.client.entity.ResponseResult;
import com.wudimanong.experiment.client.entity.bo.ConfigBO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangqiao
 */
@Slf4j
public class ExperimentFeignFallbackFactory implements FallbackFactory<ExperimentFeignClient> {

    /**
     * @param cause
     * @return
     */
    @Override
    public ExperimentFeignClient create(Throwable cause) {
        return new ExperimentFeignClient() {
            @Override
            public ResponseResult<ConfigBO> findByFactorTag(String factorTag) {
                log.info("AbTest实验服务调用降级逻辑处理...");
                log.error(cause.getMessage());
                return null;
            }
        };
    }
}
