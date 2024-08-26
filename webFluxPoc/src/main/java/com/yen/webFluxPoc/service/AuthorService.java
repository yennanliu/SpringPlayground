package com.yen.webFluxPoc.service;

import com.yen.webFluxPoc.model.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorService {

    Flux<Author> findAll();

    Mono<Author> save(Author author);

    Mono<Author> findById(Integer id);

    Mono<Void> delete(Integer id);

    Mono<Author> update(Integer id, Author author);
}
