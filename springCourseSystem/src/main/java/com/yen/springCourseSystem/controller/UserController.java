package com.yen.springCourseSystem.controller;

import com.yen.springCourseSystem.service.CourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@Log4j2
public class UserController {

    /** get users' course */
    @GetMapping("/list")
    public String userList(){
        return null;
    }

}
