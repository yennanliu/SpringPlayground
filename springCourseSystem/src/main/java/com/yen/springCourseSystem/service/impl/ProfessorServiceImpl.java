package com.yen.springCourseSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springCourseSystem.Util.ProfessorQueryHelper;
import com.yen.springCourseSystem.mapper.ProfessorMapper;
import com.yen.springCourseSystem.service.ProfessorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springCourseSystem.bean.Professor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@Log4j2
public class ProfessorServiceImpl extends ServiceImpl<ProfessorMapper, Professor> implements ProfessorService {

    @Autowired
    ProfessorMapper professorMapper;

    @Override
    public Page<Professor> getProfessorPage(ProfessorQueryHelper helper, Integer pageNo, Integer pageSize) {

        log.info(">>> getProfessorPage start ...");
        log.info(">>> pageNo = {}, pageSize = {}", pageNo, pageSize);

        Page<Professor> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Professor> professorWrapper = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(helper.getQryProfessorName())){
            professorWrapper.like("name", helper.getQryProfessorName());
        }

        List<Professor> courseList = baseMapper.getProfessorList(page, professorWrapper);

        courseList.forEach(x ->
                x.setName(String.valueOf(professorMapper.selectById(x.getId())))
        );

        log.info(">>> getProfessorPage end ...");

        if(CollectionUtils.isNotEmpty(courseList)){
            page.setRecords(courseList);
            return page;
        }

        return new Page<>();
    }

}
