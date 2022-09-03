package com.wudimanong.efence.entity.bo;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
@Builder
public class ContainPointBO implements Serializable {

    private Boolean result;
}
