package com.yen.springUserSystem.mapper;

// book p.2-26

import com.yen.springUserSystem.bean.UserSmsCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSmsCodeDao {

    int insert(UserSmsCode userSmsCode);

    UserSmsCode selectByMobileNo(String mobileNo);

}
