package com.wudimanong.efence.convert;

import com.wudimanong.efence.dao.model.FenceGeoInfoPO;
import com.wudimanong.efence.entity.bo.GeoFenceBO;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangqiao
 */
@org.mapstruct.Mapper
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
