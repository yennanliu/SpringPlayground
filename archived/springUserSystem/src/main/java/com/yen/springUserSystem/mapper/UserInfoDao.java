package com.yen.springUserSystem.mapper;

// book p.2-28

import com.yen.springUserSystem.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao {

    UserInfo selectByMobileNo(String mobileNo);
    int insert(UserInfo userInfo);
    int updateById(UserInfo userInfo);
}
