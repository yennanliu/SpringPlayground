package com.yen.springMybatisDemo1.bean;

// https://www.youtube.com/watch?v=MplwOygXyBo&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39
// https://www.youtube.com/watch?v=3DntOk8Nj0A&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=43
// https://www.youtube.com/watch?v=jlr8Rc77boA&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=58

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Emp implements Serializable {

    private Integer eid;
    private String empName;
    private Integer age;
    private String sex;
    private String email;
    private Integer did;
    private Dept dept; // multiple to one

    // constructor
    public Emp(String empName, Integer age, String sex, String email) {
        this.empName = empName;
        this.age = age;
        this.sex = sex;
        this.email = email;
    }

}
