package com.yen.springMybatisDemo1.bean;

// https://www.youtube.com/watch?v=MplwOygXyBo&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=39
// https://www.youtube.com/watch?v=3DntOk8Nj0A&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=43
// https://www.youtube.com/watch?v=nvLKTBxgIdg&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=46

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dept {

    private Integer did;
    private String deptName;
    private List<Emp> emps;  // multiple to one
}
