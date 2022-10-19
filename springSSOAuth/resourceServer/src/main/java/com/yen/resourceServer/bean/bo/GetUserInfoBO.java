package com.yen.resourceServer.bean.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/entity/bo/GetUserInfoBO.java

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserInfoBO {

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户手机号
     */
    private String mobileNo;

    /**
     * 用户性别（1-女；2-男；3-未知）
     */
    private Integer gender;

    /**
     * 用户描述
     */
    private String desc;

}