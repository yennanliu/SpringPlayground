package com.yen.springWarehouse.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Product;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

  /** get info for pagination */
  // TODO : check @Param("ew") ??
  List<Product> getProductList(Page<Product> productPage, @Param("ew") Wrapper<Product> wrapper);
}
