package com.wudimanong.authserver.client.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@Builder
public class CheckPassWordDTO {

    /**
     * 登录账号
     */
    private String userName;
}
