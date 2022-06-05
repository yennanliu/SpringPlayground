package com.vansl.sign.controller;

import com.vansl.sign.vo.HttpResult;
import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.Teacher;
import com.vansl.sign.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @PostMapping(value = "/teachers",params = {"name"})
    public HttpResult add(String name) {
        HttpResult result = new HttpResult();
        try{
            Teacher teacher = new Teacher();
            teacher.setName(name);
            teacherService.save(teacher);
            result.setStatus("ok");
        }catch (Exception e){
            result.setStatus("wrong");
        }finally {
            return result;
        }

    }


    @GetMapping(value = "/teacher/{id}")
    public HttpResult queryTeacherById(@PathVariable Long id){
         HttpResult<Teacher> result = new HttpResult<>();
         result.setData(teacherService.findTeacherById(id));
         result.setStatus("ok");
         return result;
    }

    @GetMapping(value = "/teachers")
    public HttpResult queryAll(){
        HttpResult<List<Teacher>> result = new HttpResult<>();
        result.setData(teacherService.findAll());
        result.setStatus("ok");
        return result;
    }


    /*
     * 根据教师Id查询教师的所有课程
     * */
    @GetMapping(value = "/teacher/{teacherId}/courses")
    public HttpResult queryCoursesByTeacher(@PathVariable Long teacherId){
        HttpResult<List<Course>> result = new HttpResult<>();
        result.setData(teacherService.findCoursesByTeacher(teacherId));
        result.setStatus("ok");
        return result;
    }
}
