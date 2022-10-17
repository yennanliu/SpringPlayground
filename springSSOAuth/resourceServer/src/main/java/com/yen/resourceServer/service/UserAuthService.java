package com.yen.resourceServer.service;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/service/UserAuthService.java

import com.yen.resourceServer.bean.bo.CheckPassWordBO;
import com.yen.resourceServer.bean.dto.CheckPassWordDTO;

public interface UserAuthService {

    /** check password interface */
    CheckPassWordBO checkPassWord(CheckPassWordDTO checkPassWordDTO);
}
