package com.wudimanong.experiment.dao.model;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wudimanong.experiment.client.entity.AbtestGroup;
import com.wudimanong.experiment.utils.BucketUtils;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@TableName("abtest_group")
public class AbtestGroupPO {

    /**
     * 分组ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分组类别，0-实验组；1-对照组
     */
    private Integer groupType;

    /**
     * 分组后流量占比
     */
    private Integer flowRatio;

    /**
     * 实验ID
     */
    private Integer expId;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组类型，如0-区间分组；1-单双号分组
     */
    private Integer groupPartitionType;

    /**
     * 分流内包含的桶编号列表
     */
    private String groupPartitionDetails;

    /**
     * 策略对应JSON
     */
    private String strategyDetail;

    /**
     * 是否已上线
     */
    private Integer online;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 分流的稀释系数，实际分流为 flowRatio / dilutionRatio
     */
    private Integer dilutionRatio;

    /**
     * 白名单
     */
    private String whiteList;


    /**
     * 将数据库实体对象转换为接口实体对象
     *
     * @param abtestGroupPO
     * @return
     */
    public static AbtestGroup mapGroup(AbtestGroupPO abtestGroupPO) {
        //分流内包含的桶编号列表
        List<Integer> partitionSerialNums = null;
        if (abtestGroupPO.getGroupPartitionDetails() != null && !"".equals(abtestGroupPO.getGroupPartitionDetails())) {
            partitionSerialNums = Arrays.asList(abtestGroupPO.getGroupPartitionDetails().split(",")).stream()
                    .map(o -> Integer.valueOf(o)).collect(Collectors.toList());
        } else {
            partitionSerialNums = new ArrayList<>();
        }
        //判断桶数量大小是否>100，以此决定传输时是否启用压缩
        boolean useBase64Nums = partitionSerialNums.size() > 100;
        AbtestGroup group = new AbtestGroup();
        group.setExpId(abtestGroupPO.getExpId());
        group.setGroupId(abtestGroupPO.getId());
        group.setGroupType(abtestGroupPO.getGroupType());
        //是否使用Base64压缩
        group.setIsUseBase64Nums(useBase64Nums);
        //不需要Base64压缩则直接设置桶编号列表
        group.setPartitionSerialNums(useBase64Nums ? Collections.emptySet() : new HashSet<>(partitionSerialNums));
        //需要压缩则进行压缩操作
        group.setPartitionSerialNums64(useBase64Nums ? BucketUtils.bucketsToBitStr(partitionSerialNums) : "");
        group.setDilutionRatio(abtestGroupPO.getDilutionRatio());
        //设置分组实验策略信息
        group.setAbtestStrategies(JSON.parseObject(abtestGroupPO.getStrategyDetail(), List.class));
        group.setWhiteList(
                Objects.nonNull(abtestGroupPO.getWhiteList()) ? Arrays.asList(abtestGroupPO.getWhiteList().split(","))
                        : null);
        return group;
    }
}
