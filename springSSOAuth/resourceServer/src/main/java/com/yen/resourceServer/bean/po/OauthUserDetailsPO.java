package com.yen.resourceServer.bean.po;

// book p.3-61
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/dao/model/OauthUserDetailsPO.java

import lombok.Data;
import java.sql.Timestamp;

@Data
public class OauthUserDetailsPO {

    private String userName;
    private String password;
    private String salt;
    private String nickName;
    private String mobile;
    private Integer gender;
    private String authorities;
    private Boolean nonExpired;
    private Boolean nonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private Timestamp createTime;
    private String createBy;
    private Timestamp updateTime;
    private String updateBy;
}