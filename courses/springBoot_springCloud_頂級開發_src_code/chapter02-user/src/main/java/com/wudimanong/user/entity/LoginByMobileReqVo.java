package com.wudimanong.user.entity;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author joe
 */
@Data
@Builder
public class LoginByMobileReqVo implements Serializable {

    private String reqId;
    private String mobileNo;
    private String smsCode;

}
