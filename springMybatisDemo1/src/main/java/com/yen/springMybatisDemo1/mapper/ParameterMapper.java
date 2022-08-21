package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=_5a7CjR-XSw&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=22
// https://www.youtube.com/watch?v=kmPPvKs0G6Y&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=26
// https://www.youtube.com/watch?v=RZRKAEyAOfQ&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=27
// https://www.youtube.com/watch?v=CzlctiCjlZE&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=28
// https://www.youtube.com/watch?v=9Q-SJ1lqJfA&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=28
// https://www.youtube.com/watch?v=vwj4GNZVuh4&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=32
// https://www.youtube.com/watch?v=7EKLqmiYwAQ&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=32
// https://www.youtube.com/watch?v=xwf7iCOgzLU&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=34

import com.yen.springMybatisDemo1.bean.MyUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParameterMapper {

    /** select all users */
    List<MyUser> getAllUser();

    /** get user by username */
    MyUser getUserByName(String username);

    /** verify login */
    MyUser checkLogin(String username, String password);

    /** verify login V2 (param is a map) */
    MyUser checkLogin2(Map<String, Object> map);

    /** verify login V3 (@Param as parameter)
     *
     *  NOTE !!! via @Param,
     *      -> Mybatis will put params into a k-v map
     *      -> example :
     *          -> @Param("username") String username
     *          -> Map("username", "some_user_name")
     */
    MyUser checkLogin3(@Param("username") String username, @Param("password") String password);

    /** insert a new user (param is an actual class) */
    int insertUser(MyUser user);

    /** select all users count */
    Integer getAllUserCount();

    /** select user info return as Map
     *
     *   -> if result type as Map<String, Object>
     *   -> will use att-name as key, value as value
     *   -> (Map 's k-v value)
     */
    Map<String, Object> getUserByIdToMap(@Param("id") Integer id);

    /** select all users return as Map
     *
     *  NOTE !!! : for multiple Map return, we HAVE to use List<T> as return type
     */
    List<Map<String, Object>> getAllUserToMap();

    /**  select all users return as Map : V2
     *
     *   -> use  @MapKey("id"),
     *   -> so use part of return value as key, all return value as value
     *
     *  https://youtu.be/xwf7iCOgzLU?t=289
     */
    @MapKey("id") // NOTE !! have to use UNIQUE key (or some value may be overridden)
    Map<String, Object> getAllUserToMap2();

}
