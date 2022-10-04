package com.yen.springUserSystem.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@ToString
public class UserInfo  implements Serializable {

    private String userId;
    private String nickName;
    private String mobileNo;
    private String password;
    private Boolean isLogin;
    private Timestamp loginTime;
    private Boolean isDel;
    private Timestamp createTime;
}
