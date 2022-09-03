package com.wudimanong.monitor.dao.mapper;

import com.wudimanong.monitor.dao.model.TestInfoPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author jiangqiao
 */
@Repository
@Mapper
public interface TestInfoDao {

    /**
     * insert方法
     *
     * @param testInfoPO
     * @return
     */
    @Insert("insert into test_info(name,create_time,update_time) values(#{name},#{createTime,jdbcType=TIMESTAMP},#{updateTime,jdbcType=TIMESTAMP})")
    int saveTestInfo(TestInfoPO testInfoPO);

}
