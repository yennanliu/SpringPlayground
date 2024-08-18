package com.yen.webFluxPoc.dev;

// https://youtu.be/E-9UjhOu8Ps?si=_r5uf7guFVxGWm8s

import java.time.Duration;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

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
   * <p>
   *     Sinks.many(); // send a flux data
   *     Sinks.one(); // send a Mono data
   *
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
             * <p>(onBackpressureBuffer) : can have 5 elements at most (similar as limit)
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
    // many.asFlux().subscribe(x -> System.out.println("sub 2 = " + x));

    // or, can simulate that subscriber 2 start consume after 5 sec (delay with 5 sec)
    new Thread(
        () -> {
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });

    many.asFlux().subscribe(x -> System.out.println("sub 2 = " + x));

    // can replay as well
    // Sinks.many().replay();
    // Sinks.many().replay().latest(); // replay later
    // Sinks.many().replay().limit(5); // only replay 5 elements

    Thread.sleep(20000);
  }

  /** Cache demo */
  @Test
  public void cache() throws InterruptedException {

    Flux<Integer> cache =
        Flux.range(1, 10)
            .delayElements(Duration.ofSeconds(1))
            /** Can cache 3 elements */
            .cache(3);
    // .subscribe(x-> System.out.println(x));

    cache.subscribe();

    new Thread(
            () -> {
              // start process after 7 sec
              try {
                Thread.sleep(7000);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              cache.subscribe(x -> System.out.println(x));
            })
        .start();

    Thread.sleep(20000);
  }

  @Test
  public void blockAPI() {

    List<Integer> integerList =
        Flux.just(1, 2, 3)
            .map(x -> x + 10)
            .collectList()
            .block(); // block here (one of the subscribers)

    System.out.println("integerList = " + integerList);
  }

  /** parallel demo */
  @Test
  public void parallelFlux() throws InterruptedException {

    /**
     * V1
     *
     * <p>log :
     *
     * <p>[1, 2, 3, 4, 5, 6, 7, 8, 9, 10] [11, 12, 13, 14, 15, 16, 17, 18, 19, 20] [21, 22, 23, 24,
     * 25, 26, 27, 28, 29, 30] [31, 32, 33, 34, 35, 36, 37, 38, 39, 40] [41, 42, 43, 44, 45, 46, 47,
     * 48, 49, 50] [51, 52, 53, 54, 55, 56, 57, 58, 59, 60] [61, 62, 63, 64, 65, 66, 67, 68, 69, 70]
     * [71, 72, 73, 74, 75, 76, 77, 78, 79, 80] [81, 82, 83, 84, 85, 86, 87, 88, 89, 90] [91, 92,
     * 93, 94, 95, 96, 97, 98, 99, 100]
     */
    //    Flux.range(1, 100)
    //            .buffer(10)
    //            .subscribe(x->System.out.println(x));

    /** V2 */
    Flux.range(1, 100)
        .buffer(10)
        .publishOn(Schedulers.newParallel("xx--"))
        .parallel(8)
        .runOn(Schedulers.newParallel("yy--"))
        // .log()
        .flatMap(list -> Flux.fromIterable(list))
        .collectSortedList(Integer::compareTo)
        .subscribe(x -> System.out.println(x));

    Thread.sleep(20000);
  }

  /**
   * contextAPI demo
   *
   * <p>1. threadLocal CAN'T be used in reactor API 2. use contextAPI share data within context -
   * Context : read, write - ContextView : read
   */
  @Test
  public void contextAPI() {

    /** NOTE !!! have to use API that support context */
    Flux.just(1, 2, 3)
        .transformDeferredContextual(
            (flux, context) -> {
              System.out.println("flux = " + flux);
              System.out.println("context = " + context);
              //return Flux.just(2, 3, 4);
                return flux.map(i -> i + " ==> " + context.get("prefix"));
            })
        .map(
            x -> {
              System.out.println("x = " + 1);
              return x + 10;
            })
        // upstream can get the newest data from downstream****
        .contextWrite(Context.of("prefix", " lol "))
        /**
         * ThreadLocal share data, upstream can see data, Context send data from upstream to
         * downstream
         */
        .subscribe(x -> System.out.println(x));
  }
}
