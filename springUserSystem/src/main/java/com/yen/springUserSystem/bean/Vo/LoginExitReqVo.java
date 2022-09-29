package com.yen.springUserSystem.bean.Vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class LoginExitReqVo implements Serializable {

    private String userId;
    private String accessToken;
}
