package com.yen.springCourseSystem.bean;

// https://github.com/yennanliu/SpringPlayground/blob/main/ref_project/easy-springboot-master/src/main/java/com/xiaoze/course/entity/UserInfo.java

import lombok.Data;
import java.io.Serializable;

@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 6172465382210937945L;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public UserInfo() {
    }

    public UserInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
