package com.yen.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;
import com.yen.gulimall.product.dao.SpuInfoDao;
import com.yen.gulimall.product.entity.SpuInfoEntity;
import com.yen.gulimall.product.service.SpuInfoService;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

}