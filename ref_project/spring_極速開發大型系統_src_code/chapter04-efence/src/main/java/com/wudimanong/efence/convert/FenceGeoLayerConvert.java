package com.wudimanong.efence.convert;

import com.wudimanong.efence.dao.model.FenceGeoLayerPO;
import com.wudimanong.efence.entity.bo.FenceGeoLayerBO;
import com.wudimanong.efence.entity.dto.FenceGeoLayerSaveDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangqiao
 */
@org.mapstruct.Mapper
public interface FenceGeoLayerConvert {

    FenceGeoLayerConvert INSTANCE = Mappers.getMapper(FenceGeoLayerConvert.class);

    /**
     * 图层新增方法业务对象到数据库对象的转换映射
     *
     * @param salesCouponChannelsDTO
     * @return
     */
    @Mappings({
            @Mapping(source = "businessType", target = "type"),
            @Mapping(source = "owner", target = "createUser")
    })
    FenceGeoLayerPO convertFenceGeoLayerPO(FenceGeoLayerSaveDTO salesCouponChannelsDTO);

    /**
     * 将图层数据库对象转换为业务层BO对象映射
     *
     * @param fenceGeoLayerPO
     * @return
     */
    @Mappings({
            @Mapping(source = "type", target = "businessType"),
            @Mapping(source = "createUser", target = "owner")
    })
    FenceGeoLayerBO convertFenceGeoLayerBO(FenceGeoLayerPO fenceGeoLayerPO);
}
