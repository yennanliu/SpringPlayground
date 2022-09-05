package com.wudimanong.user.dao;

import com.wudimanong.user.entity.UserSmsCode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author joe
 */
@Mapper
public interface UserSmsCodeDao {

    int insert(UserSmsCode userSmsCode);

    UserSmsCode selectByMobileNo(String mobileNo);
}
