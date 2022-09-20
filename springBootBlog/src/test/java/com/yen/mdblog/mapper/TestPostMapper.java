//package com.yen.mdblog.mapper;
//
//import com.yen.mdblog.entity.Post;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//@SpringBootTest
//public class TestPostMapper {
//
//    @Autowired
//    PostMapper postMapper;
//
//    @Test
//    public void test1(){
//
//        List<Post> posts = postMapper.getAllPosts();
//        posts.forEach(x->{System.out.println(x);});
//    }
//
//
//    @Test
//    public void test3(){
//
//        Post post = postMapper.getById(1);
//        System.out.println(">>> post = " + post);
//        System.out.println(">>> post.getAuthor() = " + post.getAuthor());
//        System.out.println(">>> post.getId() = " + post.getId());
//    }
//
//}
