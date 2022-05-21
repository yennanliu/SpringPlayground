package com.yen.springBootPOC2AdminSystem.mapper;

// https://www.youtube.com/watch?v=Iz69cdsPFkA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=64

import com.yen.springBootPOC2AdminSystem.bean.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 *  mapper for mybatis map Product
 *
 *  NOTE !!! we need @Mapper annotation
 *   -> so can avoid below errors
 *      1) Field productMapper in com.yen.springBootPOC2AdminSystem.service.ProductService required a bean of type 'com.yen.springBootPOC2AdminSystem.mapper.ProductMapper' that could not be found.
 *      2) Consider defining a bean of type 'com.yen.springBootPOC2AdminSystem.mapper.ProductMapper' in your configuration.
 */
@Mapper
public interface ProductMapper {

    public Product getProduct(long id);
}
