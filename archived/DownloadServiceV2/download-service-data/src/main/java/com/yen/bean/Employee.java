package com.yen.bean;

// https://hackmd.io/@KaiChen/HJEN-mMVw

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private Long emp_id;
    private String emp_name;
    private String emp_team;
    private Date emp_birthDate;
}
