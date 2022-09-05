package com.wudimanong.experiment.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudimanong.experiment.dao.model.AbtestGroupPO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author jiangqiao
 */
@Repository
public interface AbtestGroupDao extends BaseMapper<AbtestGroupPO> {

    /**
     * 批量插入方法
     *
     * @param list
     * @return
     */
    int batchInsert(List<AbtestGroupPO> list);
}
