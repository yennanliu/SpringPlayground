package com.yen.springCourseSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springCourseSystem.Util.CourseQueryHelper;
import com.yen.springCourseSystem.Util.ProfessorQueryHelper;
import com.yen.springCourseSystem.bean.Course;
import com.yen.springCourseSystem.mapper.CourseTypeMapper;
import com.yen.springCourseSystem.mapper.ProfessorMapper;
import com.yen.springCourseSystem.service.ProfessorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springCourseSystem.bean.Professor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProfessorServiceImpl extends ServiceImpl<ProfessorMapper, Professor> implements ProfessorService {

    @Autowired
    ProfessorMapper professorMapper;

    @Override
    public Page<Professor> getProfessorPage(ProfessorQueryHelper helper, Integer pageNo, Integer pageSize) {

        Page<Professor> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Professor> professorWrapper = new QueryWrapper<>();

        List<Professor> courseList = baseMapper.getProfessorList(page, professorWrapper);

        courseList.forEach(x ->
                x.setName(String.valueOf(professorMapper.selectById(x.getId())))
        );

        if(CollectionUtils.isNotEmpty(courseList)){
            page.setRecords(courseList);
            return page;
        }

        return new Page<>();
    }

}
