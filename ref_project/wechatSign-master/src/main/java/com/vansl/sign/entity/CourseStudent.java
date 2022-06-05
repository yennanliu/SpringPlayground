package com.vansl.sign.entity;

import javax.persistence.*;

/* 课程的学生中间表 */
@Table(name = "t_course_student",uniqueConstraints=@UniqueConstraint(columnNames={"student_id", "course_id"}) )
@Entity
public class CourseStudent extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
