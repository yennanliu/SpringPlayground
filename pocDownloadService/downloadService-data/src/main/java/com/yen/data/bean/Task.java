package com.yen.data.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private Integer id;
    private Integer[] userList;
    private String[] reportField;
    private String  exportType;
    private Integer startTime;
    private Integer endTime;
    private String status;
}
