package com.yen.springBootPOC2AdminSystem.service;

// https://www.youtube.com/watch?v=pzL68_zvqK4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=67

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springBootPOC2AdminSystem.bean.User2;

/** User2 service interface
 *  -> its implementation : User2ServiceImpl
 */


/**
 *  NOTE !!!
 *
 *   1) we can make interface User2Service extends IService<T>
 *      -> so this interface will have ALL default methods defined in IService
 *
 *   2) and next step, we need to modify this interface's implementation (aka User22ServiceImpl).
 */
public interface User2Service extends IService<User2> {
}
