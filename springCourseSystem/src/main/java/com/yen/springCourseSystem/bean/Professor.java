package com.yen.springCourseSystem.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@TableName("professor")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Professor implements Serializable {

    private static final long serialVersionUID = -8796984414018L;

    @TableId(type = IdType.AUTO)

    private String id;

    private String name;

    private String department;
}
