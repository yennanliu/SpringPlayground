package com.xiaoze.springcloud.service.impl;

import com.xiaoze.springcloud.entity.CourseType;
import com.xiaoze.springcloud.mapper.CourseTypeMapper;
import com.xiaoze.springcloud.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CourseTypeServiceImpl
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CourseTypeServiceImpl implements CourseTypeService {

    @Autowired
    private CourseTypeMapper courseTypeMapper;

    @Override
    public void addCourseType(CourseType courseType) {

        courseTypeMapper.insert(courseType);

    }

    @Override
    public void removeCourseType(Integer typeId) {

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
