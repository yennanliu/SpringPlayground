package com.yen.springCourseSystem.controller;

// book p. 260

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yen.springCourseSystem.Util.CourseQueryHelper;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.bean.CourseType;
import com.yen.springCourseSystem.service.CourseService;
import com.yen.springCourseSystem.service.CourseTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/course")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseTypeService courseTypeService;

    @ModelAttribute // TODO : check what's this
    public void getCourse(@RequestParam(value="courseNo", required = false) String courseNo,
            Map<String, Object> map,
            Course course){

        log.info(">>> courseNo = {}, map = {}, course = {}", courseNo, map, course);
        course = courseService.loadCourseByNo(courseNo);
        if (courseNo != null && course != null){
            map.put("course", course);
        }
    }

    @GetMapping("/toInput")
    public String toInput(Map<String, Object> map, Course course){

        log.info(">>> map = {}, course = {}", map, course);
        List<CourseType> courses = courseTypeService.loadAll();
        log.info(">>> courses = {}", courses.toString());
        map.put("courseTypeList", courses);
        course.setCourseStatus("O");
        course.setCourseReqs(new String[]{"a", "b"});
        map.put("course", course);
        return "course/input_course";
    }

    @PostMapping(value = "/create")
    public String create(@RequestParam("courseTextBookPic")MultipartFile file,
            Course course,
            Map<String, Object> map) throws IOException {

        // read file, transform to binary array
        if (file != null){
            course.setCourseTextBookPic(file.getBytes());
        }
        try{
            courseService.addCourse(course);
            System.out.println(">>> create");
        }catch (Exception e){
            map.put("exceptionMessage", e.getMessage());
            map.put("courseTypeList", courseTypeService.loadAll());
            return "course/input_course";
        }
        return "redirect:/course/list";
    }

    @RequestMapping("/list")
    public String list(
            @RequestParam(value="pageNo", required = false, defaultValue = "1") String pageNoStr,
            Map<String, Object> map,
            CourseQueryHelper helper){
        int pageNo = 1;
        // check pageNo
        pageNo = Integer.parseInt(pageNoStr);
        if (pageNo < 1){
            pageNo = 1;
        }

        // paging setting
        PageHelper.startPage(pageNo, 4);
        List<Course> courseList = courseService.loadScopedCourses(helper);
        PageInfo<Course> page = new PageInfo<>(courseList);
        map.put("courseTypeList", courseTypeService.loadAll());
        map.put("page", page);
        map.put("helper", helper);
        return "course/list_course";
    }

    @DeleteMapping(value = "/remove/{courseNo}")
    public String remove(@PathVariable("courseNo") String courseNo){
        courseService.removeCourseByNo(courseNo);
        return "redirect:/course/list";
    }

    @GetMapping(value="/preUpdate/{courseNo}")
    public String preUpdate(
            @PathVariable("courseNo") String courseNo,
            Map<String, Object> map){

        map.put("course", courseService.loadCourseByNo(courseNo));
        map.put("courseTypeList", courseTypeService.loadAll());
        return "course/update";

    }

    @PostMapping(value="/update")
    public String update(
            @RequestParam("courseTextBookPic") MultipartFile file,
            Course course,
            Map<String, Object> map
                         ) throws IOException {
        // load multi-submit doc, transform all of them to binary array
        if (file.getBytes().length > 0){
            course.setCourseTextBookPic(file.getBytes());
        }
        try{
            courseService.updateCourse(course);
        }catch (Exception e){
            map.put("exceptionMessage", e.getMessage());
            map.put("courseTypeList", courseTypeService.loadAll());
        }
        return "redirect:/course/list";
    }

    @GetMapping("/getPic/{courseNo}")
    public String getPic(
            @PathVariable("courseNo") String courseNo,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        byte[] textBookPic = courseService.getTextBookPic(courseNo);
        if (textBookPic != null){
            // TODO : recheck this
            String path = String.valueOf(request.getSession().getServletContext().getRealPath("/pics/default.jpg"));
            FileInputStream fis = new FileInputStream(new File(path));
            // TODO : recheck this
            textBookPic = new byte[fis.available()];
            fis.read(textBookPic);
        }
        // send notification to browser, current sending is pic
        response.setContentType("image/jpeg");
        ServletOutputStream sos = response.getOutputStream();
        sos.write(textBookPic);
        sos.close();
        return null;
    }

}
