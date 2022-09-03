package com.wudimanong.experiment.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wudimanong.experiment.dao.mapper.AbtestExpInfoDao;
import com.wudimanong.experiment.dao.model.AbtestExpInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Service
public class CaffeineTestService {

    /**
     * 实验信息持久层接口
     */
    @Autowired
    AbtestExpInfoDao abtestExpInfoDao;

    //以参数factorTag为Key进行缓存，
    @Cacheable(value = "CAFFEINE_TEST", key = "#factorTag", sync = true)
    public AbtestExpInfoPO getExpInfoByFactorTag(String factorTag) {
        //封装查询参数
        AbtestExpInfoPO abtestExpInfoPO = new AbtestExpInfoPO();
        abtestExpInfoPO.setFactorTag(factorTag);
        QueryWrapper<AbtestExpInfoPO> queryWrapper = new QueryWrapper<>(abtestExpInfoPO);
        //数据库查询实验配置信息
        abtestExpInfoPO = abtestExpInfoDao.selectOne(queryWrapper);
        return abtestExpInfoPO;
    }
}
