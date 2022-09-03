package com.wudimanong.resourceserver.dao.mapper;

import com.wudimanong.resourceserver.dao.model.OauthUserDetailsPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jiangqiao
 */
@Mapper
public interface OauthUserDetailsDao {

    /**
     * 根据用户名称获取用户详情信息
     *
     * @param userName
     * @return
     */
    OauthUserDetailsPO getUserDetails(String userName);

}
