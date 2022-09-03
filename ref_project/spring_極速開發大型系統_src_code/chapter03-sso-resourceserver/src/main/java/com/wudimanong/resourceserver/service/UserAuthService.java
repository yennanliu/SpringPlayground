package com.wudimanong.resourceserver.service;

import com.wudimanong.resourceserver.entity.bo.CheckPassWordBO;
import com.wudimanong.resourceserver.entity.dto.CheckPassWordDTO;

/**
 * @author jiangqiao
 */
public interface UserAuthService {

    /**
     * 密码验证业务层接口
     *
     * @param checkPassWordDTO
     * @return
     */
    CheckPassWordBO checkPassWord(CheckPassWordDTO checkPassWordDTO);
}
