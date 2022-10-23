package com.yen.resourceServer.service.impl;

// book p.3-58
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/service/impl/UserAuthServiceImpl.java

import com.yen.resourceServer.bean.GlobalCodeEnum;
import com.yen.resourceServer.bean.bo.CheckPassWordBO;
import com.yen.resourceServer.bean.dao.mapper.OauthUserDetailsDao;
import com.yen.resourceServer.bean.dto.CheckPassWordDTO;
import com.yen.resourceServer.bean.po.OauthUserDetailsPO;
import com.yen.resourceServer.exception.ServiceException;
import com.yen.resourceServer.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    OauthUserDetailsDao oauthUserDetailsDao;

    @Override
    public CheckPassWordBO checkPassWord(CheckPassWordDTO checkPassWordDTO) {

        // get user info
        OauthUserDetailsPO oauthUserDetailsPo = oauthUserDetailsDao.getUserDetails(checkPassWordDTO.getUserName());
        if (oauthUserDetailsPo == null){
            throw new ServiceException(GlobalCodeEnum.BUSI_USER_NOT_EXIST.getCode(), GlobalCodeEnum.BUSI_USER_NOT_EXIST.getDesc());
        }

        // return password info
        return CheckPassWordBO.builder()
                .userName(oauthUserDetailsPo.getUserName())
                .passWord(oauthUserDetailsPo.getPassword())
                .authorities(oauthUserDetailsPo.getAuthorities())
                .build();
    }

}
