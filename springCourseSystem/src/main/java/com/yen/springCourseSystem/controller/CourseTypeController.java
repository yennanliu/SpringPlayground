package com.yen.springCourseSystem.controller;

// book p. 258

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yen.springCourseSystem.bean.CourseType;
import com.yen.springCourseSystem.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/courseType")
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService;

    @GetMapping("/toInput")
    public String input(Map<String, Object> map){
        map.put("courseType", new CourseType());
        return "courseType/input_course_type";
    }

    @GetMapping("/create")
    public String create(CourseType courseType){
        courseTypeService.addCourseType(courseType);
        return "redirect:/courseType/list";
    }

    @GetMapping("/list")
    public String list(
            Map<String, Object> map,
            @RequestParam(value="pageNo", required = false, defaultValue = "1") String pageNoStr)
    {
        int pageNo = 1;
        pageNo = Integer.parseInt(pageNoStr);

        if (pageNo < 1){
            pageNo = 1;
           }

        PageHelper.startPage(pageNo, 10);
        List<CourseType> courseTypeList  = courseTypeService.loadAll();
        PageInfo<CourseType> page = new PageInfo<CourseType>(courseTypeList);
        map.put("page", page);
        return "courseType/list_course_type";
    }

    @DeleteMapping(value="/remove/{typeId}")
    public String remove(@PathVariable("typeId") Integer typeId){
        courseTypeService.removeCourseType(typeId);
        return "redirect:/courseType/list";
    }

    @GetMapping(value="/preUpdate/{typeId}")
    public String preUpdate(@PathVariable("typeId") Integer typeId, Map<String, Object> map) {
        CourseType courseType = courseTypeService.getCourseTypeById(typeId);
        System.out.println(">>> " + courseType);
        map.put("courseType", courseType);
        return "courseType/update_course_type";
    }

    @GetMapping(value="/update")
    public String update(CourseType courseType){
        courseTypeService.updateCourseType(courseType);
        return "redirect:/courseType/list";
    }

}
