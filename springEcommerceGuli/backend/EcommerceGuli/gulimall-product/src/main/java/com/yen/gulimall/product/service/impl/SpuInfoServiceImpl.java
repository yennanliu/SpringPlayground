package com.yen.gulimall.product.service.impl;

import com.yen.gulimall.common.constant.ProductConstant;
import com.yen.gulimall.common.constant.ProductStatus;
import com.yen.gulimall.common.to.SkuReductionTo;
import com.yen.gulimall.common.to.SpuBoundTo;
import com.yen.gulimall.common.to.es.SkuEsModel;
import com.yen.gulimall.common.utils.R;
import com.yen.gulimall.common.vo.SkuHasStockVo;
import com.yen.gulimall.product.entity.*;
import com.yen.gulimall.product.feign.CouponFeignService;
import com.yen.gulimall.product.feign.SearchFeignService;
import com.yen.gulimall.product.feign.WareFeignService;
import com.yen.gulimall.product.service.*;
import com.yen.gulimall.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;
import com.yen.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SkuImagesService imagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService; // feign client call ware service controller


    @Autowired
    SearchFeignService searchFeignService;

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
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        // 2) save Spu describe image
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        // 3) save Spu image
        List<String> images = vo.getImages();
        imagesService.saveImages(infoEntity.getId(), images);

        // 4) save Spu spec param: pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map((attr) -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());

            AttrEntity id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(id.getAttrName());

            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());

            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);

        // 4') save Spu credit info : sms_spu_bounds
        // https://youtu.be/2Fgtxnc9ehQ?t=278
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        // make a feign client call (to promo service's endpoint)
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0){
           log.error("Remote (feign client) save Spu credit fail");
        }

        // 5) save current Spu's all Sku info
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0){
            skus.forEach((item) -> {

                // get default image
                String defaultImg = "";
                for (Images image: item.getImages()){
                    if (image.getDefaultImg() != 1){
                        defaultImg = image.getImgUrl();
                    }
                }

                // init entity, set its attrs
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                //   5-1) sku basic info : pms_sku_info
                // save Sku info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                // get Sku id for relative table info
                Long skuId = skuInfoEntity.getSkuId();

                // collect image info
                List<SkuImagesEntity> imageEntities = item.getImages().stream().map((img) -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter((entity) -> {
                    // not save to DB if Sku has no image url : https://youtu.be/b4XzdRd8ZHo?t=24
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //   5-2) sku image info : pms_sku_images
                skuImagesService.saveBatch(imageEntities);

                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> attrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                //   5-3) sku sales attr info : pms_sku_sales_attr_values
                // save
                skuSaleAttrValueService.saveBatch(attrValueEntities);

                //   5-4) sku promo info : gulimall_sas DB 's sms_sku_ladder, sku_full_reduction, sms_member_price
                // https://youtu.be/2Fgtxnc9ehQ?t=888
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                // only save meaningful info : https://youtu.be/b4XzdRd8ZHo?t=107
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) > 1){
                    R r2 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r2.getCode() != 0){
                        log.error("Remote (feign client) save Sku promo fail");
                    }
                }
            });
        }

    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {

        this.baseMapper.insert(infoEntity);
    }

    /**
     * https://youtu.be/eAZGC-9QbaY?t=109
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<SpuInfoEntity>();

        // check param before forming query condition
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key); // NOTE this logic: status = 1 and (id = 1 or spu_name like xxx) , https://youtu.be/eAZGC-9QbaY?t=393
            });
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)){
           wrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catelog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 商品上架
     * https://youtu.be/X-ToZ1RIH4A?t=133
     */
    @Override
    public void up(Long spuId) {

        // step 1) prepare needed data in ES
        SkuEsModel skuEsModel = new SkuEsModel();
        // get all sku info with spuId
        List<SkuInfoEntity> skus =  skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skus.stream().map(sku -> {
            return sku.getSkuId();
        }).collect(Collectors.toList());

        log.debug(">>> skuIdList =" + skuIdList + " size = " + skuIdList.size());

        // TODO : get all sku attr which can be accessed
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        // make feign remote call, to check if such sku has stock
        Map<Long, Boolean> stockMap = null;
        try{
            R<List<SkuHasStockVo>> skuHasStock = wareFeignService.getSkuHasStock(skuIdList);
            // transform List<SkuHasStockVo> to Map<Long, Boolean>, so we can get its Map value by its key
            stockMap = skuHasStock.getData().stream().collect(
                    Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock())
            );
        }catch (Exception e){
            log.error("Ware feign call failed. Exception : {}" + e);
        }

        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        Set<Long> idSet = new HashSet<>(searchAttrIds); // transform to Set, for below filter

        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            Long attrId = item.getAttrId();
            return idSet.contains(attrId);
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());

        // step 2) setup attr value in every sku
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, esModel);
            // manually setup attr has different name in SkuInfoEntity, SkuEsModel
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());

            if (finalStockMap == null){
                esModel.setHasStock(true);
            }else{
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            // TODO : add default value to hotScore
            esModel.setHotScore(0L);

            System.out.println(">>> esModel = " + esModel);

            // get brand, category name
            BrandEntity brand = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());
            CategoryEntity category = categoryService.getById(esModel.getCatelogId());
            esModel.setCatelogName(category.getName());

            // set search attr
            esModel.setAttrs(attrsList);

            return esModel;
        }).collect(Collectors.toList());

        // save data to ES
        // https://youtu.be/PZW2rOit2s8?t=9
        // https://youtu.be/PZW2rOit2s8?t=912
        R r = searchFeignService.productStatusUp(upProducts);
        if (r.getCode() == 0){
            // feign call success
            // update SPU status in DB
            baseMapper.updateSpuStatus(spuId, ProductStatus.AttrEnum.SPU_UP.getCode());
        }else{
            // feign call fail
            // TODO : endpoint idempotence (接口冪等性), result should be the same if endpoint called several times
            log.error("(SpuInfoServiceImpl) feign call failed");
        }
    }

}