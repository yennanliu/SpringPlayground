package com.wudimanong.resourceserver.service;

import com.wudimanong.resourceserver.entity.bo.GetUserInfoBO;
import com.wudimanong.resourceserver.entity.dto.GetUserInfoDTO;

/**
 * @author jiangqiao
 */
public interface UserResourcesService {

    /**
     * 获取受限用户信息接口
     *
     * @param getUserInfoDTO
     * @return
     */
    GetUserInfoBO getUserInfo(GetUserInfoDTO getUserInfoDTO);
}
