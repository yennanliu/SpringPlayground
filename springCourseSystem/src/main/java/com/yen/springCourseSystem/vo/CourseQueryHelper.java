package com.yen.springCourseSystem.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class CourseQueryHelper implements Serializable {

    private static final long serialVersionUID = -9215712224557205069L;

    /**
     * 查询课程名称字段
     */
    private String qryCourseName;

    /**
     * 查询学分的开始范围
     */
    private Double qryStartPoint;

    /**
     * 查询学分的结束范围
     */
    private Double qryEndPoint;

    /**
     * 查询课程类型字段
     */
    private String qryCourseType;

}
