package com.xiaoze.springcloud.service.impl;

import com.xiaoze.springcloud.dao.CourseTypeDao;
import com.xiaoze.springcloud.entity.CourseType;
import com.xiaoze.springcloud.entity.Page;
import com.xiaoze.springcloud.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CourseTypeServiceImpl
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Service
public class CourseTypeServiceImpl implements CourseTypeService {

    @Autowired
    private CourseTypeDao courseTypeDao;


    @Override
    public void addCourseType(CourseType courseType) {

        String str = courseTypeDao.addCourseType(courseType);
        System.out.println("增加：" + str);

    }

    @Override
    public void removeCourseType(Integer typeId) {

        String str = courseTypeDao.removeCourseType(typeId);
        System.out.println("删除：" + str);
    }

    @Override
    public void updateCourseType(CourseType courseType) {

        String str = courseTypeDao.updateCourseType(courseType);
        System.out.println("更新：" + str);

    }

    @Override
    public CourseType getCourseTypeById(Integer typeId) {

        return courseTypeDao.getCourseTypeById(typeId);

    }

    @Override
    public Page<CourseType> loadByPageNo(Integer pageNo) {
        return courseTypeDao.loadByPageNo(pageNo);
    }


}
