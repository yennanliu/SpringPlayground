package com.yen.springCourseSystem.bean;

// book p. 246

import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import java.io.Serializable;

@Component
@TableName("course_type")
public class CourseType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    private String typeName;

    // getter, setter
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    // toString
    @Override
    public String toString() {
        return "CourseType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                '}';
    }

}
