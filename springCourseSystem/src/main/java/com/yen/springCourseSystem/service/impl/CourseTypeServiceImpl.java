package com.yen.springCourseSystem.service.impl;

// book p. 251

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.springCourseSystem.bean.CourseType;
import com.yen.springCourseSystem.mapper.CourseMapper;
import com.yen.springCourseSystem.mapper.CourseTypeMapper;
import com.yen.springCourseSystem.service.CourseTypeService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class CourseTypeServiceImpl implements CourseTypeService {

    @Resource
    CourseTypeMapper courseTypeMapper;

    @Resource
    CourseMapper courseMapper;

    @Override
    public void addCourseType(CourseType courseType) {
        courseTypeMapper.insert(courseType);
    }

    @Override
    public void removeCourseType(Integer typeId) {
        if (courseMapper.loadCourseByTypeId(typeId) != null){
            courseMapper.removeCourseByTypeId(typeId);
        }
        // delete record in course_type table with PK
        courseTypeMapper.deleteById(typeId);
    }

    @Override
    public void updateCourseType(CourseType courseType) {
        courseTypeMapper.updateById(courseType);
    }

    @Override
    public CourseType getCourseTypeById(Integer typeId) {
        return courseTypeMapper.selectById(typeId);
    }

    @Override
    public List<CourseType> loadAll() {
        return courseTypeMapper.selectAll();
    }

    @Override
    public void deleteCourseType(Integer typeId) {
    }

    @Override
    public boolean saveBatch(Collection<CourseType> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<CourseType> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<CourseType> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(CourseType entity) {
        return false;
    }

    @Override
    public CourseType getOne(Wrapper<CourseType> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<CourseType> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<CourseType> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<CourseType> getBaseMapper() {
        return null;
    }

    @Override
    public Class<CourseType> getEntityClass() {
        return null;
    }
}
