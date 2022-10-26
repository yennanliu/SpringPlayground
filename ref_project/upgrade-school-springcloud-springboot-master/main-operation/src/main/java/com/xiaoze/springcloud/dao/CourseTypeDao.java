package com.xiaoze.springcloud.dao;

import com.xiaoze.springcloud.entity.CourseType;
import com.xiaoze.springcloud.entity.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * CourseTypeDao
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@FeignClient(name="gateway-server")
public interface CourseTypeDao {

    /**
     * 新增一条课程类型记录
     *
     * @param courseType
     * @return String
     *
     */
    @PostMapping(value="/courseType/createCourseType")
    String addCourseType(CourseType courseType);

    /**
     * 删除一条课程类型记录
     *
     * @param typeId
     * @return CourseType
     *
     */
    @DeleteMapping(value="/courseType/removeCourseType/{typeId}")
    String removeCourseType(@PathVariable("typeId") Integer typeId);

    /**
     * 更新一条课程类型记录
     *
     * @param courseType
     * @return CourseType
     *
     */
    @PutMapping(value="/courseType/updateCourseType")
    String updateCourseType( CourseType courseType);

    /**
     * 获取一条课程类型记录
     *
     * @param typeId
     * @return CourseType
     *
     */
    @GetMapping(value="/courseType/getOneCourseType/{typeId}")
    CourseType getCourseTypeById(@PathVariable("typeId")Integer typeId);

    /**
     * 获取课程类型记录
     *
     * @param pageNo
     * @return List
     *
     */
    @PostMapping("/courseType/list/{pageNo}")
    Page<CourseType> loadByPageNo(@PathVariable("pageNo")Integer pageNo);

}
