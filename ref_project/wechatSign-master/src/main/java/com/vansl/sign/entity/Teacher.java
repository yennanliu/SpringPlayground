package com.vansl.sign.entity;

import javax.persistence.*;

@Table(name = "t_teacher")
@Entity
public class Teacher extends BaseEntity{

    /* 教师姓名 */
    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
