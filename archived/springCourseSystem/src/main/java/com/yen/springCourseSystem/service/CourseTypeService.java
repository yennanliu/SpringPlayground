package com.yen.springCourseSystem.service;

// book p. 249

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springCourseSystem.bean.CourseType;

public interface CourseTypeService extends IService<CourseType> {

    /**
     * 根据课程类型删除对应的课程
     * @param typeId
     */
    void deleteCourseType(Integer typeId);

}
