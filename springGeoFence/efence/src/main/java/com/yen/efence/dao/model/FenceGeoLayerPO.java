package com.yen.efence.dao.model;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/dao/model/FenceGeoLayerPO.java

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

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