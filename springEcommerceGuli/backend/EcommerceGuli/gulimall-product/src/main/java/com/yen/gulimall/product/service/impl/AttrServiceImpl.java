package com.yen.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yen.gulimall.common.constant.ProductConstant;
import com.yen.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.yen.gulimall.product.dao.AttrGroupDao;
import com.yen.gulimall.product.dao.CategoryDao;
import com.yen.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.yen.gulimall.product.entity.AttrGroupEntity;
import com.yen.gulimall.product.entity.CategoryEntity;
import com.yen.gulimall.product.service.CategoryService;
import com.yen.gulimall.product.vo.AttrGroupRelationVo;
import com.yen.gulimall.product.vo.AttrRespVo;
import com.yen.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
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


    /**
     *  Update:
     *      - handle different type
     *      - https://youtu.be/Ga6NMrVkRDY?t=401
     */
    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {

        AttrEntity attrEntity = new AttrEntity();
        // it's not efficient to copy every attr from one to another,
        // -> use BeanUtils.copyProperties instead : BeanUtils.copyProperties(source_pojo, dest_pojo);
        //attrEntity.setAttrName(attr.getAttrName());
        BeanUtils.copyProperties(attr, attrEntity);

        // step 1) save basic info
        this.save(attrEntity);

        // step 2) save relation info
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null){ // only save if sales type (type == 1) // https://youtu.be/Tnhog8lflcc?t=362
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            relationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {

        // 0) init QueryWrapper
        QueryWrapper<AttrEntity> queryWrapper= new QueryWrapper();

        // 1) update queryWrapper per condition : catelogId
        if (catelogId != 0){
            queryWrapper
                    .eq("catelog_id", catelogId);
                    //.eq("attr_type", "base".equalsIgnoreCase(type)? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode(): ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()); // 三元運算符: if type == 1, then base type, if type == 0, then sales type
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
            if ("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity attrId = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId() != null) { // https://youtu.be/Tnhog8lflcc?t=335
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
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
     *  https://youtu.be/Ga6NMrVkRDY?t=609
     */
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {

        AttrRespVo respVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        // copy info from attrEntity to respVo
        BeanUtils.copyProperties(attrEntity, respVo);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            // 1) set up group info
            AttrAttrgroupRelationEntity attrGroupRelation = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrGroupRelation != null){
                respVo.setAttrGroupId(attrGroupRelation.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupRelation.getAttrGroupId());
                if (attrGroupEntity != null){
                    respVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
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
     * https://youtu.be/Ga6NMrVkRDY?t=648
     */
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {

        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){

            // 1) modify group relation
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


    /**
     * Get all basic relation with attrGroupId
     *  - https://youtu.be/7JOhxs7lYbE?t=162
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {

        List<AttrAttrgroupRelationEntity> entities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        // https://youtu.be/PFtMlUlCZgY?t=54
        if (attrIds == null || attrIds.size() == 0){
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    /**
     * https://youtu.be/7JOhxs7lYbE?t=404
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {

        //relationDao.delete(new QueryWrapper<>().eq("attr_id", 1L).eq("attr_group_id", 1L));
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        // batch delete
        relationDao.deleteBatchRelation(entities);
    }

    /**
     * get all categories (if no relation in current group)
     *  - https://youtu.be/PFtMlUlCZgY?t=196
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId) {

        // 1) current group can only have relation to its own category
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        // 2) current group can only have relation to the category which is NOT referenced by other group
        // 2-1: check groups under current category
        List<AttrGroupEntity> group = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId)
                //.ne("attr_group_id", attrGroupId) // .ne(): non-equal
        );

        // 2-2: check their (group) relation category
        List<Long> collect = group.stream().map((item) -> {
            return item.getAttrGroupId();}).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> groupId = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .in("attr_group_id", collect) // .in(): SQL where in a list
        );
        List<Long> attrIds = groupId.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        // 2-3: remove these categories from current category table
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()
                );
        if (attrIds != null && attrIds.size() > 0){
            wrapper.notIn("attr_id", attrIds); // only do "not in" if attrIds is not null
        }
        // for fuzzy matching
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((w) -> {
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        List<AttrEntity> attrEntities = this.baseMapper.selectList(wrapper);
        // NOTE this!!! : encapsulate result with paging
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    /**
     *  get all search attr from attrIds
     *      https://youtu.be/hnWlIHW1J0I?t=202
     */
    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {

        return baseMapper.selectSearchAttrIds(attrIds);
    }

}