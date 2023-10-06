package com.yen.springWarehouse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.util.ProductQueryHelper;

public interface ProductService extends IService<Product> {

    /** Pagination */
    Page<Product> getProductPage(ProductQueryHelper helper, Integer pageNo, Integer pageSize);
}
