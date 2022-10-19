package com.yen.resourceServer.bean.dao.mapper;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-resourceserver/src/main/java/com/wudimanong/resourceserver/dao/mapper/OauthUserDetailsDao.java

import com.yen.resourceServer.bean.po.OauthUserDetailsPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OauthUserDetailsDao {

    OauthUserDetailsPO getUserDetails(String userName);

}
