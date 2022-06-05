package com.vansl.sign.service;

import com.vansl.sign.dao.TeacherRepository;
import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    /**
    * @param id
    * @return
    */
    public Teacher findTeacherById(Long id){
        return teacherRepository.findById(id).get();
    }

    public void save(Teacher Teacher){
        teacherRepository.save(Teacher);
    }

    public Teacher findTeacherByName(String name){
        return teacherRepository.findTeacherByName(name);
    }

    public List<Teacher> findAll(){
        return teacherRepository.findAll();
    }

    public List<Course> findCoursesByTeacher(Long teacherId){
        return teacherRepository.findCoursesByTeacher(teacherId);
    }

}
