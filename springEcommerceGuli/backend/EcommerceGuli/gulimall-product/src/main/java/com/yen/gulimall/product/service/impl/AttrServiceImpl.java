package com.yen.gulimall.product.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.yen.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.yen.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.yen.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;

import com.yen.gulimall.product.dao.AttrDao;
import com.yen.gulimall.product.entity.AttrEntity;
import com.yen.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {

        AttrEntity attrEntity = new AttrEntity();
        // it's too slow, to copy every attr from one to another,
        // -> use BeanUtils.copyProperties instead : BeanUtils.copyProperties(source_pojo, dest_pojo);
        //attrEntity.setAttrName(attr.getAttrName());
        BeanUtils.copyProperties(attr, attrEntity);

        // step 1) save basic info
        this.save(attrEntity);

        // step 2) save relation info
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

}