package com.yen.springWarehouse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.dto.ProductDto;
import com.yen.springWarehouse.util.ProductQueryHelper;

import java.util.List;

public interface ProductService extends IService<Product> {

    /** Pagination */
    Page<Product> getProductPage(ProductQueryHelper helper, Integer pageNo, Integer pageSize);

    List<ProductDto> getProductDtoListFromProductList(List<Product> productList);

    void deduct(Integer id);
}
