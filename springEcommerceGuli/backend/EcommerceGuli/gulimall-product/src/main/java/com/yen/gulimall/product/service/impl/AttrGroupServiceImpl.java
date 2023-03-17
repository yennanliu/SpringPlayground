package com.yen.gulimall.product.service.impl;

import com.yen.gulimall.product.entity.AttrEntity;
import com.yen.gulimall.product.service.AttrService;
import com.yen.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;

import com.yen.gulimall.product.dao.AttrGroupDao;
import com.yen.gulimall.product.entity.AttrGroupEntity;
import com.yen.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catalogId) {

        String key = (String) params.get("key");

        // select for 3 layer
        // query: SELECT * FROM pms_attr_group where catalog_id = ? and { attr_group_id = key or attr_group_name like %key% }
        // query which table, then use its entity (e.g. AttrGroupEntity for pms_attr_group table)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>(); // plz check table pms_attr_group for current name

        // if key is not empty, keep adding conditions
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key); // like : %key_word%, likeLeft: %key_word, likeRight: key_word%
            });
        }

        // if catalogId == 0, query all info
        if(catalogId == 0){
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }else{
            // then use prepared wrapper
            wrapper.eq("catelog_id", catalogId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper);

            return new PageUtils(page);
        }
    }

    /**
     *  Based catelogId, get all groups and their categories
     *      - https://youtu.be/zG9e_EVd4xw?t=299
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {

        // 1) get group info
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        // 2) get all categories
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map((item) -> {

            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item, attrsVo);

            List<AttrEntity> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);

            return attrsVo;
        }).collect(Collectors.toList());

        return collect;
    }

}