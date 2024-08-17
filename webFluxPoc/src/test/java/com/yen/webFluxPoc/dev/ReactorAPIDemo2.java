package com.yen.webFluxPoc.dev;

// https://youtu.be/E-9UjhOu8Ps?si=_r5uf7guFVxGWm8s

import java.time.Duration;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class ReactorAPIDemo2 {

  @Test
  public void retryAndTimeOut() throws InterruptedException {

    // exception : java.util.concurrent.TimeoutException: Did not observe any item or terminal
    // signal within 1000ms in 'concatMapNoPrefetch' (and no fallback has been configured)

    Flux.just(1, 2, 3)
        .delayElements(Duration.ofSeconds(3))
        .log()
        .timeout(Duration.ofSeconds(1)) // timeout
        .retry(3) // retry
        .map(x -> " !!!")
        // .log()
        .subscribe();

    Thread.sleep(20000);
  }

  /**
   * Sinks : stream pipeline (tunnel) data flows with sink
   *
   * <p>Sinks.many(); // send a flux data Sinks.one(); // send a Mono data
   */
  @Test
  public void sinkDemo1() throws InterruptedException {

    //    Sinks.many(); // send a flux data
    //    Sinks.one(); // send a Mono data

    //    Sinks.many().unicast(); // 單播 : can only have one subscriber
    //    Sinks.many().multicast(); // 多播 : can only have multiple subscribers
    //    Sinks.many().replay(); // 重放 : can replay (resend) elements

    Sinks.Many<Object> many =
        Sinks.many()
            .unicast() // 單播
            /**
             * 背壓隊列:
             *
             * <p>(onBackpressureBuffer) : max can have 5 elements (similar as limit)
             */
            .onBackpressureBuffer(new LinkedBlockingDeque<>(5));

    //    for (int i = 0; i < 10; i++) {
    //      many.tryEmitNext("record = " + i);
    //      Thread.sleep(1000);
    //    }

    // use a new thread
    new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                many.tryEmitNext("record = " + i);
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              }
            })
        .start();

    many.asFlux().subscribe(x -> System.out.println("sub 1 = " + x));
    /**
     * exception : (since unicast(單播) can ONLY have ONE subscriber)
     *
     * <p>reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.IllegalStateException:
     * Sinks.many().unicast() sinks only allow a single Subscriber Caused by:
     * java.lang.IllegalStateException: Sinks.many().unicast() sinks only allow a single Subscriber
     */
    many.asFlux().subscribe(x -> System.out.println("sub 2 = " + x));

    Thread.sleep(20000);
  }

  @Test
  public void sinkDemo2() throws InterruptedException {

    Sinks.Many<Object> many =
        Sinks.many()
            .multicast() // 多播
            .onBackpressureBuffer();

    // use a new thread
    new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                many.tryEmitNext("record = " + i);
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              }
            })
        .start();

    many.asFlux().subscribe(x -> System.out.println("sub 1 = " + x));
    //many.asFlux().subscribe(x -> System.out.println("sub 2 = " + x));

    // or, can simulate subscriber 2 start consume after 5 sec
    new Thread(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    many.asFlux().subscribe(x -> System.out.println("sub 2 = " + x));


    // can replay as well
    //Sinks.many().replay();
    //Sinks.many().replay().latest(); // replay later
    //Sinks.many().replay().limit(5); // only replay 5 elements

    Thread.sleep(20000);
  }

}
