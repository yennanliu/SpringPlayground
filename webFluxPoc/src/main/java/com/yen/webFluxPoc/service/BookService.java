package com.yen.webFluxPoc.service;

import com.yen.webFluxPoc.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {

  Flux<Book> findAll();

  Mono<Book> save(Book book);

  Mono<Book> findById(Integer id);

  Mono<Void> delete(Integer id);

  Mono<Book> update(Integer id, Book book);
}
