package com.wudimanong.authserver.client.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@Builder
public class CheckPassWordBO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 加盐值
     */
    private String salt;

    /**
     * 用户权限
     */
    private String authorities;

}
