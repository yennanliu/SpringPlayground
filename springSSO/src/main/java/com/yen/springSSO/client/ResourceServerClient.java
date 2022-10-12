package com.yen.springSSO.client;

// book p.3-30

import com.yen.springSSO.bean.ResponseResult;
import com.yen.springSSO.client.bo.CheckPasswordBO;
import com.yen.springSSO.client.dto.CheckPassWordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "sso-resourceserver",
        configuration = ResourceServerConfiguration.class,
        fallbackFactory = ResourceServerFallbackFactory.class)
public interface ResourceServerClient {

    /** login pwd check interface */
    @PostMapping("/auth/checkPassword")
    public ResponseResult<CheckPasswordBO> checkPassWord(CheckPassWordDTO checkPassWordDTO);
}
