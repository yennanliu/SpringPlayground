package com.yen.gulimall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
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

        // if catalogId == 0, query all info
        if(catalogId == 0){
//            IPage<AttrGroupEntity> page = this.page(
//                    new Query<AttrGroupEntity>().getPage(params),
//                    new QueryWrapper<AttrGroupEntity>()
            return this.queryPage(params);
        }else{
            String key = (String) params.get("key");

            // select for 3 layer
            // query: SELECT * FROM pms_attr_group where catalog_id = ? and { attr_group_id = key or attr_group_name like %key% }
            // query which table, then use its entity (e.g. AttrGroupEntity for pms_attr_group table)
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>()
                    .eq("catalog_id", catalogId);

            // if key is not empty, keep adding conditions
            if(!StringUtils.isNotEmpty(key)){
                wrapper.and((obj) -> {
                    obj.eq("attr_group_id", key).or().like("attr_group_name", key); // like : %key_word%, likeLeft: %key_word, likeRight: key_word%
                });
            }

            // then use prepared wrapper
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper);

            return new PageUtils(page);
        }

    }

}