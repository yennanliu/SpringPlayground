package com.wudimanong.experiment.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@TableName("abtest_layer")
public class AbtestLayerPO {

    /**
     * 分层ID，使用数据库自增机制
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分层名称
     */
    private String name;

    /**
     * 分层描述
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 流量分组字段类型（1 用户；2 地理位置 ...）
     */
    private Integer groupFieldId;

    /**
     * 当前流量层分桶总数
     */
    private Integer bucketTotalNum;

    /**
     * 未被使用的分桶编号列表
     */
    private String unusedBucketNos;

    /**
     * 分区类型（功能待扩展字段）
     */
    private Integer partitionType;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 是否已删除,0-未删除；1-已删除
     */
    private Integer isDelete;
}