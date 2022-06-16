package com.yen.data.mapper;

import com.yen.data.bean.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TaskMapper {

    @Select("SELECT * FROM tasks WHERE id = #{id}")
    public Task getTaskById(Integer id);

    @Select("SELECT * FROM tasks")
    public Task[] getAllTask();

    @Insert("INSERT INTO history VALUES ()")
    public void insertTask();
}
