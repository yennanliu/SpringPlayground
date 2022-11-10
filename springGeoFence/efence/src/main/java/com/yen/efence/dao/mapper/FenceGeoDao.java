package com.yen.efence.dao.mapper;

// book p.4-55
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/dao/mapper/FenceGeoDao.java

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.yen.efence.dao.model.FenceGeoInfoPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FenceGeoDao extends BaseMapper<FenceGeoInfoPO> {

    /**
     * 批量入库方法
     *
     * @param fenceGeoInfoPOList
     * @return
     */
    int batchInsert(List<FenceGeoInfoPO> fenceGeoInfoPOList);

    /**
     * PostGis函数判断坐标点是否在指定围栏
     *
     * @param lon
     * @param lat
     * @param fenceId
     * @return
     */
    String isContainPoint(@Param("lon") Double lon, @Param("lat") Double lat, @Param("fenceId") Integer fenceId);
}