package com.yen.springCourseSystem.controller;

// book p. 258

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springCourseSystem.bean.CourseType;
import com.yen.springCourseSystem.service.CourseService;
import com.yen.springCourseSystem.service.CourseTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("/courseType")
@Slf4j
public class CourseTypeController {

    @Autowired
    private CourseTypeService courseTypeService ;

    @Autowired
    private CourseService courseService ;

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

        courseTypeService.save(courseType);

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

        /*
         * 第一个参数：第几页;
         * 第二个参数：每页获取的条数.
         */
        // instead of LambdaQueryWrapper, use QueryWrapper
        // https://www.cxyzjd.com/article/sinat_34338162/108667109
        // https://blog.csdn.net/qq_41389354/article/details/112008695
        log.info("pageNo = {}", pageNo);
        Page<CourseType> page = new Page<>(pageNo,3);

        QueryWrapper<CourseType> queryWrapper = new QueryWrapper<>();
        //queryWrapper.isNotNull("typeId");
//        IPage<CourseType> iPage = courseTypeService.page(
//                page, queryWrapper);
        IPage<CourseType> iPage = courseTypeService.page(page,
                new LambdaQueryWrapper<CourseType>()
                        .orderByAsc(CourseType::getTypeId)
        );

        log.info(">>> iPage = " + iPage);
        log.info(">>> iPage.total = {}, iPage.getPages = {}",  iPage.getTotal(), iPage.getPages());
        map.put("page", iPage);

        return "courseType/list_course_type";
    }

    @DeleteMapping(value="/remove/{typeId}")
    public String remove(@PathVariable("typeId") Integer typeId) {

        courseTypeService.deleteCourseType(typeId);

        return "redirect:/courseType/list";
    }

    @GetMapping(value="/preUpdate/{typeId}")
    public String preUpdate(@PathVariable("typeId") Integer typeId, Map<String, Object> map) {

        log.info(">>> preUpdate typeId : {}, map = {}", typeId, map);
        CourseType courseType = courseTypeService.getById(typeId);
        log.info(">>> courseType = {}", courseType);
        map.put("courseType", courseType);

        return "courseType/update_course_type";
    }

    @PostMapping(value="/update")
    public String update(CourseType courseType) {

        log.info(">>> update course : {}", courseType);
        courseTypeService.updateById(courseType);

        return "redirect:/courseType/list";
    }

}
