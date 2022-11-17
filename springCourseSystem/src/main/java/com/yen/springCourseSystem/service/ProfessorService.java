package com.yen.springCourseSystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springCourseSystem.Util.CourseQueryHelper;
import com.yen.springCourseSystem.Util.ProfessorQueryHelper;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.bean.Professor;

public interface ProfessorService extends IService<Professor>{

    public Page<Professor> getProfessorPage(ProfessorQueryHelper helper, Integer pageNo, Integer pageSize);
}
