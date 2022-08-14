package com.yen.springCourseSystem.bean;

// book p. 247

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

@Component
public class Course implements Serializable{

    // attr
    private String courseNo;
    private String courseName;
    private Integer courseHours;
    private String courseStatus;
    private Double coursePoint;
    private String[] courseReqs;
    private String reqs;
    private String courseMemo;
    private byte[] courseTextBookPic; // course profile pic
    private CourseType courseType;

    // getter, setter
    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(Integer courseHours) {
        this.courseHours = courseHours;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public Double getCoursePoint() {
        return coursePoint;
    }

    public void setCoursePoint(Double coursePoint) {
        this.coursePoint = coursePoint;
    }

    public String[] getCourseReqs() {
        return courseReqs;
    }

//    public void setCourseReqs(String[] courseReqs) {
//        this.courseReqs = courseReqs;
//    }

    public void setCourseReqs(String[] courseReqs) {
        this.courseReqs = courseReqs;
        StringBuffer sb = new StringBuffer();
        for (String req:courseReqs){
            // TODO : may need to fix this
            sb.append(req + "|");
        }
        sb.deleteCharAt(sb.length()-1);
        this.reqs = sb.toString();
    }

    public String getReqs() {
        return reqs;
    }

//    public void setReqs(String reqs) {
//        this.reqs = reqs;
//    }

    public void setReqs(String reqs) {
        this.reqs = reqs;
        this.courseReqs = this.reqs.split("\\/");
    }

    public String getCourseMemo() {
        return courseMemo;
    }

    public void setCourseMemo(String courseMemo) {
        this.courseMemo = courseMemo;
    }

    public byte[] getCourseTextBookPic() {
        return courseTextBookPic;
    }

    public void setCourseTextBookPic(byte[] courseTextBookPic) {
        this.courseTextBookPic = courseTextBookPic;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

}
