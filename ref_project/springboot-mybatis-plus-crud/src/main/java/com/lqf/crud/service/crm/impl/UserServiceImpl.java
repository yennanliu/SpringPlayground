package com.lqf.crud.service.crm.impl;

import com.lqf.crud.bean.crm.User;
import com.lqf.crud.dao.crm.UserMapper;
import com.lqf.crud.service.crm.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lqf
 * @since 2018-09-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
