package com.yen.springCourseSystem.service.impl;

// book p. 252

import com.yen.springCourseSystem.Util.CourseQueryHelper;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.mapper.CourseMapper;
import com.yen.springCourseSystem.service.CourseService;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseServiceImpl implements CourseService {

    @Resource
    CourseMapper courseMapper;

    @Override
    public void addCourse(Course course) {
        courseMapper.addCourse(course);
    }

    @Override
    public boolean removeCourseByNo(String courseNo) {
        courseMapper.removeCourseByNo(courseNo);
        return true;
    }

    @Override
    public void updateCourse(Course course) {
        String[] courseReq = course.getCourseReqs();
        if (courseReq != null && courseReq.length > 0){
            courseMapper.updateCourse(course);
        }else{
            course.setReqs("");
            courseMapper.updateCourse(course);
        }
    }

    @Override
    public Course loadCourseByNo(String courseNo) {
        // TODO : re-check this ?
        Course course = new Course();
        course = null;
        if (courseNo != null){
            course = courseMapper.loadCourseByNo(courseNo);
        }

        return course;
    }

    @Override
    public List<Course> loadScopedCourses(CourseQueryHelper helper) {

        // TODO : check "initialCapacity"
        Map<String, Object> map = new HashMap<>(16);
        // TODO : fix below (?)
        //map = getQueryHelper(helper);
        List<Course> list = courseMapper.loadScopedCourses(map);
        return list;
    }

    @Override
    public byte[] getTextBookPic(String courseNo) {

        byte[] textBookPic = null;
        Course course = courseMapper.loadCourseByNo(courseNo);
        textBookPic = course.getCourseTextBookPic();
        return textBookPic;
    }

    private Map<String, Object> GetQueryHelper(CourseQueryHelper helper){

        Map<String, Object> map = new HashMap<>(16);

        if (helper.getQryCourseName() != null){
            map.put("qryCourseName", helper.getQryCourseName());
        }

        if (helper.getQryEndPoint() != null){
            map.put("qryEndPoint", helper.getQryEndPoint());
        }

        if (helper.getQryStartPoint() != null){
            map.put("qryStartPoint", helper.getQryStartPoint());
        }

        if (helper.getQryCourseType() != null){
            map.put("qryCourseType", helper.getQryCourseType());
        }

        return map;
    }

}
