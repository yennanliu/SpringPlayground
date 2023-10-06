package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.mapper.ProductMapper;
import com.yen.springWarehouse.mapper.ProductTypeMapper;
import com.yen.springWarehouse.service.ProductService;
import com.yen.springWarehouse.util.ProductQueryHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public Page<Product> getProductPage(ProductQueryHelper helper, Integer pageNo, Integer pageSize) {

        Page<Product> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Product> courseWrapper = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(helper.getQryProductName())){
            courseWrapper.like("product_name", helper.getQryProductName());
        }

        if(helper.getQryStartPrice()!=null){
            courseWrapper.lambda().ge(Product::getPrice, helper.getQryStartPrice());
        }

        if(helper.getQryEndPrice()!=null){
            courseWrapper.lambda().le(Product::getPrice, helper.getQryEndPrice());
        }

        if(StringUtils.isNotEmpty(helper.getQryProductType())){
            courseWrapper.like("type_id", Integer.parseInt(helper.getQryProductType()));
        }

        List<Product> courseList = baseMapper.getProductList(page, courseWrapper);

        // TODO : fix below
//        courseList.forEach(_prod ->
//                //course.setCourseType(courseTypeMapper.selectById(course.getTypeId()))
//                _prod.setTypeId(productTypeMapper.selectById(_prod.getTypeId()))
//        );

        if(CollectionUtils.isNotEmpty(courseList)){
            page.setRecords(courseList);
            return page;
        }

        return new Page<>();

    }

}
