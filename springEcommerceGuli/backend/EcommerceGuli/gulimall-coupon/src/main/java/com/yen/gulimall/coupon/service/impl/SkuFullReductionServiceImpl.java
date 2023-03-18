package com.yen.gulimall.coupon.service.impl;

import com.yen.gulimall.common.to.MemberPrice;
import com.yen.gulimall.common.to.SkuReductionTo;
import com.yen.gulimall.coupon.dao.SkuFullReductionDao;
import com.yen.gulimall.coupon.entity.MemberPriceEntity;
import com.yen.gulimall.coupon.entity.SkuFullReductionEntity;
import com.yen.gulimall.coupon.entity.SkuLadderEntity;
import com.yen.gulimall.coupon.service.MemberPriceService;
import com.yen.gulimall.coupon.service.SkuFullReductionService;
import com.yen.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;

@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {


    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }


    /**
     *  https://youtu.be/2Fgtxnc9ehQ?t=1175
     */
    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {

        // 1) save whole duduction info
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuLadderEntity.getSkuId());
        skuLadderEntity.setFullCount(skuLadderEntity.getFullCount());
        skuLadderEntity.setDiscount(skuLadderEntity.getDiscount());
        skuLadderEntity.setAddOther(skuLadderEntity.getAddOther());
        skuLadderService.save(skuLadderEntity);

        // 2) save sku promo : sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        this.save(skuFullReductionEntity);

        // 3) member info : sms_member_price
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> collect = memberPrice.stream().map((item) -> {
            MemberPriceEntity priceEntity = new MemberPriceEntity();
            priceEntity.setSkuId(skuReductionTo.getSkuId());
            priceEntity.setMemberLevelId(item.getId());
            priceEntity.setMemberLevelName(item.getName());
            priceEntity.setMemberPrice(item.getPrice());
            priceEntity.setAddOther(1);
            return priceEntity;
        }).collect(Collectors.toList());

        memberPriceService.saveBatch(collect);
    }
}