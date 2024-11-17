package com.yen.springBootAdvance1.bean;

// https://www.youtube.com/watch?v=Un_YC0fBKls&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=3
// https://www.youtube.com/watch?v=JDlq3u_EEWI&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=12

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee implements Serializable { // for save to redis

    private Integer id;
    private String lastName;
    private String email;
    private Integer gender; // 1: male, 0: female
    private Integer dId;

    public Integer getId() {
        return id;
    }

}
