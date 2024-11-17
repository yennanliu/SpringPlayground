package com.yen.efence.covert;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/convert/FenceGeoConvert.java

import com.yen.efence.dao.model.FenceGeoInfoPO;
import com.yen.efence.entity.bo.GeoFenceBO;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

// TODO : understand this method
@org.mapstruct.Mapper // NOTE here
public interface FenceGeoConvert {

    FenceGeoConvert INSTANCE = Mappers.getMapper(FenceGeoConvert.class);

    /**
     * 图层新增方法业务对象到数据库对象的转换映射
     *
     * @param geoFenceBO
     * @return
     */
    @Mappings({})
    FenceGeoInfoPO convertFenceGeoPO(GeoFenceBO geoFenceBO);

    /**
     * 围栏数据库对象转换为接口BO对象
     *
     * @param fenceGeoInfoPO
     * @return
     */
    @Mappings({})
    GeoFenceBO convertFenceGeoBO(FenceGeoInfoPO fenceGeoInfoPO);
}