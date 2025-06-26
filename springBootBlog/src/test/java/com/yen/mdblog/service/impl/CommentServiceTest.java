//package com.yen.mdblog.service.impl;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.yen.mdblog.entity.Po.Comment;
//import com.yen.mdblog.mapper.CommentMapper;
//import com.yen.mdblog.service.CommentService;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class CommentServiceTest {
//
//  @Autowired CommentService commentService;
//
//  @Autowired CommentMapper commentMapper;
//
//  // TODO : fix Mockito mock static (?) method. ref :
//  // https://blog.csdn.net/HPLJAVA/article/details/81940368
//  @Test
//  public void getCommentsByPostId() {
//
//    List<Comment> mockedCommentList = new ArrayList<>();
//    //        Mockito.when(commentMapper.getCommentByPostId(1L))
//    //                .thenReturn(null);
//
//    //        Mockito.when(commentService.getCommentsByPostId(1L))
//    //                .thenReturn((List<Comment>) new Comment());
//
//    // assertEquals(mockedCommentList, commentService.getCommentsByPostId(1L));
//    // System.out.println(commentService.getCommentsByPostId(1L));
//  }
//}
