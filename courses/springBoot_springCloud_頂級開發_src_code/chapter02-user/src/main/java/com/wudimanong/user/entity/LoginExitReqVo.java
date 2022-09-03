package com.wudimanong.user.entity;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author joe
 */
@Data
@Builder
public class LoginExitReqVo implements Serializable {

    private String userId;
    private String accessToken;

}
