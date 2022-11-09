package com.yen.springCourseSystem.mapper;

// book p. 254

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.springCourseSystem.bean.CourseType;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CourseTypeMapper extends BaseMapper<CourseType> {

}
