package com.vansl.sign.service;

import com.vansl.sign.dao.CourseRepository;
import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public Course findCourseById(Long id){
        return courseRepository.findById(id).get();
    }

    public void save(Course course){
        courseRepository.save(course);
    }

    public List<Student> findStudentsByCourse(Long courseId){
        Course course = findCourseById(courseId);
        List<Student> students = new ArrayList<>();
        course.getCourseStudents().forEach(x-> students.add(x.getStudent()));
        return students;
    }

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

}
