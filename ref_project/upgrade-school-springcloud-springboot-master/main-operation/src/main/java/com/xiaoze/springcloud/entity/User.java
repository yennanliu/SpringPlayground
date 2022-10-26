package com.xiaoze.springcloud.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * User
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Data
public class User implements Serializable{

    private static final long serialVersionUID = -3130398182995647688L;

    /**
     * 用户账号
     */
    private String userNo;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码
     */
    private String userPwd;


}
