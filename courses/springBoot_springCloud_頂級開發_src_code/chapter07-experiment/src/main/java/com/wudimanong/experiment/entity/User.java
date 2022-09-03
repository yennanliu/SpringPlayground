package com.wudimanong.experiment.entity;

import lombok.Data;

/**
 * @author jiangqiao
 * @desc 登录用户实体
 */
@Data
public class User {

    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 头像链接
     */
    private String avatar;

    /**
     * 真实姓名
     */
    private String name;

}
