package com.yen.webFluxPoc.service.impl;

import com.yen.webFluxPoc.data.BookDataSource;
import com.yen.webFluxPoc.model.Book;
import com.yen.webFluxPoc.service.BookService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// https://juejin.cn/post/7129076913951211557

@Service
public class BookServiceImpl implements BookService {

  /**
   * NOTE !!!
   *
   * We simulate DB data source via below static instance
   */
  private static Flux<Book> bookData = null;

  static {
    bookData = BookDataSource.bookFlux();
  }

  @Override
  public Flux<Book> findAll() {

    return bookData;
    // return BookDataSource.bookFlux();

    // return null;
    //        return Flux.create(fluxSink -> {
    //            for (int i = 0; i < 10; i++){
    //                Book newBook = new Book();
    //                newBook.setId(i);
    //                newBook.setName("book-" + i);
    //                newBook.setAuthor("author-" + i);
    //                // add to sink (?)
    //                fluxSink.next(newBook);
    //            }
    //
    //            // call complete method after iteration end
    //            fluxSink.complete();
    //        });
  }

  @Override
  public Mono<Book> save(Book book) {
    Flux<Book> newFlux = this.createFlux(book);
    bookData = Flux.concat(bookData, newFlux);
    System.out.println("book saved, " + book);
    return Mono.just(book);
  }

  @Override
  public Mono<Book> findById(Integer id) {
    // return null;
    //        return Mono.create(callBack -> {
    //            Book newBook = new Book();
    //            newBook.setId(id);
    //            newBook.setName("book-" + id);
    //            newBook.setAuthor("author-" + id);
    //
    //            /** NOTE !!!
    //             *
    //             *  if success, return result
    //             *  can also setup "error -> {} ..." for error handling
    //             */
    //            callBack.success(newBook);
    //        });
    return bookData
        .filter(book -> book.getId().equals(id))
        .next()
        .switchIfEmpty(Mono.empty()); // Returns an empty Mono if no book is found;
  }

  @Override
  public Mono<Void> delete(Integer id) {
    // TODO : implement it
    System.out.println("delete id = " + id);
    return null;
  }

  @Override
  public Mono<Book> update(Integer id, Book book) {
    // TODO : implement it
    System.out.println("to update id = " + id + ", book = " + book);
    return null;
  }

  // help method create Flux
  private Flux<Book> createFlux(Book book) {
    return Flux.create(
        fluxSink -> {
          Book newBook = new Book();
          newBook.setId(book.getId());
          newBook.setName(book.getName());
          newBook.setAuthor(book.getAuthor());
          fluxSink.next(newBook);
          fluxSink.complete();
        });
  }
}
