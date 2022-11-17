package com.yen.springCourseSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.service.ProfessorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pro")
@Log4j2
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    /** get users' course */
    @GetMapping("/list")
    public String professorList(){

        int pageNo = 1;

        return null;
    }

}
