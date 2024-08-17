package com.yen.webFluxPoc.dev;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// https://youtu.be/te21jRgrNSQ?si=GAbtZgQ-UvW6hHtV
public class ErrorApiDemo {

  /**
   * Error demo
   *
   * <p>1) by default: subscriber can get normal element and catch error error will interrupt stream
   * subscription
   *
   * <p>2) onErrorReturn : return default val when error happens error/exception will be omitted
   * stream still can complete
   *
   * <p>3) onErrorResume: error/exception will be omitted run the "default" logic / method
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
        // .onErrorReturn("Divided by zero ")
        .onErrorReturn(ArithmeticException.class, "Arithmetic error")
        .subscribe(y -> System.out.println("y = " + y));

    // V3 : onErrorResume
    Flux.just(1, 2, 0, 3)
        .map(x -> " x / 100  = " + (100 / x))
        .onErrorResume(err -> Mono.just("res from onErrorResume"))
        .subscribe(y -> System.out.println("y = " + y));
  }

  @Test
  public void error2() {

    // onErrorResume with custom func
    Flux.just(1, 2, 0, 3)
        .map(x -> " x / 100  = " + (100 / x))
        .onErrorResume(err -> myErrorHandler(err))
        .subscribe(y -> System.out.println("y = " + y));
  }

  /** encapsulate error as custom business error */
  @Test
  public void BusinessError() {

    // V1
    Flux.just(1, 2, 0, 3)
        .map(x -> " x / 100  = " + (100 / x))
        .onErrorResume(err -> Flux.error(new BusinessException(err.getMessage() + " ~~!!!")))
        .subscribe(y -> System.out.println("y = " + y));

    // V2
    Flux.just(1, 2, 0, 3)
        .map(x -> " x / 100  = " + (100 / x))
        // .onErrorResume(err -> Flux.error(new BusinessException(err.getMessage() + " !!!")))
        .onErrorMap(err -> new RuntimeException(err.getMessage() + " ???!!!"))
        .subscribe(y -> System.out.println("y = " + y));
  }

  @Test
  public void doOnError() {

    Flux.just(1, 2, 0, 3)
        .map(x -> " x / 100  = " + (100 / x))
        .doOnError(err -> System.out.println("error !!! = " + err))
        .subscribe(y -> System.out.println("y = " + y));
  }

  /** doFinally */
  @Test
  public void finallyBlock() {

    Flux.just(1, 2, 0, 3)
        .map(x -> " x / 100  = " + (100 / x))
        .doOnError(err -> System.out.println("error !!! = " + err))
        .doFinally(
            signal -> {
              System.out.println("final msg : " + signal);
            })
        .subscribe(y -> System.out.println("y = " + y));
  }

  @Test
  public void continueWhenError() {

    Flux.just(1, 2, 0, 3, 5)
        .map(x -> 10 / x)
        // NOTE !!! continue execution even error happened
        .onErrorContinue((err, val)->{
          System.out.println("err = " + err + ", val = " + val);
        })
        .subscribe(x -> System.out.println(x), err -> System.out.println(err));
  }

  // https://youtu.be/te21jRgrNSQ?si=ydThlsh-5rn041ZG&t=991
  Mono<String> myErrorHandler(Throwable throwable) {
    if (throwable.getClass() == NullPointerException.class) {
      System.out.println("OHHH");
    }
    // more logic..
    return Mono.just(">>> handled error : " + throwable);
  }

  // custom biz exception
  class BusinessException extends RuntimeException {
    public BusinessException(String msg) {
      super(">>> BusinessException " + msg);
    }
  }
}
