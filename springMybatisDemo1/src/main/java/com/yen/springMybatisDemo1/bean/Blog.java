package com.yen.springMybatisDemo1.bean;

// https://www.796t.com/content/1547529875.html

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Blog {

    private int id;
    private String title;
    private String content;
    private String owner;
    private List<Comment> comments;
}
