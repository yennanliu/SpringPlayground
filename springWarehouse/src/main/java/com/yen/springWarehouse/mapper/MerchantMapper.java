package com.yen.springWarehouse.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Merchant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantMapper extends BaseMapper<Merchant> {

    List<Merchant> getMerchantList(Page<Merchant> merchantPage, @Param("ew") QueryWrapper<Merchant> wrapper);

    List<Merchant> getAllMerchant();
}
