package com.yen.springUserSystem.mapper;

// book p.2-28

import com.yen.springUserSystem.bean.UserInfo;

public interface UserInfoDao {

    UserInfo selectByMobileNo(String mobileNo);
    int insert(UserInfo userInfo);
    int updateById(UserInfo userInfo);
}
