package com.yen.springWarehouse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springWarehouse.bean.Merchant;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.util.MerchantQueryHelper;
import com.yen.springWarehouse.util.ProductQueryHelper;

import java.util.List;

public interface MerchantService extends IService<Merchant> {

    Page<Merchant> getMerchantPage(MerchantQueryHelper helper, Integer pageNo, Integer pageSize);

    List<Merchant> getAllMerchant();
}
