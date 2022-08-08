package com.yen.springMybatisDemo1.bean;

// https://www.796t.com/content/1547529875.html

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {

    private int id;

    private String content;

    private Date commentDate = new Date();

    //private Blog blog;
}
