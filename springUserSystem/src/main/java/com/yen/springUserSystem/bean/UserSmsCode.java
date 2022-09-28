package com.yen.springUserSystem.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import java.sql.Timestamp;

@Data
@Builder
@ToString
public class UserSmsCode {

    private String mobileNo;
    private String smsCode;
    private Timestamp sendTime;
    private Timestamp createTime;
}
