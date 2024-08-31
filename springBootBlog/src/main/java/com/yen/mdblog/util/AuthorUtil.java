package com.yen.mdblog.util;

import com.yen.mdblog.entity.Po.Author;
import com.yen.mdblog.repository.AuthorRepository;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

public class AuthorUtil {
  public static Mono<Author> bootstrapAuthor(AuthorRepository authorRepository) {
    // Optional<Author> authorOpt = authorRepository.findById(1L);
    //Optional<Author> authorOpt = authorRepository.findById(1);
    Mono<Author> authorOpt = authorRepository.findById(1);
    return authorOpt;
    // TODO : fix below for Mono
//    if (authorOpt.isPresent()) {
//      return authorOpt.get();
//    } else {
//      Author newAuthor = new Author();
//      newAuthor.setName("yen");
//      newAuthor.setEmail("y999@gmail.com");
//      newAuthor.setUrl("yen.xx.yy");
//      newAuthor.setCreateTime(new Date());
//      newAuthor.setUpdateTime(new Date());
//      authorRepository.save(newAuthor);
//      return newAuthor;
//    }
  }
}
