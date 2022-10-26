package com.xiaoze.springcloud.entity;

import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * CourseType
 *
 * @author xiaoze
 * @date 2018/6/3
 *
 */
@Data
@Table(name="tbl_course_type")
public class CourseType implements Serializable{

    private static final long serialVersionUID = -2876033532725884341L;

    /**
     * 课程类型Id
     */
    @Id
    private Integer typeId;

    /**
     * 课程类型名称
     */
    private String  typeName;

    public String getTypeName() {
        return "服务1：" + typeName;
    }
}
