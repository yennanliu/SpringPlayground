package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.mapper.ProductMapper;
import com.yen.springWarehouse.mapper.ProductTypeMapper;
import com.yen.springWarehouse.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// NOTE below !!!
// -> we don't implement every abstract method in  ProductTypeService, but we extend ServiceImpl first
// then implement rest of the abstract methods
@Service
//@Async("getMineAsync")
@Transactional(rollbackFor = Exception.class)
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements ProductTypeService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public void deleteProductType(Integer typeId) {

        // delete product
        productMapper.delete(
                new LambdaQueryWrapper<Product>()
                        .eq(Product::getTypeId, typeId)
        );
        // delete product type
        baseMapper.deleteById(typeId);
    }

}
