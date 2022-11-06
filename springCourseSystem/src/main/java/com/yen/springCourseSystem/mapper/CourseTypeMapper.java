package com.yen.springCourseSystem.mapper;

// book p. 254

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.springCourseSystem.bean.CourseType;

import java.util.List;

public interface CourseTypeMapper extends BaseMapper<CourseType> {

    List<CourseType> selectAll();
}