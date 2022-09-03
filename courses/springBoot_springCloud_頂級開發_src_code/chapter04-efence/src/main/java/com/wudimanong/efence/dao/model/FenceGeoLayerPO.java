package com.wudimanong.efence.dao.model;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@TableName("fence_geo_layer")
@KeySequence(value = "fence_geo_layer_id_seq")
public class FenceGeoLayerPO implements Serializable {

    private Integer id;
    private String code;
    private String name;
    private String explain;
    private String checkCity;
    private String cityCode;
    private Integer state;
    private Integer type;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createUser;
    private String updateUser;
}
