package com.yen.springCourseSystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springCourseSystem.Util.CourseQueryHelper;
import com.yen.springCourseSystem.bean.Course;

// book p. 250

public interface CourseService extends IService<Course> {

    /**
     * 根据课程编号，获得图片
     *
     * @param courseNo
     * @return byte
     *
     */
    byte[] getTextbookPic(String courseNo);

    /**
     * 分页查询
     * @param helper 模糊查询条件
     * @param pageNo 当前要显示第几页
     * @param pageSize 每页有几条记录
     * @return IPage<Course>
     */
    Page<Course> getCoursePage(CourseQueryHelper helper, Integer pageNo, Integer pageSize);

}
