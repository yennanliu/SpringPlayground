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
import com.yen.gulimall.product.dao.ProductAttrValueDao;
import com.yen.gulimall.product.entity.ProductAttrValueEntity;
import com.yen.gulimall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * https://youtu.be/g-NuHtkP5JI?t=788
     */
    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> collect) {

        this.saveBatch(collect);
    }

    // https://youtu.be/MpNzMayYIXY?t=181
    @Override
    public List<ProductAttrValueEntity> baseAttrListForSpu(Long spuId) {

        List<ProductAttrValueEntity> entities = this.baseMapper.selectList(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId)
        );
        return entities;
    }

    // https://youtu.be/MpNzMayYIXY?t=405
    @Transactional
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities) {

        // 1) delete all attrs given SPU is corresponding with
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        // 2) update
        List<ProductAttrValueEntity> collect = entities.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());

        this.saveBatch(collect);
    }

}