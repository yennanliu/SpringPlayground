package com.yen.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.product.entity.SkuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long id, List<String> images);
}

