package com.vansl.sign.service;

import com.vansl.sign.dao.CourseRepository;
import com.vansl.sign.dao.CourseStudentRepository;
import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.CourseStudent;
import com.vansl.sign.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseStudentService {

    @Autowired
    CourseStudentRepository courseStudentRepository;


    public void save(CourseStudent courseStudent){
        courseStudentRepository.save(courseStudent);
    }

}
