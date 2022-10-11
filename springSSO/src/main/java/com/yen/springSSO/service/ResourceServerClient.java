package com.yen.springSSO.service;

// book p.3-30

import com.yen.springSSO.bean.ResourceServerFallbackFacory;
import com.yen.springSSO.bean.ResponseResult;
import com.yen.springSSO.bean.bo.CheckPasswordBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "sso-resourceserver",
        configuration = ResourceServerConfiguration.class,
        fallbackFactory = ResourceServerFallbackFacory.class)
public interface ResourceServerClient {

    /** login pwd check interface */
    @PostMapping("/auth/checkPassword")
    public ResponseResult<CheckPasswordBO> checkPassWord(CheckPasswordBO checkPasswordBO);
}
