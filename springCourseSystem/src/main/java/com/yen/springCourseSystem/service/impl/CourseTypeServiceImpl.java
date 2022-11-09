package com.yen.springCourseSystem.service.impl;

// book p. 251

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.bean.CourseType;
import com.yen.springCourseSystem.mapper.CourseMapper;
import com.yen.springCourseSystem.mapper.CourseTypeMapper;
import com.yen.springCourseSystem.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements CourseTypeService {

    @Autowired
    CourseMapper courseMapper;

    @Override
    public void deleteCourseType(Integer typeId) {

        courseMapper.delete(
                new LambdaQueryWrapper<Course>()
                        .eq(Course::getTypeId, typeId)
        );
        baseMapper.deleteById(typeId);

    }
}
