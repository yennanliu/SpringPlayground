package com.wudimanong.experiment.client.entity.bo;

import java.io.Serializable;
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
public class CreateExpBO implements Serializable {

    /**
     * 是否创建成功
     */
    private Boolean isSuccess;

    /**
     * 返回实验ID
     */
    private Integer expId;
}
