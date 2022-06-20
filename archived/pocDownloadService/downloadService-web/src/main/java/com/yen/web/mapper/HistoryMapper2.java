package com.yen.web.mapper;

import com.yen.data.bean.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HistoryMapper2 {

    // TODO : fix below data type (e.g. how to deal with array IO with mysql)
    @Insert("INSERT INTO history VALUES (id = #{id}, userList = #{userList}, reportField = #{reportField}, exportType = #{exportType}, startTime = #{startTime}, endTime = #{endTime}, status = #{status} )")
    //public Task[] insertTask(Integer id, Integer[] userList, String[] reportField, String exportType, Integer startTime, Integer endTime, String status);
    //public void insertTask(Integer id, String userList, String reportField, String exportType, Integer startTime, Integer endTime, String status);
    public void insertTask(Task task);

    //public void insertTask(Task task);
}
