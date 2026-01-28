package com.yen.mdblog.service;

import com.yen.mdblog.entity.Po.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthorService {

  Mono<Author> getById(Integer id);

  Mono<Author> getByName(String name);

  Flux<Author> getAllAuthors();

  Boolean createAuthor(String name, String email);

  void updateAuthor(Author author);
}
