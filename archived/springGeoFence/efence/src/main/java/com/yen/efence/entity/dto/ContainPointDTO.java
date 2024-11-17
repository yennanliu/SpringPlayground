package com.yen.efence.entity.dto;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/entity/dto/ContainPointDTO.java

import java.io.Serializable;
import lombok.Data;

@Data
public class ContainPointDTO implements Serializable {

    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 围栏ID
     */
    private Integer fenceId;
}