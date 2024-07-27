package com.yen.webFluxPoc.webFluxPoc.service.impl;

import com.yen.webFluxPoc.webFluxPoc.model.Book;
import com.yen.webFluxPoc.webFluxPoc.service.BookService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// https://juejin.cn/post/7129076913951211557

@Service
public class BookServiceImpl implements BookService {

    @Override
    public Flux<Book> findAll() {
        //return null;
        return Flux.create(fluxSink -> {
            for (int i = 0; i < 10; i++){
                Book newBook = new Book();
                newBook.setId(i);
                newBook.setName("book-" + i);
                newBook.setAuthor("author-" + i);
                // add to sink (?)
                fluxSink.next(newBook);
            }

            // call complete method after iteration end
            fluxSink.complete();
        });
    }

    @Override
    public Mono<Book> save(Book book) {
        //return null;
        // return instance directly, via just() method
        return Mono.just(book);
    }

    @Override
    public Mono<Book> findById(Integer id) {
        //return null;
        return Mono.create(callBack -> {
            Book newBook = new Book();
            newBook.setId(id);
            newBook.setName("book-" + id);
            newBook.setAuthor("author-" + id);

            /** NOTE !!!
             *
             *  if success, return result
             *  can also setup "error -> {} ..." for error handling
             */
            callBack.success(newBook);
        });
    }

}
