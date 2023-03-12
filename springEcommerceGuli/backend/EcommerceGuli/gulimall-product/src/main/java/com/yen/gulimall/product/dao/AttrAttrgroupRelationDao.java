package com.yen.gulimall.product.dao;

import com.yen.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    // https://youtu.be/7JOhxs7lYbE?t=621
    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
