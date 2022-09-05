package com.wudimanong.experiment.client.entity.dto;

import com.wudimanong.experiment.client.validator.EnumValue;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author jiangqiao
 */
@Data
public class GetExpInfosDTO {

    /**
     * 实验名称模糊匹配
     */
    private String nameLike;

    /**
     * 根据业务标签精准匹配
     */
    private String factorTag;

    /**
     * 状态（0 新建, 1 已发布, 2 生效中, 3 已暂停, 4 已终止, 5 已结束）
     */
    @EnumValue(intValues = {0, 1, 2, 3, 4, 5}, message = "请输入正确的实验状态值")
    private Integer status;

    /**
     * 页码
     */
    @NotNull(message = "页码不能为空")
    private Integer pageNo;

    /**
     * 每页条数
     */
    @NotNull(message = "页码大小不能为空")
    private Integer pageSize;
}
