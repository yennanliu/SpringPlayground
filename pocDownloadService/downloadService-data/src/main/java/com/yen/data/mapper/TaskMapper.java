package com.yen.data.mapper;

import com.yen.data.bean.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TaskMapper {

    @Select("SELECT * FROM tasks WHERE id = #{id}")
    Task getTaskById(Integer id);

    @Select("SELECT * FROM tasks")
    Task[] getAllTask();
}
