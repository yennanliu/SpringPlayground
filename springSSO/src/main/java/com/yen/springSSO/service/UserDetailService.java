package com.yen.springSSO.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailService {

    UserDetails loadUserByUserName(String userName);
}
