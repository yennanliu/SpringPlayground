package com.yen.springBootPOC2AdminSystem.service.impl;

// https://www.youtube.com/watch?v=pzL68_zvqK4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=67

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springBootPOC2AdminSystem.bean.User2;
import com.yen.springBootPOC2AdminSystem.mapper.User2Mapper;
import com.yen.springBootPOC2AdminSystem.service.User2Service;
import org.springframework.stereotype.Service;

/**
 *  NOTE !!!
 *
 *  1) if User22ServiceImpl ONLY implement User2Service interface,
 *     (e.g. public class User22ServiceImpl implements User2Service)
 *     then we NEED to implement ALL methods defined in IService.
 *     -> It's too complex
 *     -> we can extends ServiceImpl<mapper_class, bean_class>
 *     -> so we DON'T need to  implement ALL methods defined in IService.
 */
@Service
public class User22ServiceImpl extends ServiceImpl<User2Mapper, User2> implements User2Service {
}
