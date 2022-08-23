package com.yen.springMybatisDemo1.mapper;

// https://www.youtube.com/watch?v=sgmK0NNNQdE&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=35
// https://www.youtube.com/watch?v=8XqiR7n3O5U&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=36
// https://www.youtube.com/watch?v=UomWY7-XeDo&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=36
// https://www.youtube.com/watch?v=_gS0vSjXpwg&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=37

import com.yen.springMybatisDemo1.bean.MyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SQLMapper {

    /** select user by fuzzy search */
    List<MyUser> getUserByLike(@Param("username") String username);

    /** bulk delete */
    int deleteMulti(@Param("ids") String ids);  // e.g. : "id1,id2"

    /** query defined table */
    List<MyUser> getUserByTableName(@Param("tableName") String tableName);

    /** add new user */
    void insertUser(MyUser myUser);

}
