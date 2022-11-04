package com.yen.efence.entity.bo;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/entity/bo/GeoFenceBO.java

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GeoFenceBO implements Serializable {

    /**
     * 围栏名称
     */
    @NotNull(message = "围栏名称不能为空")
    private String name;

    /**
     * 围栏描述
     */
    private String explain;

    /**
     * 城市编码
     */
    @NotNull(message = "归属城市编码不能为空")
    private String cityCode;

    /**
     * 管理归属区域编码
     */
    private String adCode;

    /**
     * 归属图层编码
     */
    @NotNull(message = "所属图层编码不能为空")
    private String layerCode;

    /**
     * 围栏的空间信息表示(尽量完全合法的GeoJson)
     */
    @NotNull(message = "围栏位置信息不能为空")
    private String regionGeoJson;

    /**
     * 自定义JSON（自定义围栏业务信息）
     */
    private String customInfo;

    /**
     * 围栏生效日期区间
     */
    private String dateRange;

    /**
     * 围栏生效时间区间
     */
    private String timeBucket;

    /**
     * 0：生效／1：已删除 ／2：失效（优先级低)
     */
    private Integer state;
}