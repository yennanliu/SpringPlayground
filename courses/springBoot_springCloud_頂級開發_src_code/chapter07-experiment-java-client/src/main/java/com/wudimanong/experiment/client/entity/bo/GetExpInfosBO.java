package com.wudimanong.experiment.client.entity.bo;

import com.wudimanong.experiment.client.entity.AbtestExpInfo;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangqiao
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetExpInfosBO implements Serializable {

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 具体数据列表
     */
    List<AbtestExpInfo> list;
}
