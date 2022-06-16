package com.yen.data.mapper;

import com.yen.data.bean.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface HistoryMapper {

    @Select("INSERT INTO history VALUES (id = #{id}, userList = #{userList}, reportField = #{reportField}, exportType = #{exportType}, startTime = #{startTime}, endTime = #{endTime}, status = #{status} )")
    public Task[] insertTask(Integer id, Integer[] userList, String[] reportField, String exportType, Integer startTime, Integer endTime, String status);

}

