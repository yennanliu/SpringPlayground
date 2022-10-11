package com.yen.springSSO.service.impl;

// book p.3-25

import com.yen.springSSO.bean.ResponseResult;
import com.yen.springSSO.bean.bo.CheckPasswordBO;
import com.yen.springSSO.bean.dto.CheckPassWordDTO;
import com.yen.springSSO.service.ResourceServerClient;
import com.yen.springSSO.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class BaseUserDetailService implements UserDetailService {

    @Autowired
    ResourceServerClient resourceServerClient;

    @Override
    public UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException {

        CheckPassWordDTO checkPassWordDTO = CheckPassWordDTO.builder().userName(userName).build();
        ResponseResult<CheckPasswordBO> responseResult = resourceServerClient.checkPassWord(checkPassWordDTO);
        CheckPasswordBO checkPasswordBO = responseResult.getData();
        List<GrantedAuthority> authorities = new ArrayList<>();
        // return user-info-permission to user
        User user = new User(checkPasswordBO.getUserName(), checkPasswordBO.getPassWord() + "," + checkPasswordBO.getSalt(), true, true,true, true, authorities);

        return user;
    }

}
