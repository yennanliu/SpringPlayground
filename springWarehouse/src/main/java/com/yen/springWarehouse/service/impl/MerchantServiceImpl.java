package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.Merchant;
import com.yen.springWarehouse.mapper.MerchantMapper;
import com.yen.springWarehouse.service.MerchantService;
import com.yen.springWarehouse.util.MerchantQueryHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    @Autowired
    MerchantMapper merchantMapper;

    @Override
    public Page<Merchant> getMerchantPage(MerchantQueryHelper helper, Integer pageNo, Integer pageSize) {

        Page<Merchant> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Merchant> merchantWrapper = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(helper.getQryMerchantName())){
            merchantWrapper.like("name", helper.getQryMerchantName());
        }

        if(helper.getQryMerchantCity()!=null){
            merchantWrapper.lambda().ge(Merchant::getCity, helper.getQryMerchantCity());
        }

        if(StringUtils.isNotEmpty(helper.getQryMerchantType())){
            merchantWrapper.like("type", helper.getQryMerchantType());
        }

        //List<Merchant> merchantList = baseMapper.getProductList(page, productWrapper);
        List<Merchant> merchantList = baseMapper.getMerchantList(page, merchantWrapper);

        if(CollectionUtils.isNotEmpty(merchantList)){
            page.setRecords(merchantList);
            return page;
        }

        return new Page<>();
    }

    @Override
    public List<Merchant> getAllMerchant() {
        return merchantMapper.getAllMerchant();
    }

}
