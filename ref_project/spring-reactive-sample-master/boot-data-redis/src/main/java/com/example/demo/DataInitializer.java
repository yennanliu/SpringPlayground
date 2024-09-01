package com.example.demo;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
class DataInitializer implements CommandLineRunner {

  private final PostRepository posts;

  public DataInitializer(PostRepository posts) {
    this.posts = posts;
  }

  @Override
  public void run(String[] args) {
    log.info("start data initialization  ...");
    this.posts
        .deleteAll()
        .thenMany(
            Flux
                .just("Post one", "Post two")
                .flatMap(
                    title -> {
                      String id = UUID.randomUUID().toString();
                      return this.posts.save(
                          Post.builder().id(id).title(title).content("content of " + title)
                              .build());
                    })
                .subscribeOn(Schedulers.boundedElastic()))
        .log()
        .subscribe(
            null,
            null,
            () -> log.info("done initialization..."));

  }

}
