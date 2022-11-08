package com.yen.efence.dao.mapper;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/dao/mapper/FenceGeoLayerDao.java

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.efence.dao.model.FenceGeoLayerPO;
import org.springframework.stereotype.Repository;


@Repository
public interface FenceGeoLayerDao extends BaseMapper<FenceGeoLayerPO> {

}