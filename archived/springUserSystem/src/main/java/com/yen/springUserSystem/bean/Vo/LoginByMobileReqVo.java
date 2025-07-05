package com.yen.springUserSystem.bean.Vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class LoginByMobileReqVo implements Serializable  {

    private String reqId;
    private String mobileNo;
    private String smsCode;
}
