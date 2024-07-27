package com.yen.webFluxPoc.service;

import com.yen.webFluxPoc.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {

    public Flux<Book> findAll();

    public Mono<Book> save(Book book);

    public Mono<Book> findById(Integer id);

    public Mono<Void> delete(Integer id);

    Mono<Book> update(Integer id, Book book);
}
