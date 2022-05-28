package com.yen.springBootPOC2AdminSystem.mapper;

// https://www.youtube.com/watch?v=njvVPhCFH6o&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=66
// https://www.youtube.com/watch?v=pzL68_zvqK4&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=67

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.springBootPOC2AdminSystem.bean.User2;
import org.apache.ibatis.annotations.Mapper;

/** mapper for mybatis plus User2Mapper */

/**
 *  NOTE !!!
 *   -> since we use mybatis plus here,
 *   -> so we can use extends BaseMapper<T> (T is generic type)
 *   -> so our interface (User2Mapper) ALREADY has basic CRUD methods which already defined in BaseMapper
 *      -> e.g. create, read, update, delete
 */

@Mapper
public interface User2Mapper extends BaseMapper<User2> {
}
