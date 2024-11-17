package com.yen.efence.service.impl;

// book p.4-38
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/service/impl/FenceGeoLayerServiceImpl.java

import com.yen.efence.covert.FenceGeoLayerConvert;
import com.yen.efence.dao.mapper.FenceGeoLayerDao;
import com.yen.efence.dao.model.FenceGeoLayerPO;
import com.yen.efence.entity.BusinessCodeEnum;
import com.yen.efence.entity.bo.FenceGeoLayerBO;
import com.yen.efence.entity.dto.FenceGeoLayerSaveDTO;
import com.yen.efence.exception.FenceGeoLayerServiceException;
import com.yen.efence.service.FenceGeoLayerService;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FenceGeoLayerServiceImpl implements FenceGeoLayerService {

    @Autowired
    FenceGeoLayerDao fenceGeoLayerDao;

    /** add and save layer info */
    @Override
    public FenceGeoLayerBO save(FenceGeoLayerSaveDTO fenceGeoLayerSaveDTO) {

        Map<String, Object> map = new HashMap();
        map.put("code", fenceGeoLayerSaveDTO.getCode());
        List<FenceGeoLayerPO> layerPOList = fenceGeoLayerDao.selectByMap(map);
        // check if duplicated layer
        if (layerPOList != null && layerPOList.size() > 0){
            throw new FenceGeoLayerServiceException(
                    BusinessCodeEnum.BUSI_LAYER_FAIL_1000.getCode(),
                    BusinessCodeEnum.BUSI_LAYER_FAIL_1000.getDesc(),
                    fenceGeoLayerSaveDTO);
        }

        /** transform BO (business) data to PO (DB) data via MapStruct (less code) */
        FenceGeoLayerPO fenceGeoLayerPO = FenceGeoLayerConvert
                .INSTANCE.convertFenceGeoLayerPO(fenceGeoLayerSaveDTO);
        fenceGeoLayerPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fenceGeoLayerPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        // insert to DB via Mybatis and Mybatis-plus
        fenceGeoLayerDao.insert(fenceGeoLayerPO);

        /** transform PO (DB) data to BO (business) data */
        FenceGeoLayerBO fenceGeoLayerBO = FenceGeoLayerConvert
                .INSTANCE.convertFenceGeoLayerBO(fenceGeoLayerPO);
        fenceGeoLayerBO.setRegionalType(fenceGeoLayerSaveDTO.getRegionType());

        return fenceGeoLayerBO;
    }

    /** get single layer info */
    @Override
    public FenceGeoLayerBO getSingle(String code) {

        Map<String, Object> map = new HashMap();
        map.put("code", code);
        List<FenceGeoLayerPO> layerPOList = fenceGeoLayerDao.selectByMap(map);

        // transform PO (DB object) to BO (business object)
        FenceGeoLayerBO fenceGeoLayerBO = FenceGeoLayerConvert
                .INSTANCE.INSTANCE.convertFenceGeoLayerBO(layerPOList.get(0));

        return fenceGeoLayerBO;
    }

}
