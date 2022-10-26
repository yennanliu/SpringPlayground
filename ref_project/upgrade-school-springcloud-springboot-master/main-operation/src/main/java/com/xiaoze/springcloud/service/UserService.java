package com.xiaoze.springcloud.service;


import com.xiaoze.springcloud.entity.User;

/**
 * UserService
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
public interface UserService {

    /**
     * 获取一条用户数据
     *
     * @param  userNo
     * @return User
     *
     */
    User get(String userNo);


}

