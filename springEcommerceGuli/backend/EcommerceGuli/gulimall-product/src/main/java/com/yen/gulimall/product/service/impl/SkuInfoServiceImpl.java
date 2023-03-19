package com.yen.gulimall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;
import com.yen.gulimall.product.dao.SkuInfoDao;
import com.yen.gulimall.product.entity.SkuInfoEntity;
import com.yen.gulimall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * https://youtu.be/upNskS8E_7o?t=399
     */
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {

        this.baseMapper.insert(skuInfoEntity);
    }

    /**
     * https://youtu.be/9dqp-qapASU?t=69
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<SkuInfoEntity>();
        
        // check param before forming query wrapper
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w) -> {
                w.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(key)){
            wrapper.eq("catelog_id", catelogId);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)){
            wrapper.eq("brand_id", brandId);
        }

        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
            wrapper.ge("price", min);
        }

        String max = (String) params.get("max");
        if(!StringUtils.isEmpty(max)){
            wrapper.le("price", max);
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );
        
        return new PageUtils(page);
    }

}