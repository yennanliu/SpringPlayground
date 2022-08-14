package com.yen.springCourseSystem.service;

// book p. 250

import com.yen.springCourseSystem.Util.CourseQueryHelper;
import com.yen.springCourseSystem.bean.Course;

import java.util.List;

public interface CourseService {

    void addCourse(Course course);
    boolean removeCourseByNo(String courseNo);
    void updateCourse(Course course);
    Course loadCourseByNo(String courseNo);
    List<Course> loadScopedCourses(CourseQueryHelper helper);
    byte[] getTextBookPic(String courseNo);
}
