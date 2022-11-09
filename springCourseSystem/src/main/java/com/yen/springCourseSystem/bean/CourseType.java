package com.yen.springCourseSystem.bean;

// book p. 246

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("course_type")
public class CourseType implements Serializable{

    private static final long serialVersionUID = 585761698369667604L;

    /**
     * 课程类型Id
     */
    @TableId(type = IdType.AUTO)
    private Integer typeId;

    /**
     * 课程类型名称
     */
    private String  typeName;

}