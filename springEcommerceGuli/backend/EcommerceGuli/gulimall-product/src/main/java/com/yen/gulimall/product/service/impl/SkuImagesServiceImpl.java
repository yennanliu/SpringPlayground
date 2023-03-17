package com.yen.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;

import com.yen.gulimall.product.dao.SkuImagesDao;
import com.yen.gulimall.product.entity.SkuImagesEntity;
import com.yen.gulimall.product.service.SkuImagesService;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * https://youtu.be/g-NuHtkP5JI?t=434
     */
    @Override
    public void saveImages(Long id, List<String> images) {

        // only save if images is not null
        if (images == null || images.size() == 0){

        }else{

            List<SkuImagesEntity> collect = images.stream().map((img) -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(id);
                skuImagesEntity.setImgUrl(img);
                return skuImagesEntity;
            }).collect(Collectors.toList());

            // save
            this.saveBatch(collect);
        }

    }

}