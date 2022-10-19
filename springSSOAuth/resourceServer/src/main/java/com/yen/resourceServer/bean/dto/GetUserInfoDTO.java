package com.yen.resourceServer.bean.dto;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/entity/dto/GetUserInfoDTO.java

import lombok.Data;

@Data
public class GetUserInfoDTO {

    /**
     * 登录账号
     */
    private String userName;
}
