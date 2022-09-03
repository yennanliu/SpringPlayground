package com.wudimanong.monitor.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class TestInfoPO implements Serializable {

    private Integer id;
    private String name;
    private Timestamp createTime;
    private Timestamp updateTime;
}
