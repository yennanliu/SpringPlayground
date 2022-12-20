//package com.yen.mdblog.mapper;
//
//import com.yen.mdblog.entity.Author;
//import com.yen.mdblog.service.AuthorService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@SpringBootTest
//public class TestAuthorMapper {
//
//    @Autowired
//    AuthorService authorService;
//
//    @Test
//    public void TestGetAllId(){
//        List<Author> authors = authorService.getAllAuthors();
//		//Integer[] authorId = (Integer[]) authors.stream().map(x -> x.getId()).toArray();
//        List<Long> ids = authors.stream().map(x -> x.getId()).collect(Collectors.toList());
//
//        System.out.println(">>> authors = " + authors);
//        System.out.println(">>> ids = " + ids);
//		//System.out.println(">>> authorId = " + authorId);
//        System.out.println();
//
//        authors.stream().forEach(x->{System.out.println(x);});
//        authors.stream().forEach(x->{System.out.println(x.getName());});
//    }
//
//}
