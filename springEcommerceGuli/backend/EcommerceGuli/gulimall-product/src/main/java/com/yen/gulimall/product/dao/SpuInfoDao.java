package com.yen.gulimall.product.dao;

import com.yen.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-01 08:23:29
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    // https://youtu.be/PZW2rOit2s8?t=1171
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
