package com.yen.springCourseSystem.service.impl;

// book p. 251

import com.yen.springCourseSystem.bean.CourseType;
import com.yen.springCourseSystem.mapper.CourseMapper;
import com.yen.springCourseSystem.mapper.CourseTypeMapper;
import com.yen.springCourseSystem.service.CourseTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseTypeServiceImpl implements CourseTypeService {

    @Resource
    CourseTypeMapper courseTypeMapper;

    @Resource
    CourseMapper courseMapper;

    @Override
    public void addCourseType(CourseType courseType) {
        courseTypeMapper.insert(courseType);
    }

    @Override
    public void removeCourseType(Integer typeId) {
        if (courseMapper.loadCourseByTypeId(typeId) != null){
            courseMapper.removeCourseByTypeId(typeId);
        }
        // delete record in course_type table with PK
        courseTypeMapper.deleteByPrimaryKey(typeId);
    }

    @Override
    public void updateCourseType(CourseType courseType) {
        courseTypeMapper.updateByPrimaryKey(courseType);
    }

    @Override
    public CourseType getCourseTypeById(Integer typeId) {
        return courseTypeMapper.selectByPrimaryKey(typeId);
    }

    @Override
    public List<CourseType> loadAll() {
        return courseTypeMapper.selectAll();
    }

}
