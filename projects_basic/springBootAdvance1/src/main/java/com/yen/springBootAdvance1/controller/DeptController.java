package com.yen.springBootAdvance1.controller;

// https://www.youtube.com/watch?v=FhlRZRshF14&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=13

import com.yen.springBootAdvance1.bean.Department;
import com.yen.springBootAdvance1.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeptController {

    @Autowired
    DeptService deptService;

    @GetMapping("/dept/{id}")
    public Department getDept(@PathVariable Integer id){
        return deptService.getDeptById(id);
    }

}
