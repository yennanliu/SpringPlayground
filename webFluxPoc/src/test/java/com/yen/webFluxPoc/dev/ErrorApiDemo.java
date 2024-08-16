package com.yen.webFluxPoc.dev;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// https://youtu.be/te21jRgrNSQ?si=GAbtZgQ-UvW6hHtV
public class ErrorApiDemo {

  /**
   *  Error demo
   *
   *  1) by default:
   *       subscriber can get normal element and catch error
   *       error will interrupt stream subscription
   *
   *  2) onErrorReturn :
   *      return default val when error happens
   *      error/exception will be omitted
   *      stream still can completed
   *
   *  3) onErrorResume:
   *     error/exception will be omitted
   *     run the "default" logic / method
   *
   */
  @Test
  public void error() {

    // V1 : no error handling
    Flux.just(1, 2, 0, 3)
        .map(x -> " x / 100  = " + (100 / x))
        .onErrorReturn("Divided by zero ")
        // .subscribe(y -> System.out.println("y = " + y));
        .subscribe(
            y -> System.out.println("y = " + y),
            err -> System.out.println("err = " + err),
            () -> System.out.println("stream completed"));

    // V2 :  onErrorReturn
    Flux.just(1, 2, 0, 3)
            .map(x -> " x / 100  = " + (100 / x))
            //.onErrorReturn("Divided by zero ")
            .onErrorReturn(ArithmeticException.class, "Arithmetic error")
            .subscribe(
                    y -> System.out.println("y = " + y));

    // V3 : onErrorResume
    Flux.just(1, 2, 0, 3)
            .map(x -> " x / 100  = " + (100 / x))
            .onErrorResume(err -> Mono.just("res from onErrorResume"))
            .subscribe(
                    y -> System.out.println("y = " + y));

  }

}
