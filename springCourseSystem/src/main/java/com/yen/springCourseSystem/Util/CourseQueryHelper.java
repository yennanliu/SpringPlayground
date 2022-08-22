package com.yen.springCourseSystem.Util;

// book p. 255

public class CourseQueryHelper {

    // attr
    private String qryCourseName;
    private Double qryStartPoint;
    private Double qryEndPoint;
    private String qryCourseType;

    // getter, setter
    public String getQryCourseName() {
        return qryCourseName;
    }

    public void setQryCourseName(String qryCourseName) {
        this.qryCourseName = qryCourseName;
    }

    public Double getQryStartPoint() {
        return qryStartPoint;
    }

    public void setQryStartPoint(Double qryStartPoint) {
        this.qryStartPoint = qryStartPoint;
    }

    public Double getQryEndPoint() {
        return qryEndPoint;
    }

    public void setQryEndPoint(Double qryEndPoint) {
        this.qryEndPoint = qryEndPoint;
    }

    public String getQryCourseType() {
        return qryCourseType;
    }

    public void setQryCourseType(String qryCourseType) {
        this.qryCourseType = qryCourseType;
    }

}
