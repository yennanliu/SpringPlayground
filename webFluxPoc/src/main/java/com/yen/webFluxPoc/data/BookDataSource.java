package com.yen.webFluxPoc.data;

import com.yen.webFluxPoc.model.Book;
import reactor.core.publisher.Flux;

/** simulate DB data source */
public class BookDataSource {

  public static Flux<Book> bookFlux() {
    return Flux.create(
        fluxSink -> {
          for (int i = 0; i < 10; i++) {
            Book newBook = new Book();
            newBook.setId(i);
            newBook.setName("book-" + i);
            newBook.setAuthor("author-" + i);
            fluxSink.next(newBook);
          }
          fluxSink.complete();
        });
  }
}
