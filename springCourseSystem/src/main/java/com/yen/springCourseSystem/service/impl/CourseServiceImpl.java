package com.yen.springCourseSystem.service.impl;

import com.yen.springCourseSystem.Util.CourseQueryHelper;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.service.CourseService;

import java.util.List;

public class CourseServiceImpl implements CourseService {

    @Override
    public void addCourse(Course course) {

    }

    @Override
    public void removeCourseByNo(String courseNo) {

    }

    @Override
    public void updateCourse(Course course) {

    }

    @Override
    public Course loadCourseByNo(String courseNo) {
        return null;
    }

    @Override
    public List<Course> loadScopedCourses(CourseQueryHelper helper) {
        return null;
    }

    @Override
    public byte[] getTextBookPic(String courseNo) {
        return new byte[0];
    }

}
