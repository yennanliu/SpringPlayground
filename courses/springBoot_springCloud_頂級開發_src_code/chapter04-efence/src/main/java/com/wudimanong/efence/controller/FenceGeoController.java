package com.wudimanong.efence.controller;

import com.wudimanong.efence.entity.ResponseResult;
import com.wudimanong.efence.entity.bo.BatchImportGeoFenceBO;
import com.wudimanong.efence.entity.bo.ContainPointBO;
import com.wudimanong.efence.entity.bo.GeoFenceBO;
import com.wudimanong.efence.entity.dto.BatchImportGeoFenceDTO;
import com.wudimanong.efence.entity.dto.ContainPointDTO;
import com.wudimanong.efence.service.FenceGeoService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangqiao
 */
@Slf4j
@RestController
@RequestMapping("/fence")
public class FenceGeoController {

    @Autowired
    FenceGeoService fenceGeoServiceImpl;

    /**
     * 批量导入电子围栏数据
     *
     * @param batchImportGeoFenceDTO
     * @return
     */
    @PostMapping("/batchImportGeoFence")
    public ResponseResult<List<BatchImportGeoFenceBO>> batchImportGeoFence(
            @RequestBody @Validated BatchImportGeoFenceDTO batchImportGeoFenceDTO) {
        return ResponseResult.OK(fenceGeoServiceImpl.batchImportGeoFence(batchImportGeoFenceDTO));
    }

    /**
     * 根据围栏ID查询围栏信息
     *
     * @param fenceId
     * @return
     */
    @GetMapping("/getGeofenceById")
    public ResponseResult<GeoFenceBO> getGeofenceById(@RequestParam(value = "fenceId") Integer fenceId) {
        return ResponseResult.OK(fenceGeoServiceImpl.getGeofenceById(fenceId));
    }

    /**
     * 判断指定坐标是否在指定围栏之中
     *
     * @return
     */
    @GetMapping("/isContainPoint")
    public ResponseResult<ContainPointBO> isContainPoint(ContainPointDTO containPointDTO) {
        return ResponseResult.OK(fenceGeoServiceImpl.isContainPoint(containPointDTO));
    }
}
