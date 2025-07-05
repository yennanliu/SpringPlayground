package com.yen.springUserSystem.bean.Vo;

// Book p. 2-20

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class LoginByMobileResVo implements Serializable {

    private String userId;
    private String accessToken;
}
