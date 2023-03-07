package com.yen.gulimall.product.service.impl;

import com.yen.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;

import com.yen.gulimall.product.dao.BrandDao;
import com.yen.gulimall.product.entity.BrandEntity;
import com.yen.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         *  update :
         *      - key SQL fuzzy matching
         *     - https://youtu.be/dG2Bo8noDtY?t=222
         */
        // 0) init query wrapper
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        // 1) get key
        String key = (String) params.get("key");
        // 2) if key not null, then have to do "fuzzy matching"
        if(!StringUtils.isNotEmpty(key)){
            queryWrapper
                    .eq("brand_id", key).or()
                    .like("name", key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *  Update
     *      - https://youtu.be/dG2Bo8noDtY?t=1118
     *      - not only update product info, but have to make sure data consistency (redundant attr)
     */
    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {

        // 1) make sure data consistency (redundant attr)
        this.updateById(brand);
        // 2) if brand name is not empty, means other tables also have such product info, we have to update there as well
        if(!StringUtils.isEmpty(brand.getName())){
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());
            // TODO : update other tables as well
        }

    }

}