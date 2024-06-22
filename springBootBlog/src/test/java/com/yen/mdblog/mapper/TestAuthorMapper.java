package com.yen.mdblog.mapper;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.service.AuthorService;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAuthorMapper {

  @Autowired AuthorService authorService;

  @Test
  public void TestGetAllId() {
    List<Author> authors = authorService.getAllAuthors();
    // Integer[] authorId = (Integer[]) authors.stream().map(x -> x.getId()).toArray();
    List<Integer> ids = authors.stream().map(x -> x.getId()).collect(Collectors.toList());

    System.out.println(">>> authors = " + authors);
    System.out.println(">>> ids = " + ids);
    // System.out.println(">>> authorId = " + authorId);
    System.out.println();

    authors.stream()
        .forEach(
            x -> {
              System.out.println(x);
            });
    authors.stream()
        .forEach(
            x -> {
              System.out.println(x.getName());
            });
  }
}
