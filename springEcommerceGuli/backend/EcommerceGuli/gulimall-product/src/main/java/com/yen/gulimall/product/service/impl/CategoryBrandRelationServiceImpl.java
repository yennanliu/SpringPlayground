package com.yen.gulimall.product.service.impl;

import com.yen.gulimall.product.dao.BrandDao;
import com.yen.gulimall.product.dao.CategoryDao;
import com.yen.gulimall.product.entity.BrandEntity;
import com.yen.gulimall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;

import com.yen.gulimall.product.dao.CategoryBrandRelationDao;
import com.yen.gulimall.product.entity.CategoryBrandRelationEntity;
import com.yen.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    BrandDao brandDao;

    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    // https://youtu.be/dG2Bo8noDtY?t=907
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {

        Long brandID = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        // 1) query detail name
        BrandEntity brandEntity = brandDao.selectById(catelogId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        // 2) save to DB
        this.save(categoryBrandRelation);
    }

}