package com.wudimanong.resourceserver.dao.model;

import java.sql.Timestamp;
import lombok.Data;

/**
 * @author jiangqiao
 */
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
