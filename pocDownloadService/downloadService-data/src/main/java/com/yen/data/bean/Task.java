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
    private Integer userId;
    private String taskName;
    private Integer startTime;
    //private BigInteger endTime;
    private String status;
}
