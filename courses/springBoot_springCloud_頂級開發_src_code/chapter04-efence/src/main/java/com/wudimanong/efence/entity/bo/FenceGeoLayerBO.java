package com.wudimanong.efence.entity.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author jiangqiao
 */
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
