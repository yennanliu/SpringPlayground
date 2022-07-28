package com.yen.springMybatisDemo1.service;

import com.yen.springMybatisDemo1.bean.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestCommentService {

    @Autowired
    CommentService commentService;

    @Test
    public void test1(){

        Comment c1 = commentService.getOneComment(1);
        System.out.println(">>> c1 = " + c1);
        System.out.println(">>> c1.toString = " + c1.toString());

        List<Comment> comments = commentService.getAllComment();
        System.out.println(">>> comments = " + comments);
    }

}
