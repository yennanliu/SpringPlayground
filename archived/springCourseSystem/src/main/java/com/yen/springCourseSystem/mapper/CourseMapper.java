package com.yen.springCourseSystem.mapper;

// book p. 254

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springCourseSystem.bean.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 获取Course分页数据
     * @param coursePage
     * @param wrapper
     * @return List<Course>
     */
    List<Course> getCourseList(Page<Course> coursePage, @Param("ew") Wrapper<Course> wrapper);

}
