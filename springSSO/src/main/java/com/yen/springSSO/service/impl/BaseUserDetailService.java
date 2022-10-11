package com.yen.springSSO.service.impl;

// book p.3-25

import com.yen.springSSO.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseUserDetailService implements UserDetailService {

//    @Autowired
//    ResourceServerClient resourceServerClient;
//
    @Override
    public UserDetails loadUserByUserName(String userName) {

        return null;
    }

}
