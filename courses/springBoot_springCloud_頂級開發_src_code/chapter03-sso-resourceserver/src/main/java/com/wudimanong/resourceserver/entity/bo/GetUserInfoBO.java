package com.wudimanong.resourceserver.entity.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@Builder
public class GetUserInfoBO {

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户手机号
     */
    private String mobileNo;

    /**
     * 用户性别（1-女；2-男；3-未知）
     */
    private Integer gender;

    /**
     * 用户描述
     */
    private String desc;

}
