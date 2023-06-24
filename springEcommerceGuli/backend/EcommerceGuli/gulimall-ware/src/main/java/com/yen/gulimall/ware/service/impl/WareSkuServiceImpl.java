package com.yen.gulimall.ware.service.impl;

import com.yen.gulimall.common.utils.R;
import com.yen.gulimall.common.vo.SkuHasStockVo;
import com.yen.gulimall.ware.feign.ProductFeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.yen.common.utils.PageUtils;
//import com.yen.common.utils.Query;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;
import com.yen.gulimall.ware.dao.WareSkuDao;
import com.yen.gulimall.ware.entity.WareSkuEntity;
import com.yen.gulimall.ware.service.WareSkuService;


/**
 *  Update:
 *      - https://youtu.be/O80QfTFlwSk?t=55
 */
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductFeignService productFeignService;

    // https://youtu.be/O80QfTFlwSk?t=81
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");

        if(!StringUtils.isEmpty(skuId)){
            // prepare SQL where condition
            queryWrapper.eq("sku_id", skuId);
        }

        String wareId = (String) params.get("wareId");

        if(!StringUtils.isEmpty(wareId)){
            // prepare SQL where condition
            queryWrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    // https://youtu.be/L83Bxqy8UEE?t=890
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {

        // 1) check is there is a record in ware system
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>()
                .eq("sku_id", skuId).eq("ware_id", wareId));

        if (wareSkuEntities == null || wareSkuEntities.size() == 0){
            // if not record in ware, add it
            WareSkuEntity SkuEntity = new WareSkuEntity();
            SkuEntity.setSkuId(skuId);
            SkuEntity.setWareId(wareId);
            SkuEntity.setStock(skuNum);
            SkuEntity.setStockLocked(0);
            // get sku name via feign client, if failed, NO NEED to roll back (NOT trigger transaction)
            // 1) approach 1) catch exception, so transaction NOT trigger if feign call failed
            // 2) approach 2) : TODO
            try{
                R info = productFeignService.info(skuId);
                if (info.getCode() == 0){
                    Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                    SkuEntity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){
            }
            wareSkuDao.insert(SkuEntity);
        }else{
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }

    /**
     * https://youtu.be/JRPBm5sK4Gg?t=239
     */
    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {

        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();

            System.out.println("skuId = " + skuId);
            System.out.println("vo = " + vo);

            // get SKU stock from DB
            long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count > 0);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

}