package com.yen.springCourseSystem.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.bean.Professor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfessorMapper extends BaseMapper<Professor>{

    List<Professor> getProfessorList(Page<Professor> professorPage, @Param("ew") Wrapper<Professor> wrapper);
}
