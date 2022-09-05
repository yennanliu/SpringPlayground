package com.wudimanong.efence.service.impl;

import com.wudimanong.efence.convert.FenceGeoLayerConvert;
import com.wudimanong.efence.dao.mapper.FenceGeoLayerDao;
import com.wudimanong.efence.dao.model.FenceGeoLayerPO;
import com.wudimanong.efence.entity.BusinessCodeEnum;
import com.wudimanong.efence.entity.bo.FenceGeoLayerBO;
import com.wudimanong.efence.entity.dto.FenceGeoLayerSaveDTO;
import com.wudimanong.efence.exception.FenceGeoLayerServiceException;
import com.wudimanong.efence.service.FenceGeoLayerService;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class FenceGeoLayerServiceImpl implements FenceGeoLayerService {

    /**
     * 依赖注入持久层操作对象
     */
    @Autowired
    FenceGeoLayerDao fenceGeoLayerDao;

    /**
     * 图层信息新增保存业务层方法
     *
     * @param fenceGeoLayerSaveDTO
     * @return
     */
    @Override
    public FenceGeoLayerBO save(FenceGeoLayerSaveDTO fenceGeoLayerSaveDTO) {
        //根据code判断是否重复
        Map map = new HashMap<>();
        map.put("code", fenceGeoLayerSaveDTO.getCode());
        List<FenceGeoLayerPO> layerPOList = fenceGeoLayerDao.selectByMap(map);
        if (layerPOList != null && layerPOList.size() > 0) {
            throw new FenceGeoLayerServiceException(BusinessCodeEnum.BUSI_LAYER_FAIL_1000.getCode(),
                    BusinessCodeEnum.BUSI_LAYER_FAIL_1000.getDesc(), fenceGeoLayerSaveDTO);
        }
        //将业务层数据转换为数据库层对象，这里通过MapStruct工具进行转换(减少数据转换代码量)
        FenceGeoLayerPO fenceGeoLayerPO = FenceGeoLayerConvert.INSTANCE
                .convertFenceGeoLayerPO(fenceGeoLayerSaveDTO);
        fenceGeoLayerPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fenceGeoLayerPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        //完成Mybatis及MyBatis-Plus支持的数据库Insert操作
        fenceGeoLayerDao.insert(fenceGeoLayerPO);

        //将数据库对象转换为业务层输出BO对象
        FenceGeoLayerBO fenceGeoLayerBO = FenceGeoLayerConvert.INSTANCE
                .convertFenceGeoLayerBO(fenceGeoLayerPO);
        fenceGeoLayerBO.setRegionalType(fenceGeoLayerSaveDTO.getRegionType());
        return fenceGeoLayerBO;
    }

    /**
     * 单条图层信息获取方法
     *
     * @param code
     * @return
     */
    @Override
    public FenceGeoLayerBO getSingle(String code) {
        Map map = new HashMap<>();
        map.put("code", code);
        List<FenceGeoLayerPO> layerPOList = fenceGeoLayerDao.selectByMap(map);
        //将数据库对象转换为业务层输出BO对象
        FenceGeoLayerBO fenceGeoLayerBO = FenceGeoLayerConvert.INSTANCE
                .convertFenceGeoLayerBO(layerPOList.get(0));
        return fenceGeoLayerBO;
    }
}
