package com.yen.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yen.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.yen.gulimall.product.dao.AttrGroupDao;
import com.yen.gulimall.product.dao.CategoryDao;
import com.yen.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.yen.gulimall.product.entity.AttrGroupEntity;
import com.yen.gulimall.product.entity.CategoryEntity;
import com.yen.gulimall.product.service.CategoryService;
import com.yen.gulimall.product.vo.AttrRespVo;
import com.yen.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

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
        relationDao.insert(relationEntity);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {

        // 0) init QueryWrapper
        QueryWrapper<AttrEntity> queryWrapper= new QueryWrapper();

        // 1) update queryWrapper per condition : catelogId
        if (catelogId != 0){
            queryWrapper.eq("catelogId", catelogId);
        }

        String key = (String) params.get("key");
        // 2) update queryWrapper per condition : key
        if (!StringUtils.isEmpty(key)){
            // attr_id, attr_name
            queryWrapper.and( (wrapper) -> {
                wrapper.eq("attr_id", key).or()
                       .like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);

        // 3) modify records data, so can have needed attr
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map(attrEntity -> {

            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            // setup group name and group
            AttrAttrgroupRelationEntity attrId = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (attrId != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    /**
     *  https://youtu.be/kCjMunm_9Ig?t=86
     */
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {

        AttrRespVo respVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        // copy info from attrEntity to respVo
        BeanUtils.copyProperties(attrEntity, respVo);

        // 1) set up group info
        AttrAttrgroupRelationEntity attrGroupRelation = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if (attrGroupRelation != null){
            respVo.setAttrGroupId(attrGroupRelation.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupRelation.getAttrGroupId());
            if (attrGroupEntity != null){
                respVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }

        // 2) set up category info
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        respVo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null){
            respVo.setCatelogName(categoryEntity.getName());
        }

        return respVo;
    }

    /**
     * https://youtu.be/kCjMunm_9Ig?t=462
     */
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {

        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());

        Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));

        // case 1) if existed -> modify
        if (count > 0){
            // modify group relation
            /** NOTE !!! : UpdateWrapper here */
            relationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        }else{ // case 2) if not existed -> add a new one
            relationDao.insert(relationEntity);
        }
    }

}