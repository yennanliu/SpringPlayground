package com.wudimanong.monitor.service.impl;

import com.wudimanong.monitor.dao.mapper.TestInfoDao;
import com.wudimanong.monitor.dao.model.TestInfoPO;
import com.wudimanong.monitor.service.MonitorService;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author jiangqiao
 */
@Service
public class MonitorServiceImpl implements MonitorService {

    /**
     * 持久层组件
     */
    @Autowired
    private TestInfoDao testInfoDao;
    /**
     * Redis访问组件
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存方法
     *
     * @param name
     * @return
     */
    @Override
    public String monitorTest(String name) {
        TestInfoPO testInfoPO = new TestInfoPO();
        testInfoPO.setName(name);
        testInfoPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        testInfoPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        //插入数据库
        testInfoDao.saveTestInfo(testInfoPO);
        //插入缓存
        redisTemplate.opsForValue().set(name, testInfoPO);
        return name;
    }
}
