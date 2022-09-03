package com.wudimanong.user.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;

/**
 * @author joe
 */
@Data
@Builder
public class UserSmsCode {

    private int id;
    private String mobileNo;
    private String smsCode;
    private Timestamp sendTime;
    private Timestamp createTime;

    public UserSmsCode() {
    }

    public UserSmsCode(int id, String mobileNo, String smsCode, Timestamp sendTime, Timestamp createTime) {
        this.id = id;
        this.mobileNo = mobileNo;
        this.smsCode = smsCode;
        this.sendTime = sendTime;
        this.createTime = createTime;
    }
}
