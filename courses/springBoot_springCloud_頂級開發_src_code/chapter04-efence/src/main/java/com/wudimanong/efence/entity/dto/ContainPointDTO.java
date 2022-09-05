package com.wudimanong.efence.entity.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author jiangqiao
 */
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
