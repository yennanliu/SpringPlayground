package com.wudimanong.experiment.convert;

import com.wudimanong.experiment.client.entity.AbtestExp;
import com.wudimanong.experiment.client.entity.AbtestExpInfo;
import com.wudimanong.experiment.client.entity.bo.ConfigBO;
import com.wudimanong.experiment.dao.model.AbtestExpInfoPO;
import com.wudimanong.experiment.dao.model.AbtestGroupPO;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

/**
 * @author jiangqiao
 */
@Mapper
public interface AbtestExpConvert {

    AbtestExpConvert INSTANCE = Mappers.getMapper(AbtestExpConvert.class);

    /**
     * 实验持久层信息列表转换为服务层输出数据对象
     *
     * @param abtestExpInfoPOList
     * @return
     */
    @Mappings({})
    List<AbtestExpInfo> convertAbtestInfo(List<AbtestExpInfoPO> abtestExpInfoPOList);

    /**
     * 更加实验及分组信息转换SDK配置获取输出
     *
     * @param abtestExpInfoPO
     * @param groupPOList
     * @return
     */
    default ConfigBO convertConfig(AbtestExpInfoPO abtestExpInfoPO, List<AbtestGroupPO> groupPOList) {
        AbtestExp abtestExp = new AbtestExp();
        abtestExp.setExpId(abtestExpInfoPO.getId());
        abtestExp.setFactorTag(abtestExpInfoPO.getFactorTag());
        abtestExp.setLayerId(abtestExpInfoPO.getLayerId());
        abtestExp.setGroupFieldId(abtestExpInfoPO.getGroupFieldId());
        abtestExp.setIsSampling(abtestExpInfoPO.getIsSampling() == 1 ? true : false);
        abtestExp.setSamplingRatio(abtestExpInfoPO.getSamplingRatio());
        //通过函数式编程转换设置分组信息
        abtestExp.setAbtestGroups(map(groupPOList, AbtestGroupPO::mapGroup));
        return ConfigBO.builder().factorTag(abtestExp.getFactorTag()).abtestExp(abtestExp).build();
    }

    /**
     * 函数式方法转换实验分组信息
     *
     * @param list
     * @param func
     * @param <T>
     * @param <R>
     * @return
     */
    default <T, R> List<R> map(List<T> list, Function<T, R> func) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(func).collect(Collectors.toList());
    }
}
