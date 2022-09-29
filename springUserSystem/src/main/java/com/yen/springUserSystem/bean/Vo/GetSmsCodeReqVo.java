package com.yen.springUserSystem.bean.Vo;

// Book p. 2-19

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class GetSmsCodeReqVo implements Serializable {

    private String reqId;
    private String mobileNo;
}
