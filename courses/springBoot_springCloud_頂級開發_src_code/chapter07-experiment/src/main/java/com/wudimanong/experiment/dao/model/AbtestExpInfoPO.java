package com.wudimanong.experiment.dao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@TableName("abtest_exp_info")
public class AbtestExpInfoPO {

    /**
     * 实验ID，使用数据库自增机制
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 实验名称
     */
    private String name;

    /**
     * 系统对应的业务标签
     */
    private String factorTag;

    /**
     * 分层ID
     */
    private Integer layerId;

    /**
     * 分组字段（参数类型） 1 用户，2 地理位置；冗余字段，跟layer保持一致
     */
    private Integer groupFieldId;

    /**
     * 实验假设
     */
    private String expHyp;

    /**
     * 实验预期
     */
    private String resultExpect;

    /**
     * 指标ids
     */
    private String metricIds;

    /**
     * 实验开始时间
     */
    private Timestamp startTime;

    /**
     * 实验结束时间
     */
    private Timestamp endTime;

    /**
     * 实验状态，0 新建, 1 已发布, 2 生效中, 3 已暂停, 4 已终止, 5 已结束
     */
    private Integer status;

    /**
     * 是否已上线
     */
    private Integer online;

    /**
     * 分区类型，0 XX，1 XX
     */
    private Integer partitionType;

    /**
     * 是否抽样打点 0 否，1 是
     */
    private Integer isSampling;

    /**
     * 抽样率，例如 5 表示5%
     */
    private Integer samplingRatio;

    /**
     * 实验自定义配置信息
     */
    private String config;

    /**
     * 使用端系统名称，例如pay表示支付系统
     */
    private String serviceName;

    /**
     * 负责人信息
     */
    private String owner;

    /**
     * 是否已删除，0 未删除 1 已删除
     */
    private Integer isDelete;

    /**
     * 扩展信息
     */
    private String ext;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;
}
