package com.yen.springCourseSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springCourseSystem.Util.ProfessorQueryHelper;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.bean.CourseType;
import com.yen.springCourseSystem.bean.Professor;
import com.yen.springCourseSystem.service.ProfessorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/professor")
@Log4j2
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    /** get users' course */
    @GetMapping("/list")
    public String professorList(Map<String, Object> map, ProfessorQueryHelper helper){

        int pageNo = 1;

//        Professor x = professorService.getById(100);
//        System.out.println(">>> x = " + x);

        Page<Professor> page = professorService.getProfessorPage(helper, pageNo, 2);

        map.put("Professor List", professorService.list());
        map.put("page", page);
        map.put("helper", helper);
        log.info(">>> map = {}", map);

        //return "professor/test";
        return "professor/list_professor";
    }

    @PostMapping(value="/remove/{id}")
    public String remove(@PathVariable("id") Integer id) {

        professorService.removeById(id);

        // return "redirect:/courseType/list";
        //return "redirect:professor/list_professor";
        return "/professor/list";
    }

    // TODO : complete it
    @GetMapping(value="/preUpdate/{id}")
    public String preUpdate(@PathVariable("id") Integer id, Map<String, Object> map) {

        log.info(">>> preUpdate professor : id : {}, map = {}", id, map);
        Professor professor = professorService.getById(id);
        log.info(">>> preUpdate professor : professor = {}", professor);
        map.put("professor", professor);

        return "professor/update_professor";
    }

    @PostMapping(value="/update")
    public String update(Professor professor) {

        log.info(">>> update professor : {}", professor);
        professorService.updateById(professor);

        log.info(">>> update professor : return to professor/list page");
        return "redirect:/professor/list";
        //return "redirect:professor/list_professor";
    }

}
