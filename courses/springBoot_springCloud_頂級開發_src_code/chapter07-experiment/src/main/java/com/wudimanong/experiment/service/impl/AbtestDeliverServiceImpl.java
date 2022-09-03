package com.wudimanong.experiment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wudimanong.experiment.client.entity.bo.ConfigBO;
import com.wudimanong.experiment.convert.AbtestExpConvert;
import com.wudimanong.experiment.dao.mapper.AbtestExpInfoDao;
import com.wudimanong.experiment.dao.mapper.AbtestGroupDao;
import com.wudimanong.experiment.dao.model.AbtestExpInfoPO;
import com.wudimanong.experiment.dao.model.AbtestGroupPO;
import com.wudimanong.experiment.service.AbtestDeliverService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Slf4j
@Service
public class AbtestDeliverServiceImpl implements AbtestDeliverService {

    /**
     * 实验信息持久层接口
     */
    @Autowired
    AbtestExpInfoDao abtestExpInfoDao;

    /**
     * 实验分组信息
     */
    @Autowired
    AbtestGroupDao abtestGroupDao;

    /**
     * 根据业务系统标识，获取实验配置信息信息（以参数factorTag为Key使用Caffeine进行缓存）
     */
    @Override
    @Cacheable(value = "EXP_CONFIG_INFO", key = "#factorTag")
    public ConfigBO getExpInfoByFactorTag(String factorTag) {
        //根据业务系统参数查询实验基本信息
        AbtestExpInfoPO abtestExpInfoPO = new AbtestExpInfoPO();
        abtestExpInfoPO.setFactorTag(factorTag);
        QueryWrapper<AbtestExpInfoPO> queryWrapper = new QueryWrapper<>(abtestExpInfoPO);
        abtestExpInfoPO = abtestExpInfoDao.selectOne(queryWrapper);
        //实验信息不存在则返回空配置
        if (abtestExpInfoPO == null) {
            return null;
        }
        //根据实验ID查询分组列表信息
        QueryWrapper<AbtestGroupPO> groupQueryWrapper = new QueryWrapper<>();
        groupQueryWrapper.eq("exp_id", abtestExpInfoPO.getId());
        List<AbtestGroupPO> groupPOList = abtestGroupDao.selectList(groupQueryWrapper);
        //转换构建返实验配置返回参数信息
        ConfigBO configBO = AbtestExpConvert.INSTANCE.convertConfig(abtestExpInfoPO, groupPOList);
        return configBO;
    }
}
