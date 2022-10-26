package com.xiaoze.springcloud.controller;

import com.xiaoze.springcloud.entity.CourseType;
import com.xiaoze.springcloud.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * CourseTypeController
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Controller
@RequestMapping("/courseType")
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService ;

    /**
     * 访问课程类型输入界面
     */
    @GetMapping("/toInput")
    public String input(Map<String, Object> map) {
        map.put("courseType", new CourseType());

        return "courseType/input_course_type";
    }

    /**
     * 创建新课程类型
     */
    @PostMapping(value="/create")
    public String create(CourseType courseType) {

        courseTypeService.addCourseType(courseType);

        return "redirect:/courseType/list";

    }

    @GetMapping("/list")
    public String list(Map<String, Object> map, @RequestParam(value="pageNo", required=false, defaultValue="1") String pageNoStr) {

        int pageNo = 1;

        //对 pageNo 的校验
        pageNo = Integer.parseInt(pageNoStr);
        if(pageNo < 1){
            pageNo = 1;
        }

        map.put("page", courseTypeService.loadByPageNo(pageNo));

        return "courseType/list_course_type";
    }

    @DeleteMapping(value="/remove/{typeId}")
    public String remove(@PathVariable("typeId") Integer typeId) {

        courseTypeService.removeCourseType(typeId);

        return "redirect:/courseType/list";
    }

    @GetMapping(value="/preUpdate/{typeId}")
    public String preUpdate(@PathVariable("typeId") Integer typeId, Map<String, Object> map) {
        System.out.println(courseTypeService.getCourseTypeById(typeId));
        map.put("courseType", courseTypeService.getCourseTypeById(typeId));

        return "courseType/update_course_type";
    }

    @PutMapping(value="/update")
    public String update(CourseType courseType) {

        courseTypeService.updateCourseType(courseType);

        return "redirect:/courseType/list";
    }

}
