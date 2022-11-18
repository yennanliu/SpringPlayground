package com.yen.springCourseSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springCourseSystem.Util.ProfessorQueryHelper;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.bean.Professor;
import com.yen.springCourseSystem.service.ProfessorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/pro")
@Log4j2
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    /** get users' course */
    @GetMapping("/list")
    public String professorList(Map<String, Object> map, ProfessorQueryHelper helper){

        int pageNo = 1;

        Professor x = professorService.getById(100);
        System.out.println(">>> x = " + x);

        Page<Professor> page = professorService.getProfessorPage(helper, pageNo, 2);

        map.put("Professor List", professorService.list());
        map.put("page", page);
        map.put("helper", helper);
        log.info(">>> map = {}", map);

        return "professor/test";
    }

}
