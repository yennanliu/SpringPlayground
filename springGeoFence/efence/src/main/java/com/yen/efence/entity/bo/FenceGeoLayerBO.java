package com.yen.efence.entity.bo;

// book p.4-22
// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/entity/bo/FenceGeoLayerBO.java

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class FenceGeoLayerBO {

    private Integer id;
    private String code;
    private String name;
    private String businessType;
    private String explain;
    private Integer regionalType;
    private String owner;

    /**
     * 格式化日期显示
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
}