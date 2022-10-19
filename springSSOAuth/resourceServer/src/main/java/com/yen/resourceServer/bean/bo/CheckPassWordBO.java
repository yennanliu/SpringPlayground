package com.yen.resourceServer.bean.bo;

// book p.3-51
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/entity/bo/CheckPassWordBO.java

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckPassWordBO {

    private String userName;
    private String passWord;
    private String salt;
    private String authorities;
}
