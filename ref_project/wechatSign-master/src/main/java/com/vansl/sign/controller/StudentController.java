package com.vansl.sign.controller;

import com.vansl.sign.vo.HttpResult;
import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.Student;
import com.vansl.sign.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping(value = "/students",params = {"name"})
    public HttpResult add(String name) {
        HttpResult result = new HttpResult();
        try {
            Student student = new Student();
            student.setName(name);
            studentService.save(student);
            result.setStatus("ok");
        }catch (Exception e){
            result.setStatus("wrong");
        }finally {
            return result;
        }

    }

    @GetMapping(value = "/student/{id}")
    public HttpResult queryStudentById(@PathVariable Long id){
        HttpResult<Student> result = new HttpResult<>();
        result.setData(studentService.findStudentById(id));
        result.setStatus("ok");
        return result;
    }

    @GetMapping(value = "/students")
    public HttpResult queryAll(){
        HttpResult<List<Student>> result = new HttpResult<>();
        result.setData(studentService.findAll());
        result.setStatus("ok");
        return result;
    }

    /*
     * 查询某个学生的所有课程
     * */
    @GetMapping(value = "/student/{studentId}/courses")
    public HttpResult queryStudentsByCourse(@PathVariable Long studentId){
        HttpResult<List<Course>> result = new HttpResult<>();
        List<Course> courses = studentService.findCoursesByStudent(studentId);
        result.setData(courses);
        result.setStatus("ok");
        return result;
    }
}
