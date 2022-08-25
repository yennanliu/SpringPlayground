package com.yen.springMybatisDemo1.bean;

// https://www.youtube.com/watch?v=MplwOygXyBo&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39
// https://www.youtube.com/watch?v=3DntOk8Nj0A&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=43

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dept {

    private Integer did;
    private String deptName;
    // private Emp emp;  // multiple to one
}
