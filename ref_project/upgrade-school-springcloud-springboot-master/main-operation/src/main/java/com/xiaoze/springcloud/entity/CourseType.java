package com.xiaoze.springcloud.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * CourseType
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Data
public class CourseType implements Serializable{

    private static final long serialVersionUID = -2876033532725884341L;

    /**
     * 课程类型Id
     */
    private Integer typeId;

    /**
     * 课程类型名称
     */
    private String  typeName;

}
