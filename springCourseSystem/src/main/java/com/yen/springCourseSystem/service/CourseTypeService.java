package com.yen.springCourseSystem.service;

// book p. 249

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springCourseSystem.bean.CourseType;

import java.util.List;

public interface CourseTypeService extends IService<CourseType> {

    void addCourseType(CourseType courseType);
    void removeCourseType(Integer typeId);
    void updateCourseType(CourseType courseType);
    CourseType getCourseTypeById(Integer typeId);
    List<CourseType> loadAll();

    void deleteCourseType(Integer typeId);

}
