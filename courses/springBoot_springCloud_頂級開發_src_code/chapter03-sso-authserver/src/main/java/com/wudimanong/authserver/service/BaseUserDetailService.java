package com.wudimanong.authserver.service;

import com.wudimanong.authserver.client.ResourceServerClient;
import com.wudimanong.authserver.client.bo.CheckPassWordBO;
import com.wudimanong.authserver.client.dto.CheckPassWordDTO;
import com.wudimanong.authserver.entity.ResponseResult;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Service
public class BaseUserDetailService implements UserDetailsService {

    /**
     * 远程Feign调用接口依赖
     */
    @Autowired
    ResourceServerClient resourceServerClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CheckPassWordDTO checkPassWordDTO = CheckPassWordDTO.builder().userName(username).build();
        ResponseResult<CheckPassWordBO> responseResult = resourceServerClient.checkPassWord(checkPassWordDTO);
        CheckPassWordBO checkPassWordBO = responseResult.getData();
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 返回带有用户权限信息的User
        User user = new User(checkPassWordBO.getUserName(),
                checkPassWordBO.getPassWord() + "," + checkPassWordBO.getSalt(), true, true, true, true, authorities);
        return user;
    }
}
