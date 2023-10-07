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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public Page<Product> getProductPage(ProductQueryHelper helper, Integer pageNo, Integer pageSize) {

        Page<Product> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Product> productWrapper = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(helper.getQryProductName())){
            productWrapper.like("product_name", helper.getQryProductName());
        }

        if(helper.getQryStartPrice()!=null){
            productWrapper.lambda().ge(Product::getPrice, helper.getQryStartPrice());
        }

        if(helper.getQryEndPrice()!=null){
            productWrapper.lambda().le(Product::getPrice, helper.getQryEndPrice());
        }

        if(StringUtils.isNotEmpty(helper.getQryProductType())){
            productWrapper.like("type_id", Integer.parseInt(helper.getQryProductType()));
        }

        List<Product> productList = baseMapper.getProductList(page, productWrapper);

        // TODO : fix below
//        productList.forEach(_prod ->
//                //product.setproductType(productTypeMapper.selectById(product.getTypeId()))
//                _prod.setTypeId(productTypeMapper.selectById(_prod.getTypeId()))
//        );

        if(CollectionUtils.isNotEmpty(productList)){
            page.setRecords(productList);
            return page;
        }

        return new Page<>();

    }

}
