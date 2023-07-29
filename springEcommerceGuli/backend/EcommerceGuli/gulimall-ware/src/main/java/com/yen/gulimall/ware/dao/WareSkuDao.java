package com.yen.gulimall.ware.dao;

import com.yen.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-05 00:24:12
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);


    // https://youtu.be/JRPBm5sK4Gg?t=469
    // https://youtu.be/cMtrw90Ol6M?t=311 // use Long, for skuStock is null case
    Long getSkuStock(@Param("skuId") int skuId);
}
