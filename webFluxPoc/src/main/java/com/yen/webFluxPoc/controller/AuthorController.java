package com.yen.webFluxPoc.controller;

import com.yen.webFluxPoc.model.Author;
import com.yen.webFluxPoc.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/author")
public class AuthorController {

  @Autowired AuthorService authorService;

  @GetMapping("")
  public Flux<Author> all() {
    return this.authorService.findAll();
  }

  @PostMapping("")
  public Mono<Author> create(@RequestBody Author author) {
    return this.authorService.save(author);
  }

  @GetMapping("/{id}")
  public Mono<Author> getById(@PathVariable("id") Integer id) {
    return this.authorService.findById(id);
  }

  @PutMapping("/{id}")
  public Mono<Author> update(@PathVariable("id") Integer id, @RequestBody Author author) {
    return this.authorService.update(id, author);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> delete(@PathVariable("id") Integer id) {
    return this.authorService.delete(id);
  }

}
