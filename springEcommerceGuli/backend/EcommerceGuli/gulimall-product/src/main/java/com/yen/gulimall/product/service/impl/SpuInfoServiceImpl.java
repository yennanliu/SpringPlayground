package com.yen.gulimall.product.service.impl;

import com.yen.gulimall.product.vo.SpuSaveVo;
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
import org.springframework.transaction.annotation.Transactional;


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

    // https://youtu.be/jYMptVJXIA8?t=227
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {

        // 1) save Spu basic info
        // 2) save Spu describe image
        // 3) save Spu image
        // 4) save Spu spec param: pms_product_attr_value
        // 4') save Spu credit info : sms_spu_bounds
        // 5) save current Spu's all Sku info
        //   5-1) sku basic info : pms_sku_info
        //   5-2) sku image info : pms_sku_images
        //   5-3) sku sales attr info : pms_sku_sales_attr_values
        //   5-4) sku promo info : gulimall_sas DB 's sms_sku_ladder, sku_full_reduction, sms_member_price

    }

}