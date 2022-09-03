package com.wudimanong.efence.service.impl;

import com.wudimanong.efence.convert.FenceGeoConvert;
import com.wudimanong.efence.entity.bo.ContainPointBO;
import com.wudimanong.efence.entity.dto.ContainPointDTO;
import com.wudimanong.efence.utils.GeoJsonUtil;
import com.wudimanong.efence.dao.mapper.FenceGeoDao;
import com.wudimanong.efence.dao.model.FenceGeoInfoPO;
import com.wudimanong.efence.entity.bo.BatchImportGeoFenceBO;
import com.wudimanong.efence.entity.bo.GeoFenceBO;
import com.wudimanong.efence.entity.dto.BatchImportGeoFenceDTO;
import com.wudimanong.efence.service.FenceGeoService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.postgis.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class FenceGeoServiceImpl implements FenceGeoService {

    /**
     * 注入持久层组件
     */
    @Autowired
    FenceGeoDao fenceGeoDao;

    /**
     * 批量导入电子围栏业务层方法实现（事务方法）
     *
     * @param batchImportGeoFenceDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BatchImportGeoFenceBO> batchImportGeoFence(BatchImportGeoFenceDTO batchImportGeoFenceDTO) {
        //对批量导入的围栏数据进行数据校验及过滤(方法拆分)
        Map<String, List<GeoFenceBO>> validateFenceData = validateFenceData(batchImportGeoFenceDTO.getFences());
        //获取合法围栏数据请求列表
        List<GeoFenceBO> successFenceData = validateFenceData.get("success");
        if (successFenceData != null && successFenceData.size() > 0) {
            //通过MapStruct将BO数据转换为PO持久层对象数据
            List<FenceGeoInfoPO> successFenceDataPO = convertSuccessFenceData(batchImportGeoFenceDTO, successFenceData);
            //批量数据库导入围栏数据
            fenceGeoDao.batchInsert(successFenceDataPO);
        }
        //获取非法围栏数据请求列表
        List<GeoFenceBO> failedFenceData = validateFenceData.get("fail");
        //将导入失败数据转换为输出对象
        List<BatchImportGeoFenceBO> batchImportGeoFenceBOList = convertFailFenceData(batchImportGeoFenceDTO,
                failedFenceData);
        return batchImportGeoFenceBOList;
    }

    /**
     * 根据围栏ID查询围栏信息业务层方法
     *
     * @param fenceId
     * @return
     */
    @Override
    public GeoFenceBO getGeofenceById(Integer fenceId) {
        FenceGeoInfoPO fenceGeoInfoPO = fenceGeoDao.selectById(fenceId);
        GeoFenceBO geoFenceBO = null;
        if (fenceGeoInfoPO != null) {
            geoFenceBO = FenceGeoConvert.INSTANCE.convertFenceGeoBO(fenceGeoInfoPO);
            geoFenceBO.setRegionGeoJson(fenceGeoInfoPO.getRegion().toString());
        }
        return geoFenceBO;
    }

    /**
     * 判断坐标点是否在指定围栏之中
     *
     * @param containPointDTO
     * @return
     */
    @Override
    public ContainPointBO isContainPoint(ContainPointDTO containPointDTO) {
        String result = fenceGeoDao
                .isContainPoint(containPointDTO.getLon(), containPointDTO.getLat(), containPointDTO.getFenceId());
        ContainPointBO containPointBO;
        if ("f".equals(result)) {
            containPointBO = ContainPointBO.builder().result(new Boolean(false)).build();
        } else {
            containPointBO = ContainPointBO.builder().result(new Boolean(true)).build();
        }
        return containPointBO;
    }

    /**
     * 通过数据校验，分割合法围栏数据及不合法围栏数据
     *
     * @param fenceBOList
     * @return
     */
    private Map<String, List<GeoFenceBO>> validateFenceData(List<GeoFenceBO> fenceBOList) {
        //校验结果数据
        Map<String, List<GeoFenceBO>> validateResult = new HashMap<>();
        //合法数据
        List<GeoFenceBO> successGeoFenceBO = new ArrayList<>();
        //非法数据
        List<GeoFenceBO> failGeoFenceBO = new ArrayList<>();
        for (GeoFenceBO geoFenceBO : fenceBOList) {
            //todo 可以根据实际的业务场景对数据进行过滤，这里不做具体实现，直接返回原始输入数据
        }
        validateResult.put("success", fenceBOList);
        return validateResult;
    }

    /**
     * 将接口上送的正确电子围栏数据转换为PostgreSQL数据库持久层对象
     *
     * @param successFenceBOList
     * @return
     */
    private List<FenceGeoInfoPO> convertSuccessFenceData(BatchImportGeoFenceDTO batchImportGeoFenceDTO,
            List<GeoFenceBO> successFenceBOList) {
        List<FenceGeoInfoPO> fenceGeoInfoPOList = new ArrayList<>();
        for (GeoFenceBO geoFenceBO : successFenceBOList) {
            FenceGeoInfoPO fenceGeoInfoPO = FenceGeoConvert.INSTANCE.convertFenceGeoPO(geoFenceBO);
            //将GeoJson转换为Polygon数据类型后进行数据设置
            Polygon regionPolygon = GeoJsonUtil.convertPointArrayJsonToPolygon(geoFenceBO.getRegionGeoJson());
            fenceGeoInfoPO.setRegion(regionPolygon);
            //todo 通过GeoJson计算围栏的中心点（PostGis提供函数ST_Centroid(*)）,也可以通过geoTools工具包提供的方法进行计算
            //todo 通过GeoJson计算围栏面积（PostGis提供函数ST_Area(*)），也可以通过geoTools工具包提供的方法进行计算
            //设置导入批次ID
            fenceGeoInfoPO.setBatchId(batchImportGeoFenceDTO.getBatchId());
            fenceGeoInfoPOList.add(fenceGeoInfoPO);
        }
        return fenceGeoInfoPOList;
    }

    /**
     * 将校验失败的电子围栏数据进行错误结果对象转换
     *
     * @param batchImportGeoFenceDTO
     * @param failFenceBOList
     * @return
     */
    private List<BatchImportGeoFenceBO> convertFailFenceData(BatchImportGeoFenceDTO batchImportGeoFenceDTO,
            List<GeoFenceBO> failFenceBOList) {
        List<BatchImportGeoFenceBO> list = new ArrayList<>();
        //todo 将失败数据转换为接口输出格式，这里仅作示范不作实现
        return list;
    }
}
