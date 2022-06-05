package com.vansl.sign.service;

import com.vansl.sign.dao.CourseRepository;
import com.vansl.sign.dao.StudentRepository;
import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    public Student findStudentById(Long id){
        return studentRepository.findById(id).get();
    }

    public void save(Student Student){
        studentRepository.save(Student);
    }

    public Student findStudentByName(String name){
        return studentRepository.findStudentByName(name);
    }

    public List<Student> findAll(){
        return studentRepository.findAll();
    }

    /*
     * 查询某个学生的所有课程
     * */
    public List<Course> findCoursesByStudent(Long studentId){
        Student student = findStudentById(studentId);
        List<Course> courses = new ArrayList<>();
        student.getCourseStudents().forEach(x->courses.add(x.getCourse()));
        return courses;
    }
}
