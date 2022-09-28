package com.yen.springUserSystem.bean.Vo;

// Book p. 2-19

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class GetSmsCodeReqVo {

    private String reqId;
    private String mobileNo;
}
