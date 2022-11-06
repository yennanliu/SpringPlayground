package com.yen.springCourseSystem.service.impl;

// book p. 250
// https://github.com/yennanliu/SpringPlayground/blob/main/ref_project/easy-springboot-master/src/main/java/com/xiaoze/course/service/impl/UserServiceImpl.java

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springCourseSystem.bean.User;
import com.yen.springCourseSystem.mapper.UserMapper;
import com.yen.springCourseSystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}