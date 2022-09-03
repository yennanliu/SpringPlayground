package com.wudimanong.user.dao;

import com.wudimanong.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author joe
 */
@Mapper
public interface UserInfoDao {

    UserInfo selectByMobileNo(String mobikeNo);

    int insert(UserInfo userInfo);

    int updateById(UserInfo userInfo);
}
