package com.wudimanong.authserver.client;

import com.wudimanong.authserver.entity.ResponseResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangqiao
 */
@Slf4j
public class ResourceServerFallbackFactory implements FallbackFactory<ResourceServerClient> {

    @Override
    public ResourceServerClient create(Throwable cause) {
        return checkPassWordDTO -> {
            log.info("资源服务调用降级逻辑处理...");
            log.error(cause.getMessage());
            return ResponseResult.systemException();
        };
    }
}
