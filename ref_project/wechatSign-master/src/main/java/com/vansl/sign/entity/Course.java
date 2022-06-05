package com.vansl.sign.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Table(name = "t_course")
@Entity
public class Course extends BaseEntity{

    public Course(){}

    /* 课程名称 */
    @Column
    private String name;

    /* 教师id */
    @Column(name = "teacher_id")
    private Long teacherId;

    @OneToMany(mappedBy="course")
    @JsonIgnore
    private List<CourseStudent> courseStudents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public List<CourseStudent> getCourseStudents() {
        return courseStudents;
    }

    public void setCourseStudents(List<CourseStudent> courseStudents) {
        this.courseStudents = courseStudents;
    }
}
