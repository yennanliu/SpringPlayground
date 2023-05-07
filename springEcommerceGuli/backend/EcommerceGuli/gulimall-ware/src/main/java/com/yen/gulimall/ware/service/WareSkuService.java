package com.yen.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
//import com.yen.common.utils.PageUtils;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.vo.SkuHasStockVo;
import com.yen.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-05 00:24:12
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

