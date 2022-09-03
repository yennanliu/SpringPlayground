package com.wudimanong.resourceserver.service.impl;

import com.wudimanong.resourceserver.dao.mapper.OauthUserDetailsDao;
import com.wudimanong.resourceserver.dao.model.OauthUserDetailsPO;
import com.wudimanong.resourceserver.entity.GlobalCodeEnum;
import com.wudimanong.resourceserver.entity.bo.GetUserInfoBO;
import com.wudimanong.resourceserver.entity.dto.GetUserInfoDTO;
import com.wudimanong.resourceserver.exception.ServiceException;
import com.wudimanong.resourceserver.service.UserResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Service
public class UserResourcesServiceImpl implements UserResourcesService {

    /**
     * 持久层组件依赖
     */
    @Autowired
    OauthUserDetailsDao oauthUserDetailsDao;

    @Override
    public GetUserInfoBO getUserInfo(GetUserInfoDTO getUserInfoDTO) {
        //获取用户信息
        OauthUserDetailsPO oauthUserDetailsPO = oauthUserDetailsDao.getUserDetails(getUserInfoDTO.getUserName());
        if (oauthUserDetailsPO == null) {
            throw new ServiceException(GlobalCodeEnum.BUSI_USER_NOT_EXIST.getCode(),
                    GlobalCodeEnum.BUSI_USER_NOT_EXIST.getDesc());
        }
        return GetUserInfoBO.builder().nickName(oauthUserDetailsPO.getNickName())
                .mobileNo(oauthUserDetailsPO.getMobile()).gender(oauthUserDetailsPO.getGender()).build();
    }
}
