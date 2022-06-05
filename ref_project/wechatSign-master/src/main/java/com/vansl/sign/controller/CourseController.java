package com.vansl.sign.controller;

import com.vansl.sign.vo.HttpResult;
import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.CourseStudent;
import com.vansl.sign.entity.Student;
import com.vansl.sign.service.CourseService;
import com.vansl.sign.service.CourseStudentService;
import com.vansl.sign.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    StudentService studentService;

    @Autowired
    CourseStudentService courseStudentService;

    /*
    * 增加课程
    * */
    @PostMapping(value = "/courses",params = {"name","teacherId"})
    public HttpResult add(String name,Long teacherId) {
        HttpResult result = new HttpResult();
        try {
            Course course = new Course();
            course.setName(name);
            course.setTeacherId(teacherId);
            courseService.save(course);
            result.setStatus("ok");
        }catch (Exception e){
            result.setStatus("wrong");
        }finally {
            return result;
        }
    }

    @GetMapping(value = "/course/{id}")
    public HttpResult queryCourseById(@PathVariable Long id){
        HttpResult<Course> result = new HttpResult<>();
        result.setData(courseService.findCourseById(id));
        return result;
    }

    @GetMapping(value = "/courses")
    public HttpResult queryAll(){
        HttpResult<List<Course>> result = new HttpResult<>();
        result.setData(courseService.findAll());
        result.setStatus("ok");
        return result;
    }


    /*
     * 查询某门课程的所有学生
     * */
    @GetMapping(value = "/course/{courseId}/students")
    public HttpResult queryStudentsByCourse(@PathVariable Long courseId){
        HttpResult<List<Student>> result = new HttpResult<>();
        result.setData(courseService.findStudentsByCourse(courseId));
        result.setStatus("ok");
        return result;
    }


    /*
     * 增加学生的课程
     * */
    @PostMapping(value = "/course/{courseId}/student/{studentId}")
    public HttpResult addCourseStudent(@PathVariable Long courseId,Long studentId) {
        HttpResult result = new HttpResult();
        try {
            Course course = courseService.findCourseById(courseId);
            Student student = studentService.findStudentById(studentId);
            CourseStudent courseStudent = new CourseStudent();
            courseStudent.setCourse(course);
            courseStudent.setStudent(student);
            courseStudentService.save(courseStudent);
            result.setStatus("ok");
        }catch (Exception e){
            result.setStatus("wrong");
        }finally {
            return result;
        }
    }
}
