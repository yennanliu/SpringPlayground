package com.yen.springCourseSystem.mapper;

// book p. 254

import com.yen.springCourseSystem.bean.Course;

import java.util.List;
import java.util.Map;

public interface CourseMapper{

    void addCourse(Course course);
    boolean removeCourseByNo(String courseNo);
    boolean removeCourseByTypeId(Integer typeId);
    void updateCourse(Course course);
    Course loadCourseByNo(String courseNo);
    List<String> loadCourseByTypeId(Integer typeId);
    List<Course> loadScopedCourses(Map map);

}
