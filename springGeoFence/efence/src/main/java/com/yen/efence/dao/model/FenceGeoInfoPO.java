package com.yen.efence.dao.model;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/dao/model/FenceGeoInfoPO.java

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Data;
import org.postgis.Point;
import org.postgis.Polygon;

@Data
@TableName("fence_geo_info")
@KeySequence(value = "fence_geo_id_seq")
public class FenceGeoInfoPO implements Serializable {

    private Integer id;
    private String name;
    private String explain;
    private String cityCode;
    private String adCode;
    private String layerCode;
    /**
     * GeoJson围栏区域定义
     */
    private Polygon region;
    /**
     * 围栏中心点
     */
    private Point centre;
    /**
     * 围栏面积，单位：平方米
     */
    private BigDecimal area;
    /**
     * 自定义JSON信息
     */
    private String customInfo;
    /**
     * 导入批次
     */
    private Integer batchId;
    /**
     * 来源围栏ID（父围栏）
     */
    private Integer fromId;
    /**
     * 冗余geoJson信息
     */
    private String geoJson;
    /**
     * 围栏覆盖的geoHash列表
     */
    private String[] geoHash;

    /**
     * 围栏生效日期区间（tstzrange）
     */
    private String dateRange;
    /**
     * 围栏在一天内的有效时间段
     */
    private String timeBucket;

    /**
     * 是否有效
     */
    private Integer state;

    private Timestamp updateTime;
    private Timestamp createTime;
    /**
     * 更新人
     */
    private Timestamp updateUser;
    /**
     * 创建人
     */
    private Timestamp createUser;
}