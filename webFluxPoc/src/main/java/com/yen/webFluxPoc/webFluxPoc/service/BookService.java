package com.yen.webFluxPoc.webFluxPoc.service;

import com.yen.webFluxPoc.webFluxPoc.model.Book;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Service
public interface BookService {

    public Flux<Book> findAll();

    public Mono<Book> save(Book book);

    public Mono<Book> findById(Integer id);

}
