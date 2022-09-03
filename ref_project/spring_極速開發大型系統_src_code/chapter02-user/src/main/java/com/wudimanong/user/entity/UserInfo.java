package com.wudimanong.user.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;

/**
 * @author joe
 */
@Data
@Builder
public class UserInfo implements Serializable {

    private int id;
    private String userId;
    private String nickName;
    private String mobileNo;
    private String password;
    private String isLogin;
    private Timestamp loginTime;
    private String isDel;
    private Timestamp createTime;

    public UserInfo() {
    }

    public UserInfo(int id, String userId, String nickName, String mobileNo, String password, String isLogin,
            Timestamp loginTime, String isDel, Timestamp createTime) {
        this.id = id;
        this.userId = userId;
        this.nickName = nickName;
        this.mobileNo = mobileNo;
        this.password = password;
        this.isLogin = isLogin;
        this.loginTime = loginTime;
        this.isDel = isDel;
        this.createTime = createTime;
    }
}
