package com.wudimanong.efence.controller;

import com.wudimanong.efence.entity.ResponseResult;
import com.wudimanong.efence.entity.bo.FenceGeoLayerBO;
import com.wudimanong.efence.entity.dto.FenceGeoLayerSaveDTO;
import com.wudimanong.efence.service.FenceGeoLayerService;
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
@RequestMapping("/fence/layer")
public class FenceGeoLayerController {

    @Autowired
    FenceGeoLayerService fenceGeoLayerServiceImpl;

    /**
     * 新增图层接口定义
     *
     * @param fenceGeoLayerSaveDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseResult<FenceGeoLayerBO> save(@RequestBody @Validated FenceGeoLayerSaveDTO fenceGeoLayerSaveDTO) {
        return ResponseResult.OK(fenceGeoLayerServiceImpl.save(fenceGeoLayerSaveDTO));
    }

    /**
     * 查询图层接口定义
     *
     * @param code
     * @return
     */
    @GetMapping("/getSingle")
    public ResponseResult<FenceGeoLayerBO> getSingle(
            @RequestParam(value = "code") String code) {
        return ResponseResult.OK(fenceGeoLayerServiceImpl.getSingle(code));
    }
}
