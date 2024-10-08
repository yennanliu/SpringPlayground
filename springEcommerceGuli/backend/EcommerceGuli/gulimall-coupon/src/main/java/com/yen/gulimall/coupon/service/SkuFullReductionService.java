package com.yen.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.gulimall.common.to.SkuReductionTo;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-03 09:12:35
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public void saveSkuReduction(SkuReductionTo skuReductionTo);
}

