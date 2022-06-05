package com.vansl.sign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("add_teacher")
    public String addTeacher(){
        return "add_teacher";
    }

    @GetMapping("add_student")
    public String addStudent(){
        return "add_student";
    }

    @GetMapping("add_course")
    public String addCourse(){
        return "add_course";
    }

    @GetMapping("add_course_student")
    public String addCourseStudent(){
        return "add_course_student";
    }

}
