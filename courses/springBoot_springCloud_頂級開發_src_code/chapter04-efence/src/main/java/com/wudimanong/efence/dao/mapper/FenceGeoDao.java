package com.wudimanong.efence.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudimanong.efence.dao.model.FenceGeoInfoPO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author jiangqiao
 */
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
