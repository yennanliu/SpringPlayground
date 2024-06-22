package com.yen.springWarehouse.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Merchant;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantMapper extends BaseMapper<Merchant> {

  List<Merchant> getMerchantList(
      Page<Merchant> merchantPage, @Param("ew") QueryWrapper<Merchant> wrapper);
}
