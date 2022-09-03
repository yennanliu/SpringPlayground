package com.wudimanong.authserver.client;

import com.wudimanong.authserver.client.bo.CheckPassWordBO;
import com.wudimanong.authserver.client.dto.CheckPassWordDTO;
import com.wudimanong.authserver.entity.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author jiangqiao
 */
@FeignClient(value = "sso-resourceserver", configuration = ResourceServerConfiguration.class, fallbackFactory = ResourceServerFallbackFactory.class)
public interface ResourceServerClient {

    /**
     * 登录密码验证接口
     *
     * @param checkPassWordDTO
     * @return
     */
    @PostMapping("/auth/checkPassWord")
    public ResponseResult<CheckPassWordBO> checkPassWord(CheckPassWordDTO checkPassWordDTO);
}
