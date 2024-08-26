package com.yen.webFluxPoc.service.impl;

import com.yen.webFluxPoc.model.Author;
import com.yen.webFluxPoc.repository.AuthorRepository;
import com.yen.webFluxPoc.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public Flux<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Mono<Author> save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Mono<Author> findById(Integer id) {
        return authorRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return authorRepository.deleteById(id);
    }

    @Override
    public Mono<Author> update(Integer id, Author author) {
        // TODO : use authorRepository.update instead
        // delete
        authorRepository.deleteById(id);
        // save
        return authorRepository.save(author);
    }

}
