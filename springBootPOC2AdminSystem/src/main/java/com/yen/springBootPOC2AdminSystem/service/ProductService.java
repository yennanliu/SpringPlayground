package com.yen.springBootPOC2AdminSystem.service;

// https://www.youtube.com/watch?v=Iz69cdsPFkA&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=64

import com.yen.springBootPOC2AdminSystem.bean.Product;
import com.yen.springBootPOC2AdminSystem.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    ProductMapper productMapper;

    public Product getProduct(long id){
        return productMapper.getProduct(id);
    }

}
