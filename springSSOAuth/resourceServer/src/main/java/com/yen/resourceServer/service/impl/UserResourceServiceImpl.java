package com.yen.resourceServer.service.impl;

// book p.3-59
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/service/impl/UserResourcesServiceImpl.java

import com.yen.resourceServer.bean.GlobalCodeEnum;
import com.yen.resourceServer.bean.bo.GetUserInfoBO;
import com.yen.resourceServer.bean.dao.mapper.OauthUserDetailsDao;
import com.yen.resourceServer.bean.dto.GetUserInfoDTO;
import com.yen.resourceServer.bean.po.OauthUserDetailsPO;
import com.yen.resourceServer.exception.ServiceException;
import com.yen.resourceServer.service.UserResourceService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserResourceServiceImpl implements UserResourceService {

    @Autowired
    OauthUserDetailsDao oauthUserDetailsDao;

    @Override
    public GetUserInfoBO getUserInfo(GetUserInfoDTO getUserInfoDTO) {

        // get user info
        OauthUserDetailsPO oauthUserDetailsPO = oauthUserDetailsDao.getUserDetails(getUserInfoDTO.getUserName());
        if (oauthUserDetailsPO == null) {
            throw new ServiceException(GlobalCodeEnum.BUSI_USER_NOT_EXIST.getCode(),
                    GlobalCodeEnum.BUSI_USER_NOT_EXIST.getDesc());
        }
        return GetUserInfoBO.builder().nickName(oauthUserDetailsPO.getNickName())
                .mobileNo(oauthUserDetailsPO.getMobile()).gender(oauthUserDetailsPO.getGender()).build();
    }

}
