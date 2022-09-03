package com.wudimanong.experiment.starter;

import com.wudimanong.experiment.client.entity.AbtestExp;
import com.wudimanong.experiment.client.entity.AbtestGroup;
import com.wudimanong.experiment.client.entity.bo.ConfigBO;
import com.wudimanong.experiment.starter.entity.AbtestInfo;
import com.wudimanong.experiment.starter.entity.MatchResult;
import com.wudimanong.experiment.starter.entity.MatchResult.RetrieveType;
import com.wudimanong.experiment.starter.feign.ExperimentFeignSource;
import com.wudimanong.experiment.starter.utils.AbtestUtils;
import com.wudimanong.experiment.utils.BucketUtils;
import java.util.Optional;

/**
 * @author jiangqiao
 */
public class ExperimentTemplate {

    /**
     * 设置实验Spring Cloud微服务Feign接口组件依赖
     */
    private ExperimentFeignSource experimentFeignSource;

    public ExperimentTemplate(ExperimentFeignSource experimentFeignSource) {
        this.experimentFeignSource = experimentFeignSource;
    }

    /**
     * 根据指定ID获取分流结果
     *
     * @param factorTag
     * @param currIdStr
     * @return
     */
    public AbtestInfo get(String factorTag, String currIdStr) {
        //获取实验结果匹配信息
        MatchResult matchResult = math(factorTag, currIdStr);
        //生成返回结果对象数据
        AbtestInfo abtestInfo = AbtestInfo.builder().factorTag(matchResult.getAbtestExp().getFactorTag())
                .paramId(currIdStr).result(matchResult).build();
        return abtestInfo;
    }

    /**
     * 实验业务标签+分流ID匹配方法
     *
     * @param factorTag
     * @param currIdStr
     */
    private MatchResult math(String factorTag, String currIdStr) {
        //以Feign的方式获取微服务实验配置信息
        ConfigBO result = experimentFeignSource.getDeliverConfig(factorTag);
        if (result == null) {
            //实验配置获取失败,返回空配置
            return MatchResult.builder().build();
        }
        //获取返回配置信息中的实验配置信息
        AbtestExp abtestExp = result.getAbtestExp();
        //计算当前层流程分桶编号（关键逻辑）
        Long currBucketNo = AbtestUtils.getBucketNo(currIdStr, abtestExp.getLayerId());
        //匹配实验配置信息中流量分组信息(以lambda语法的方式通过流过滤匹配来看看当前流量桶号匹配那个分组)
        AbtestGroup destGroup = Optional.ofNullable(abtestExp.getAbtestGroups().stream()
                .filter(lt -> lt.getIsUseBase64Nums() ? BucketUtils.bitStr2buckets(lt.getPartitionSerialNums64())
                        .contains(currBucketNo.intValue())
                        : lt.getPartitionSerialNums().contains(currBucketNo.intValue())).findFirst().get()).get();
        return MatchResult.builder().destGroup(destGroup).abtestExp(abtestExp).retrieveType(RetrieveType.BUCKET)
                .build();
    }
}
