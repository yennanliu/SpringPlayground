package com.vansl.sign.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: vansl
 * @create: 18-8-6 下午9:43
 */

/*发起的签到表*/
@Table(name = "t_sign")
@Entity
public class Sign extends BaseEntity{

    @Column(name = "course_id")
    private Long courseId;

    @Column
    private Boolean end;

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
