package com.yen.springSSO.client;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver/src/main/java/com/wudimanong/authserver/client/ResourceServerFallbackFactory.java

import com.yen.springSSO.bean.ResponseResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ResourceServerFallbackFactory implements FallbackFactory<ResourceServerClient> {

    @Override
    public ResourceServerClient create(Throwable cause) {
        return checkPassWordDTO -> {
            log.info("micro service Fallback logic process ..."); // https://github.com/yennanliu/SpringPlayground/tree/main/springCloud1
            log.error(cause.getMessage());
            return ResponseResult.systemException();
        };
    }
}
