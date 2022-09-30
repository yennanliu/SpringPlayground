package com.yen.springUserSystem.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@Builder
@ToString
public class UserInfo {

    private String userId;
    private String nickName;
    private String mobileNo;
    private String password;
    private Boolean isLogin;
    private Timestamp loginTime;
    private Boolean isDel;
    private Timestamp createTime;
}
