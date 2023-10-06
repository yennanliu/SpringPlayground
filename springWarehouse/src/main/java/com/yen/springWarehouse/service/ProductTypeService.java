package com.yen.springWarehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springWarehouse.bean.ProductType;

public interface ProductTypeService extends IService<ProductType> {

    void deleteProductType(Integer typeId);
}
