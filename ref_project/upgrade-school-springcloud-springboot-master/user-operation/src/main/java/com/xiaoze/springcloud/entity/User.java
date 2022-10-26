package com.xiaoze.springcloud.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Data
@Table(name="tbl_users")
public class User implements Serializable{

    private static final long serialVersionUID = -3130398182995647688L;

    /**
     * 用户账号
     */
    @Id
    @Column(name="user_no")
    private String userNo;

    /**
     * 用户名称
     */
    @Column(name="user_name")
    private String userName;

    /**
     * 密码
     */
    private String userPwd;


}
