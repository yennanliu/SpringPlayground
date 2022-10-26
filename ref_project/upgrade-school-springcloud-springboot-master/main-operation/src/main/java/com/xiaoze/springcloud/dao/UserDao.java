package com.xiaoze.springcloud.dao;


import com.xiaoze.springcloud.dao.impl.UserDaoHystrix;
import com.xiaoze.springcloud.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * UserDao
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@FeignClient(name="gateway-server", fallback = UserDaoHystrix.class)
public interface UserDao {

    /**
     * 获取一条用户数据
     *
     * @param  userNo
     * @return User
     *
     */
    @GetMapping("/user/getOneUser/{userNo}")
    User get(@PathVariable("userNo") String userNo);

}

