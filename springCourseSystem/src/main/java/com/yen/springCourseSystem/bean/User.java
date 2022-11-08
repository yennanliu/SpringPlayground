package com.yen.springCourseSystem.bean;

// book p. 246

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{

    private static final long serialVersionUID = -8064251796984414018L;

    /**
     * 用户账号
     */
    @TableId(type = IdType.AUTO)
    private String userNo;

    /**
     * 密码
     */
    private String userPwd;

    /**
     * 用户名称
     */
    private String userName;

}